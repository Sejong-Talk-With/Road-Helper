package sejong.transport.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationImg {

    @Id
    @GeneratedValue
    @Column(name = "station_img_id")
    private long id;
    private String name;
    private String url;
    private String stationName;
    private int exitNum;
    private Integer directionState;

    private static StationImg create(String name, String url, String stationName, int exitNum, Integer directionState) {
        StationImg stationImg = new StationImg();
        stationImg.name = name;
        stationImg.url = url;
        stationImg.stationName = stationName;
        stationImg.exitNum = exitNum;
        stationImg.directionState = directionState;

        return stationImg;
    }

}
