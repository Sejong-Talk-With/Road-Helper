package sejong.transport.domain.etc.traffictype;

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
public class Subway extends Route {

    public Subway(JSONObject json) {
        List<JSONObject> stations = (List<JSONObject>) json.get("stations");
        Long startId = (Long) stations.get(0).get("id");
        String startName = (String) stations.get(0).get("displayName");
        Long endId = (Long) stations.get(stations.size() - 1).get("id");
        String endName = (String) stations.get(stations.size() - 1).get("displayName");
        List<JSONObject> points = (List<JSONObject>) json.get("points");
        JSONObject startPoint = points.get(0);
        JSONObject endPoint = points.get(points.size() - 1);
        this.start = new Point(startId, startName, (Double) startPoint.get("x"), (Double) startPoint.get("y"));
        this.end = new Point(endId, endName, (Double) endPoint.get("x"), (Double) endPoint.get("y"));
        this.duration = (Long) json.get("duration");
        this.stationCnt = stations.size();

        List<JSONObject> routes = (List<JSONObject>) json.get("routes");
        JSONObject firstRoute = routes.get(0);
        String before = (String) firstRoute.get("longName");
        Pattern p = Pattern.compile("\\(.+?\\)");
        Matcher matcher = p.matcher(before);
        String temp = (String) firstRoute.get("name");
        this.line = temp.substring(0,1);
        if (matcher.find()) {    // 정규식과 매칭되는 값이 있으면
            this.direction = matcher.group().replaceAll("[\\(\\)]", "");        // 특정 단어 사이의 값을 추출한다
        }

    }

    public Subway(Subway before, Subway after) {
        this.start = new Point(before.getEnd());
        this.end = new Point(after.getStart());
        this.direction = String.format("%s → %s", before.getLine(), after.getLine());
        this.type = "환승";
    }
}
