package sejong.transport.controller;

import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sejong.transport.domain.etc.*;
import sejong.transport.domain.etc.traffictype.Bus;
import sejong.transport.domain.etc.traffictype.Subway;
import sejong.transport.domain.etc.traffictype.Walking;
import sejong.transport.domain.etc.usertype.Elder;
import sejong.transport.domain.etc.usertype.Pregnant;
import sejong.transport.domain.etc.usertype.Wheel;
import sejong.transport.service.RoadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final RoadService roadService;

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
        List<RouteDetail> allRoutes = roadService.findAllRoutes(start, end, searchForm.userType);
        model.addAttribute("start", start.name);
        model.addAttribute("end", end.name);
        model.addAttribute("optimal", "15:00");
        model.addAttribute("allRoutes", allRoutes);
        for (RouteDetail allRoute : allRoutes) {
            List<StationDetail> subwayDetails = allRoute.getSubwayDetails();
            if (subwayDetails.size() != 0) {
                for (StationDetail subwayDetail : subwayDetails) {
                    System.out.println("subwayDetail = " + subwayDetail);
                }
            }
        }

        return "result";
    }

}
