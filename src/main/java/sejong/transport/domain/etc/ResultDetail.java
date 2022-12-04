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
        this.imgSrc = "/stations/station/KakaoTalk_20221121_014443950.gif";
        this.start = start.getName();
        this.end = end.getName();
        this.distance = distance;
        this.duration = duration;
        this.detailInfos = infos;
    }

    public ResultDetail(String stationName, String subwayType, String direction, List<DetailElements> infos) {
        this.title = stationName + String.format(" 안내도(%s, %s)",subwayType,direction);
        this.imgSrc = '/'+stationName+"-"+subwayType;
        this.detailInfos = infos;
    }

/*    public ResultDetail(StationImg station, String subwayType, String direction, List<DetailElements> infos) { // Subway
        this.title = station.getStationName() + String.format(" 안내도(%s, %s)",subwayType,direction);
        this.imgSrc = station.getUrl();
        this.detailInfos = infos;
   }*/

    public ResultDetail() {

    }

}
