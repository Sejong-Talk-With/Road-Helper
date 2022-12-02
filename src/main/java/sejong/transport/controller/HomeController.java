package sejong.transport.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sejong.transport.domain.etc.SearchForm;
import sejong.transport.domain.etc.Times;
import sejong.transport.domain.etc.UserType;
import sejong.transport.domain.etc.usertype.Elder;
import sejong.transport.domain.etc.usertype.Pregnant;
import sejong.transport.domain.etc.usertype.Wheel;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String home(@ModelAttribute(name = "searchForm") SearchForm searchForm, Model model) {
        List<UserType> types = new ArrayList<>(List.of(new UserType(),new Wheel(), new Pregnant(), new Elder()));
        model.addAttribute("types", types);
        return "home";
    }

    @PostMapping("/")
    public String homeSearch(@ModelAttribute(name = "searchForm") SearchForm searchForm, Model model) {
        model.addAttribute("start", searchForm.start);
        model.addAttribute("end", searchForm.end);

        List<Route> allRoutes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Route route = new Route("세종대학교 정문","어린이대공원역 1번 출구","근처 엘레베이터를 이용하세요",50,i%2,10);
            allRoutes.add(route);
        }

        List<Route> detailRoutes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Route detail = new Route("세종대학교 정문","어린이대공원역 1번 출구","근처 엘레베이터를 이용하세요",50,i%2,10);
            detailRoutes.add(detail);
        }

        model.addAttribute("allRoutes", allRoutes);
        model.addAttribute("detailRoutes", detailRoutes);
        model.addAttribute("optimal","15:00");

        return "search";
    }

    @GetMapping("/detail")
    public String detailSearch(@ModelAttribute(name = "searchForm") SearchForm searchForm, Model model) {

        Route singleRoutes = new Route("세종대학교 정문","어린이대공원역 1번 출구","근처 엘레베이터를 이용하세요",50,0,10);
        model.addAttribute("singleRoutes", singleRoutes);

        List<Route> allRoutes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Route route = new Route("횡단보도","1번 출구 근처 엘레베이터","예상 시간 5분",50,i,10);
            allRoutes.add(route);
        }

        List<Route> detailRoutes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Route detail = new Route("횡단보도","1번 출구 근처 엘레베이터","예상 시간 5분",50,i,10);
            detailRoutes.add(detail);
        }

        model.addAttribute("allRoutes", allRoutes);
        model.addAttribute("detailRoutes", detailRoutes);
        return "detail";

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
