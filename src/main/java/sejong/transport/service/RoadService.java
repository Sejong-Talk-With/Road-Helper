package sejong.transport.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import sejong.transport.domain.etc.*;

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
@RequiredArgsConstructor
public class RoadService {

    private final ElevatorService elevatorService;

    public List<RouteDetail> findAllRoutes(Point start, Point end, UserType userType) {
        List<RouteDetail> originalRoutes = new ArrayList<>();
        try{
            System.out.println("start = " + start);
            System.out.println("end = " + end);
            originalRoutes = searchRoad(start, end, userType);

        }catch(Exception e){
            e.printStackTrace();
        }

        return originalRoutes;
    }

    // 장소 검색 (출발지 검색, 도착지 검색)
    public Point searchPlace(String point) throws IOException, ParseException {
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
        Long id = Long.parseLong((String) startPoint.get("id"));
        String name = (String) startPoint.get("title");
        Double x = Double.parseDouble((String) startPoint.get("x"));
        Double y = Double.parseDouble((String) startPoint.get("y"));
        return new Point(id, name, encodedName, x, y);
    }

    // 길 찾기
    private List<RouteDetail> searchRoad(Point start, Point end, UserType userType) throws IOException, ParseException {
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

        Walking firstRoute = new Walking(steps.get(0), userType);
        firstRoute.setStart(new Point(start));
        allRoutes.add(firstRoute);

        for (int i = 1; i < steps.size()-1; i++) {

            JSONObject first = steps.get(i - 1);
            String firstType = (String) first.get("type");
            JSONObject mid = steps.get(i);
            String midType = (String) mid.get("type");
            JSONObject last = steps.get(i+1);
            String lastType = (String) last.get("type");

            if (firstType.equals("SUBWAY") && midType.equals("WALKING") && lastType.equals("SUBWAY")) {
                continue;
            }

            Route midRoute;
            if (midType.equals("SUBWAY")) {
                midRoute = new Subway(mid);
            } else if (midType.equals("WALKING")) {
                midRoute = new Walking(mid, userType);
            } else { // (midType.equals("BUS"))
                midRoute = new Bus(mid);
            }
            allRoutes.add(midRoute);
//            Route before = allRoutes.get(i - 1);
//            before.setEnd(new Point(midRoute.getStart()));

        }
        Walking lastRoute = new Walking(steps.get(steps.size()-1), userType);
        lastRoute.setEnd(new Point(end));
        allRoutes.add(lastRoute);

        for (int i = 0; i < allRoutes.size(); i++) {
            Route route = allRoutes.get(i);
            if (route instanceof Walking) {
                if (i == 0) {
                    Route nextRoute = allRoutes.get(i + 1);
                    route.setEnd(new Point(nextRoute.getStart()));
                } else if (i == allRoutes.size() - 1) {
                    Route prevRoute = allRoutes.get(i - 1);
                    route.setStart(new Point(prevRoute.getEnd()));
                } else {
                    Route nextRoute = allRoutes.get(i + 1);
                    route.setEnd(new Point(nextRoute.getStart()));
                    Route prevRoute = allRoutes.get(i - 1);
                    route.setStart(new Point(prevRoute.getEnd()));
                }
            }
        }

        List<RouteDetail> allRouteDetails = new ArrayList<>();
        RouteDetail subway = null;
        List<StationDetail> subwayList = new ArrayList<>();
        boolean check = Boolean.TRUE;
        for (Route route : allRoutes) {
            RouteDetail routeDetail = new RouteDetail(route);
            if (route instanceof Subway) {
                if (check) {
                    subway = routeDetail;
                    subwayList = subway.getSubwayDetails();
                    subwayList.add(new StationDetail(route, true));
                    check = Boolean.FALSE;
                    subwayList.add(new StationDetail(route, false));
                    allRouteDetails.add(subway);
                } else {
                    String description = subway.getDescription();
                    String newDescription = String.format("%s → %s : %d분, %d개역", route.getStart().getName(), route.getEnd().getName(), route.getDuration(), route.getStationCnt());
                    subway.setDescription(description + "<br>" + newDescription);
                    subway.setDuration(subway.getDuration() + route.getDuration());
                    subway.setStationCnt(subway.getStationCnt() + route.getStationCnt() - 1);
                    String before = subway.getTitle();
                    subway.setTitle(before + " → " + route.getEnd().getName());
                    subwayList.add(new StationDetail(route, false));
                }
            } else {
                allRouteDetails.add(new RouteDetail(route));
            }
        }
        if (subway != null) {
            List<StationDetail> subwayDetails = subway.getSubwayDetails();
            StationDetail lastStation = subwayDetails.get(subwayDetails.size()-1);
            lastStation.setSubwayType("하차");
            lastStation.setTitle(String.format("%s 안내도 (%s호선 %s) ", lastStation.getName(),lastStation.getLine(), lastStation.getSubwayType()));
            lastStation.setDescription("안내도를 따라 <br> <b>외부</b>까지 엘리베이터를 이용하세요!");

            for (int i = 1; i < subwayDetails.size()-1; i++) {
                StationDetail stationDetail = subwayDetails.get(i);
                if (stationDetail.getSubwayType().equals("환승")) {
                    StationDetail nextStation = subwayDetails.get(i + 1);
                    stationDetail.setDirection(nextStation.getDirection());
                    stationDetail.setLine(String.format("%s→%s호선", stationDetail.getLine(), nextStation.getLine()));
                    stationDetail.setTitle(String.format("%s 안내도 (%s, %s %s) ", stationDetail.getName(), stationDetail.getSubwayType(), stationDetail.getLine() ,stationDetail.getDirection()));
                }
            }
        }

        return allRouteDetails;
    }


    public List<RouteDetail> findEachRouteDetail(List<Route> allRoutes) {
        List<RouteDetail> allRoutesDetail = new ArrayList<>();
        for (Route route : allRoutes) {
            allRoutesDetail.add(new RouteDetail(route));
        }
        return allRoutesDetail;
    }
}
