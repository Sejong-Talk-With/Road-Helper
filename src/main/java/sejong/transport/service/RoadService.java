package sejong.transport.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import sejong.transport.domain.etc.Route;
import sejong.transport.domain.etc.SearchForm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.*;
import sejong.transport.domain.etc.traffictype.Bus;
import sejong.transport.domain.etc.traffictype.Subway;
import sejong.transport.domain.etc.traffictype.Walking;

@Service
public class RoadService {


    public void findRoad(SearchForm searchForm) {
        try{
            Point start = searchPlace(searchForm.start);
            Point end = searchPlace(searchForm.end);
            searchRoad(start, end);
            System.out.println("start = " + start);
            System.out.println("end= " + end);

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    // 장소 검색 (출발지 검색, 도착지 검색)
    private Point searchPlace(String point) throws IOException, ParseException {
        String encodedName = URLEncoder.encode(point, StandardCharsets.UTF_8);
        String src = "https://map.naver.com/v5/api/instantSearch?lang=ko&caller=pcweb&types=place,address&coords=1,1&query=" + encodedName;
        URL url = new URL(src);
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        JSONParser jsonParser = new JSONParser();
        String result = bf.readLine();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        List<JSONObject> places = (List<JSONObject>) jsonObject.get("place");
        JSONObject startPoint = places.get(0);
        String id = (String) startPoint.get("id");
        String name = (String) startPoint.get("title");
        String x = (String) startPoint.get("x");
        String y = (String) startPoint.get("y");
        return new Point(id, name, encodedName, x, y);
    }

    // 길 찾기
    private void searchRoad(Point start, Point end) throws IOException, ParseException {
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
        List<Route> allRoutes = new ArrayList<>();
        Route firstRoute = new Walking(steps.get(0));
        Route lastRoute = new Walking(steps.get(steps.size()-1));
        allRoutes.add(firstRoute);
        for (JSONObject step : steps) {
            System.out.println("step = " + step.get("type"));
        }
        for (int i = 2; i < steps.size(); i++) {
            JSONObject first = steps.get(i - 2);
            String firstType = (String) first.get("type");
            JSONObject mid = steps.get(i - 1);
            String midType = (String) mid.get("type");
            JSONObject last = steps.get(i);
            String lastType = (String) last.get("type");
            if (firstType.equals("SUBWAY") && midType.equals("WALKING") && lastType.equals("SUBWAY")) {
                continue;
            }

            Route midRoute;
            if (midType.equals("SUBWAY")) {
                midRoute = new Subway(mid);
            } else if (midType.equals("WALKING")) {
                midRoute = new Walking(mid);
            } else { // (midType.equals("BUS"))
                midRoute = new Bus(mid);
            }
            allRoutes.add(midRoute);
        }
        allRoutes.add(lastRoute);
        for (Route route : allRoutes) {
            if (route instanceof Walking walking) {
                System.out.println("walking = " + walking);
            }
            else if (route instanceof Subway subway) {
                System.out.println("subway = " + subway);
            }
            else if (route instanceof Bus bus) {
                System.out.println("bus = " + bus);
            }
        }

    }

    @Data
    @AllArgsConstructor
    static class Point {
        public String id;
        public String name;
        public String encodedName;
        public String longitude;
        public String latitude;
    }


}
