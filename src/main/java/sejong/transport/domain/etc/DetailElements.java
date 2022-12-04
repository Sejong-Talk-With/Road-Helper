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
    public double longitude;
    public double latitude;

    public DetailElements(JSONObject json, UserType userType, int i) {
        this.title = (String) json.get("turnDesc");
        if (i == 0) {
            this.title += " 출발";
        } else if (i == -1) {
            this.title += " 도착";
        }
        this.distance = (Long) json.get("distance");
        this.duration = Math.round((Long) json.get("duration") * userType.getRate());
        this.longitude = (Double) json.get("lng");
        this.latitude = (Double) json.get("lat");
    }

    public DetailElements() {

    }
}
