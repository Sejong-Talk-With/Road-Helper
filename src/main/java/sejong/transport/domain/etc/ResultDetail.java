package sejong.transport.domain.etc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResultDetail {

    public String start;
    public String end;
    public String imgSrc;
    public Long distance;
    public Long duration;

    public List<DetailElements> detailInfos = new ArrayList<>();

    public ResultDetail(Point start, Point end, Long distance, Long duration, List<DetailElements> infos) {
        this.start = start.getName();
        this.end = end.getName();
        this.distance = distance;
        this.duration = duration;
        this.detailInfos = infos;
    }

}
