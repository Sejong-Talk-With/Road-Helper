package sejong.transport.domain.etc.traffictype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sejong.transport.domain.etc.Route;

@Getter
@Setter
@AllArgsConstructor
public class Bus extends Route {
    public String start;
    public String end;
    public String num;
}
