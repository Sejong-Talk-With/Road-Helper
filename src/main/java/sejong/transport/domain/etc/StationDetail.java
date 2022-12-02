package sejong.transport.domain.etc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StationDetail {
    public String name;
    public String title;
    public String description;
    public String line;
    public String subwayType; // type (승차, 하차 or transfer)
    public String direction; // direction (to go)

    public int stationCnt;
    public Long duration;

    public StationDetail(Route route, boolean check) {
        if (check) {
            this.name = route.getStart().getName();
            this.subwayType = "승차";
            this.direction = route.getDirection().split("-")[1] + "행";
            this.description = "안내도를 따라 <br> <b>승강장</b>까지 엘리베이터를 이용하세요!";
        } else {
            this.name = route.getEnd().getName();
            this.subwayType = "환승";
            this.direction = route.getDirection().split("-")[1] + "행";
            this.description = "안내도를 따라 <br> <b>환승</b>까지 엘리베이터를 이용하세요!";
        }
        this.line = route.getLine().substring(0,1);
        this.title = String.format("%s 안내도 (%s, %s호선 %s) ", this.name, this.subwayType, this.line , this.direction);
    }
}
