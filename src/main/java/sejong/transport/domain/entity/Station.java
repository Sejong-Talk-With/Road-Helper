package sejong.transport.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station {

    @Id
    @GeneratedValue
    @Column(name = "station_id")
    private long id;
    private String name;
    private String route;

    public static Station create(String name, String route) {
        Station station = new Station();
        station.name = name;
        station.route = route;
        return station;
    }
}
