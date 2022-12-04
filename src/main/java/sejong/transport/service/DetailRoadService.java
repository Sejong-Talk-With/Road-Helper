package sejong.transport.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
