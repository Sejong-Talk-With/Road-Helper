package sejong.transport.domain.etc.traffictype;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;
import org.json.simple.JSONObject;
import sejong.transport.domain.etc.Point;
import sejong.transport.domain.etc.Route;
import sejong.transport.domain.etc.UserType;

import java.util.List;

@Getter
@Setter
public class Walking extends Route {

    public Walking(JSONObject json, UserType userType) {
        JSONObject walkPath = (JSONObject) json.get("walkpath");
        JSONObject summary = (JSONObject) walkPath.get("summary");
        List<JSONObject> points = (List<JSONObject>) json.get("points");
        JSONObject startPoint = points.get(0);
        JSONObject endPoint = points.get(points.size() - 1);

        List<JSONObject> ways = (List<JSONObject>) summary.get("ways");
        String startName = (String) ways.get(0).get("name");
        Boolean startCheckSubway = (Boolean) ways.get(0).get("multiEntrance");
        if (startCheckSubway) {
            startName += "역";
        }
        this.start = new Point(0L,startName,(Double) startPoint.get("x"), (Double) startPoint.get("y"));

        String endName = (String) ways.get(ways.size()-1).get("name");
        Boolean endCheckSubway = (Boolean) ways.get(ways.size()-1).get("multiEntrance");
        if (endCheckSubway) {
            endName += "역";
        }
        this.end = new Point(0L,endName,(Double) endPoint.get("x"), (Double) endPoint.get("y"));

        this.distance = (Long) json.get("distance");
        this.duration = Math.round((Long) json.get("duration") * userType.getRate());
    }

    public Walking(JSONObject json, Point start, Point end, UserType userType) {
        this.start = start;
        this.end = end;
        this.distance = (Long) json.get("distance");
        this.duration = Math.round((Long) json.get("duration") * userType.getRate());
    }
}
