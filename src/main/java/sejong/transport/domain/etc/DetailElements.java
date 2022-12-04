package sejong.transport.domain.etc;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class DetailElements {

    public String title;
    public Long distance;
    public Long duration;
    public String description;

    public DetailElements(JSONObject json, UserType userType, int i) {
        this.title = (String) json.get("turnDesc");
        if (i == 0) {
            this.title += " 출발";
        } else if (i == -1) {
            this.title += " 도착";
        }
        this.distance = (Long) json.get("distance");
        this.duration = Math.round((Long) json.get("duration") * userType.getRate());
    }

    public DetailElements(String start, String end, int i) {
        if (i == 0) {
            this.title = start + "번 출구 근처 엘리베이터 탑승";
            this.description = "지상 → " + end + " 이동";
        } else if (i == -1) {
            this.title = end + "층 승강장 탑승";
            this.description = "교통약자 전용 탑승장 탑승";
        } else {
            this.title = start + "층 엘리베이터 탑승";
            this.description = start + " → " + end + " 이동";
        }
    }

    public DetailElements(String start, String end, int i, int j) {
        if (i == -1) {
            this.title = start + "번 출구 근처 엘리베이터 탑승";
            this.description = end + " → " + "지상 이동";
        } else {
            this.title = end + "층 엘리베이터 탑승";
            this.description = end + " → " + start + " 이동";
        }
    }

    public DetailElements() {

    }
}
