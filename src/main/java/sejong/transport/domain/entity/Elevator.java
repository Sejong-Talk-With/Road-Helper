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
public class Elevator {

    @Id
    @GeneratedValue
    @Column(name = "elevator_id")
    private long id;
    private String stationName;
    private int exitNum;
    private double latitude;
    private double longitude;

    public static Elevator create(String stationName, int exitNum, double latitude, double longitude) {
        Elevator elevator = new Elevator();
        elevator.stationName = stationName;
        elevator.exitNum = exitNum;
        elevator.latitude = latitude;
        elevator.longitude = longitude;
        return elevator;
    }
}
