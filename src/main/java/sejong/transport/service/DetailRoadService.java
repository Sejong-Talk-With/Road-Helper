package sejong.transport.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.transport.domain.etc.*;
import sejong.transport.repository.StationRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DetailRoadService {

    private final StationRepository stationRepository;

    public String getHtml() {
        // 지도
        System.setProperty("webdriver.chrome.driver", "src/main/resources/static/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);

        String rt = "%2C1124891%2C516673%2C1124865";
        String rt1 = "세종대학교+정문";
        String rt2 = "어린이대공원역+7호선+1번출구";

        String temp_url = "https://map.kakao.com/?map_type=TYPE_MAP&target=walk&rt=516568%2C1124891%2C516673%2C1124865&rt1=%EC%84%B8%EC%A2%85%EB%8C%80%ED%95%99%EA%B5%90+%EC%A0%95%EB%AC%B8&rt2=%EC%96%B4%EB%A6%B0%EC%9D%B4%EB%8C%80%EA%B3%B5%EC%9B%90%EC%97%AD+7%ED%98%B8%EC%84%A0+1%EB%B2%88%EC%B6%9C%EA%B5%AC&rtIds=194081591%2C10569629&rtTypes=PLACE%2CPLACE";
        System.out.println("temp_url = " + temp_url);

        String map_html = "";
        try{
            driver.get(temp_url);
            WebElement tmp = driver.findElement(By.xpath("//*[@id=\'view.mapContainer\']/div[2]"));
            map_html = tmp.getAttribute("innerHTML");
        }catch (Exception e){
            e.printStackTrace();
        }

        return map_html;
    }

    public ResultDetail findWalkingDetail(Point start, Point end, UserType userType) throws IOException, ParseException {
        String src = String.format("https://map.naver.com/v5/api/dir/findwalk?lo=ko&st=1&o=all&l=%s,%s,%s,%s;%s,%s,%s,%s&lang=ko",
                start.longitude, start.latitude, start.encodedName, start.id,
                end.longitude, end.latitude, end.encodedName, end.id
        );
        URL url = new URL(src);
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        JSONParser jsonParser = new JSONParser();
        String result = bf.readLine();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        List<JSONObject> routes = (List<JSONObject>) jsonObject.get("routes");
        JSONObject route = routes.get(0);
        JSONObject summary = (JSONObject) route.get("summary");
        List<JSONObject> legs = (List<JSONObject>) route.get("legs");
        JSONObject leg = legs.get(0);
        List<JSONObject> steps = (List<JSONObject>) leg.get("steps");
        List<DetailElements> detailElementsList = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            if (i == 0) {
                detailElementsList.add(new DetailElements(steps.get(i), userType, 0));
            } else if (i == steps.size() - 1) {
                detailElementsList.add(new DetailElements(steps.get(i), userType, -1));

            } else {
                detailElementsList.add(new DetailElements(steps.get(i), userType, 1));
            }
        }
        return new ResultDetail(start, end, (Long) summary.get("distance"), Math.round((Long) summary.get("duration") * userType.getRate()), detailElementsList);
    }

    public ResultDetail findBusDetail(Point start, Point end) throws ParseException, IOException {
        String src = String.format("https://map.naver.com/v5/api/transit/directions/point-to-point?start=%s,%s,placeid=%s,name=%s&goal=%s,%s,placeid=%s,name=%s&crs=EPSG:4326&mode=STATIC&lang=ko&includeDetailOperation=true",
                start.longitude, start.latitude, start.id, start.encodedName,
                end.longitude, end.latitude, end.id, end.encodedName
        );
        URL url = new URL(src);
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        JSONParser jsonParser = new JSONParser();
        String result = bf.readLine();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        List<JSONObject> paths = (List<JSONObject>) jsonObject.get("staticPaths");
        List<JSONObject> firstPath = (List<JSONObject>) paths.get(0).get("legs");
        List<JSONObject> steps = (List<JSONObject>) firstPath.get(0).get("steps");
        JSONObject step = steps.get(0);
        List<JSONObject> stations = (List<JSONObject>) step.get("stations");
        Long duration = (Long) step.get("duration");
        int stationCnt = stations.size();
        List<JSONObject> routes = (List<JSONObject>) step.get("routes");
        JSONObject route = routes.get(0);
        String busName = (String) route.get("name");
        List<DetailElements> detailElementsList = new ArrayList<>();
        for (JSONObject station : stations) {
            DetailElements detailElements = new DetailElements();
            detailElements.setTitle((String) station.get("name"));
            detailElementsList.add(detailElements);
        }
        ResultDetail resultDetail = new ResultDetail();
        resultDetail.setTitle(String.format("%s번 버스 (%s 정류장)", busName, stations.get(0).get("name")));
        resultDetail.setDuration(duration);
        resultDetail.setStationCnt(stationCnt);
        resultDetail.setDetailInfos(detailElementsList);
        return resultDetail;
    }
}
