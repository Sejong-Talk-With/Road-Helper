package sejong.transport.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sejong.transport.domain.etc.*;
import sejong.transport.domain.etc.traffictype.Bus;
import sejong.transport.domain.etc.traffictype.Subway;
import sejong.transport.domain.etc.traffictype.Walking;
import sejong.transport.domain.etc.usertype.Elder;
import sejong.transport.domain.etc.usertype.Pregnant;
import sejong.transport.domain.etc.usertype.Wheel;
import sejong.transport.service.DetailRoadService;
import sejong.transport.service.RoadService;
import sejong.transport.service.StationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static sejong.transport.domain.etc.UserTypeConst.pregnant;
import static sejong.transport.domain.etc.UserTypeConst.elder;
import static sejong.transport.domain.etc.UserTypeConst.wheel;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RoadService roadService;
    private final DetailRoadService detailRoadService;
    private final StationService stationService;

    @GetMapping("/")
    public String home(@ModelAttribute(name = "searchForm") SearchForm searchForm, Model model) {
        List<UserType> types = new ArrayList<>(List.of(new UserType(),new Wheel(), new Pregnant(), new Elder()));
        model.addAttribute("types", types);
        return "home";
    }

    @PostMapping("/")
    public String homeSearch(@ModelAttribute(name = "searchForm") SearchForm searchForm, Model model) throws IOException, ParseException {
        Point start = roadService.searchPlace(searchForm.getStart());
        Point end = roadService.searchPlace(searchForm.getEnd());
        List<RouteDetail> allRoutes = roadService.findAllRoutes(start, end, searchForm.getUserType());
        model.addAttribute("start", start.name);
        model.addAttribute("end", end.name);
        model.addAttribute("optimal", roadService.getOptimalTime(searchForm.getTime1(), searchForm.getTime2()));
        model.addAttribute("allRoutes", allRoutes);
        model.addAttribute("userType", searchForm.getUserType());

        for (RouteDetail allRoute : allRoutes) {
            System.out.println("allRoute = " + allRoute);
        }

        return "result";
    }

    @GetMapping("/detail/walking")
    public String detailWalking(@RequestParam(name = "sId") Long startId, @RequestParam(name = "sLongitude") Double startLong,
                               @RequestParam(name = "sLatitude") Double startLat, @RequestParam(name = "sName") String startName,
                               @RequestParam(name = "eId") Long endId, @RequestParam(name = "eLongitude") Double endLong,
                               @RequestParam(name = "eLatitude") Double endLat, @RequestParam(name = "eName") String endName,
                               @RequestParam(name = "userType") String type, Model model) throws IOException, ParseException {

        Point start = new Point(startId, startName, startLong, startLat);
        Point end = new Point(endId, endName, endLong, endLat);
        SearchForm temp = new SearchForm();
        temp.setType(type);
        ResultDetail walkingDetail = detailRoadService.findWalkingDetail(start, end, temp.getUserType());
        model.addAttribute("routeDetail", walkingDetail);
        return "walkingDetail";

    }

    @GetMapping("/detail/bus")
    public String detailBus(@RequestParam(name = "sId") Long startId, @RequestParam(name = "sLongitude") Double startLong,
                                @RequestParam(name = "sLatitude") Double startLat, @RequestParam(name = "sName") String startName,
                                @RequestParam(name = "eId") Long endId, @RequestParam(name = "eLongitude") Double endLong,
                                @RequestParam(name = "eLatitude") Double endLat, @RequestParam(name = "eName") String endName, @RequestParam(name = "userType") String type,
                            Model model) throws IOException, ParseException {

        Point start = new Point(startId, startName, startLong, startLat);
        Point end = new Point(endId, endName, endLong, endLat);
        ResultDetail busDetail = detailRoadService.findBusDetail(start, end);
        model.addAttribute("routeDetail", busDetail);
        int[] busTimes;
        if (type.equals(wheel)) {
            model.addAttribute("busType", "저상");
            busTimes = detailRoadService.getBusTime(start, end, 0);
        } else {
            model.addAttribute("busType", "일반");
            busTimes = detailRoadService.getBusTime(start, end, 1);
        }
        if (type.equals(wheel)) {

        }
        model.addAttribute("lowBus1", busTimes[0]);
        model.addAttribute("lowBus2", busTimes[1]);
        return "busDetail";

    }

    @GetMapping("/detail/subway")
    public String detailSubway(@RequestParam(name = "name") String name, @RequestParam(name = "exitNum") String exitNum,
                               @RequestParam(name = "subwaytype") String subwayType, @RequestParam(name = "direction") String direction,
                               @RequestParam(name = "line") String line, @RequestParam(name = "beforeLine") String beforeLine, @RequestParam(name = "afterLine") String afterLine, Model model) {
        if (subwayType.equals("환승")) {
            TransferDto info = stationService.getTInfo(name, beforeLine, afterLine, direction);
            model.addAttribute("stationInfo", info);
        } else {
            StationDto info = stationService.getInfo(name, exitNum, subwayType, direction, line);
            model.addAttribute("stationInfo", info);
        }

        return "subwayDetail";
    }



    @Data
    static class Route {
        public String start;
        public String end;
        public String description;
        public int distance;
        public int odd;
        public int time;

        public Route(String start, String end, String description, int distance, int odd, int time) {
            this.start = start;
            this.end = end;
            this.description = description;
            this.distance = distance;
            this.odd = odd;
            this.time = time;
        }
    }

}
