package sejong.transport.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import sejong.transport.domain.entity.Elevator;
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
            originalRoutes = searchRouteDetails(start, end, userType);
        }catch(Exception e){
            e.printStackTrace();
        }

        return originalRoutes;
    }

    private Route makeBestWalkingRoute(Route route, UserType userType) throws IOException, ParseException {
        Point start = route.getStart();
        Point end = route.getEnd();
        String startName = start.getName();
        String endName = end.getName();
        if (startName.endsWith("역")) {
            double minDistance = 1000;
            int exitNum = 0;
            List<Elevator> elevators = elevatorService.findByStationName(startName);
            if (elevators.size() == 0) {
                return route;
            }
            Double latitude = end.getLatitude();
            Double longitude = end.getLongitude();
            for (Elevator elevator : elevators) {
                System.out.println("elevator = " + elevator.getStationName());
                double lat = elevator.getLatitude();
                System.out.println("can_lat = " + lat);
                double log = elevator.getLongitude();
                System.out.println("can_log = " + log);
                double x = Math.abs(latitude - lat);
                System.out.println("x = " + x);
                double y = Math.abs(longitude - log);
                System.out.println("y = " + y);
                if (minDistance > (x + y)) {
                    minDistance = x + y;
                    exitNum = elevator.getExitNum();
                    System.out.println("minDistance = " + minDistance);
                    System.out.println("exitNum = " + exitNum);
                }
            }
            String startPlace = startName + " " + String.valueOf(exitNum);
            System.out.println("startPlace = " + startPlace);
            Point newStart = searchPlace(startPlace);
            route.setStart(newStart);

        } else if (endName.endsWith("역")) {
            double minDistance = 1000;
            int exitNum = 0;
            List<Elevator> elevators = elevatorService.findByStationName(endName);
            if (elevators.size() == 0) {
                return route;
            }
            Double latitude = start.getLatitude();
            Double longitude = start.getLongitude();
            System.out.println("lat = "+latitude);
            System.out.println("long = "+longitude);
            for (Elevator elevator : elevators) {
                System.out.println("elevator = " + elevator.getStationName());
                double lat = elevator.getLatitude();
                System.out.println("can_lat = " + lat);
                double log = elevator.getLongitude();
                System.out.println("can_log = " + log);
                double x = Math.abs(latitude - lat);
                System.out.println("x = " + x);
                double y = Math.abs(longitude - log);
                System.out.println("y = " + y);
                if (minDistance > (x + y)) {
                    minDistance = x + y;
                    exitNum = elevator.getExitNum();
                    System.out.println("minDistance = " + minDistance);
                    System.out.println("exitNum = " + exitNum);
                }
            }
            String endPlace = endName + " " + String.valueOf(exitNum) + "번출구";
            System.out.println("endPlace = " + endPlace);
            Point newEnd = searchPlace(endPlace);
            route.setEnd(newEnd);
        }

        return route;
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
    private List<RouteDetail> searchRouteDetails(Point start, Point end, UserType userType) throws IOException, ParseException {
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
            if (route instanceof Subway) {
                RouteDetail routeDetail = new RouteDetail(route);
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
                if (route instanceof Walking) {
                    Route route1 = makeBestWalkingRoute(route, userType);
                    Route newRoute = searchWalk(route1.getStart(), route1.getEnd(), userType);
                    RouteDetail routeDetail = new RouteDetail(newRoute);
                } else {
                    RouteDetail routeDetail = new RouteDetail(route);
                }
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

    private Route searchWalk(Point start, Point end, UserType userType) throws IOException, ParseException {
        String src = String.format("https://map.naver.com/v5/api/dir/findwalk?lo=ko&st=1&o=all&l=%s,%s,%s,%s;%s,%s,%s,%s&lang=ko",
                start.longitude, start.latitude, start.encodedName, start.id,
                end.longitude, end.latitude, end.encodedName, end.id
        );
        URL url = new URL(src);
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        JSONParser jsonParser = new JSONParser();
        String result = bf.readLine();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(result);
        List<JSONObject> routes = (List<JSONObject>) jsonObject.get("routes");
        JSONObject route = routes.get(0);
        JSONObject summary = (JSONObject) route.get("summary");
        return new Walking(summary, start, end, userType);
    }
}
