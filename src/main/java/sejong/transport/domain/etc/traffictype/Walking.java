package sejong.transport.domain.etc.traffictype;

import lombok.Data;
import org.json.simple.JSONObject;
import sejong.transport.domain.etc.Route;

@Data
public class Walking extends Route {
    public String start;
    public String end;

    public Walking(JSONObject json) {

    }
}
