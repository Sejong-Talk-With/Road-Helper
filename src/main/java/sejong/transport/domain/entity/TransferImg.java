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
public class TransferImg {

    @Id
    @GeneratedValue
    @Column(name = "transfer_img_id")
    private long id;
    private String name;
    private String url;
    private String stationName;
    private int lineFrom;
    private int lineTo;
    private Integer directionState;

    public static TransferImg create(String name, String url, String stationName, int lineFrom, int lineTo, Integer directionState) {
        TransferImg transferImg = new TransferImg();
        transferImg.name = name;
        transferImg.url = url;
        transferImg.stationName = stationName;
        transferImg.lineFrom = lineFrom;
        transferImg.lineTo = lineTo;
        transferImg.directionState = directionState;

        return transferImg;
    }
}
