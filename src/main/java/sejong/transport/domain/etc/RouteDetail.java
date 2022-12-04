package sejong.transport.domain.etc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import sejong.transport.domain.etc.traffictype.Bus;
import sejong.transport.domain.etc.traffictype.Subway;
import sejong.transport.domain.etc.traffictype.Walking;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class RouteDetail {

    public Route route;
    public String trafficType; // walking, subway, bus

    // for common
    public String title;
    public String description;
    public Long duration;

    // for walking
    public Long distance;

    // for bus, subway
    public int stationCnt;

    // for subway
    public String inExitNum;
    public String outExitNum;
    public List<StationDetail> subwayDetails = new ArrayList<>();

    public RouteDetail(Route route) {
        this.route = route;
        if (route instanceof Walking) {
            this.title = String.format("%s → %s", route.getStart().getName(), route.getEnd().getName());
            this.description = String.format("근처 엘리베이터를 이용하세요! <br> 거리: %dm, 시간: %d분 %d초", route.getDistance(), route.getDuration()/60, route.getDuration()%60);
            this.distance = route.getDistance();
            this.duration = route.getDuration();
            this.trafficType = "walking";

        } else if (route instanceof Bus) {
            this.title = String.format("%s → %s", route.getStart().getName(), route.getEnd().getName());
            this.description = String.format("저상버스를 이용하세요! <br> %d개 정류장, 시간: %d분", route.getStationCnt(), route.getDuration());
            this.distance = route.getDistance();
            this.duration = route.getDuration();
            this.stationCnt = route.getStationCnt();
            this.trafficType = "bus";

        } else {
            this.title = String.format("%s → %s", route.getStart().getName(), route.getEnd().getName());
            this.description = String.format("%s → %s : %d분, %d개역", route.getStart().getName(), route.getEnd().getName(), route.getDuration(), route.getStationCnt());
            this.distance = route.getDistance();
            this.duration = route.getDuration();
            this.stationCnt = route.getStationCnt();
            this.trafficType = "subway";
        }
    }

}
