package sejong.transport.domain.etc.traffictype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import sejong.transport.domain.etc.Route;

@Getter
@Setter
@AllArgsConstructor
public class Subway extends Route {
    public String start;
    public String end;
    public int line;
    public String direction;

    public Subway(JSONObject json) {

    }
}
