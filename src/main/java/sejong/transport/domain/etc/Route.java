package sejong.transport.domain.etc;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Route {

    // common
    public Point start;
    public Point end;
    public Long duration;

    // for walking
    public Long distance;

    // for subway
    public String line;
    public String direction;
    public String type;
    public String inExitNum;
    public String outExitNum;

    // for bus
    public String num;

    // for subway, bus
    public int stationCnt;
}