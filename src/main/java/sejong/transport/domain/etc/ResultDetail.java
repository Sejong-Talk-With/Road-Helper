package sejong.transport.domain.etc;

import lombok.Getter;
import lombok.Setter;
import sejong.transport.domain.entity.Station;
import sejong.transport.domain.entity.StationImg;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResultDetail {

    public String start;
    public String end;
    public String title;
    public String imgSrc;
    public Long distance;
    public Long duration;
    public int stationCnt;

    public List<DetailElements> detailInfos = new ArrayList<>();

    public ResultDetail(Point start, Point end, Long distance, Long duration, List<DetailElements> infos) { // Walking
        this.title = start.getName() + " → " + end.getName();
        this.imgSrc = "/walking/"+this.title +".jpg";
        this.start = start.getName();
        this.end = end.getName();
        this.distance = distance;
        this.duration = duration;
        this.detailInfos = infos;
    }

    public ResultDetail() {

    }

}
