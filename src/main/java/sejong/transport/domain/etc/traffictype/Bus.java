package sejong.transport.domain.etc.traffictype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import sejong.transport.domain.etc.Point;
import sejong.transport.domain.etc.Route;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Bus extends Route {

    public Bus(JSONObject json) {
        List<JSONObject> stations = (List<JSONObject>) json.get("stations");
        List<JSONObject> points = (List<JSONObject>) json.get("points");
        JSONObject startPoint = points.get(0);
        JSONObject endPoint = points.get(points.size() - 1);
        Long startId = (Long) stations.get(0).get("id");
        String startName = (String) stations.get(0).get("displayName");
        String endName =  (String) stations.get(stations.size()-1).get("displayName");
        Long endId = (Long) stations.get(stations.size()-1).get("id");
        this.start = new Point(startId,startName,(Double) startPoint.get("x"), (Double) startPoint.get("y"));
        this.end = new Point(endId,endName,(Double) endPoint.get("x"), (Double) endPoint.get("y"));
        this.stationCnt = stations.size();
        this.duration = (Long) json.get("duration");

        List<JSONObject> routes = (List<JSONObject>) json.get("routes");
        JSONObject firstRoute = routes.get(0);

        this.num= (String) firstRoute.get("longName");
    }
}
