package sejong.transport.domain.etc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
public class Point {
    public Long id;
    public String name;
    public String encodedName;
    public Double longitude;
    public Double latitude;

    public Point(Long id, String name, Double longitude, Double latitude) {
        this.id = id;
        this.name = name;
        this.encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Point(Point toCopy) {
        this.id = toCopy.getId();
        this.name = toCopy.getName();
        this.encodedName = toCopy.getEncodedName();
        this.longitude = toCopy.getLongitude();
        this.latitude = toCopy.getLatitude();
    }
}
