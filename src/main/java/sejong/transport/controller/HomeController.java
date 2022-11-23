package sejong.transport.controller;

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
import sejong.transport.service.RoadService;

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
    public String homeSearch(@ModelAttribute(name = "searchForm") SearchForm searchForm, Model model) {
        System.out.println("searchForm = " + searchForm);
        roadService.findRoad(searchForm);
        return "result";
    }

}
