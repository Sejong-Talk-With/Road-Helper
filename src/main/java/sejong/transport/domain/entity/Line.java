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
public class Line {

    @Id
    @GeneratedValue
    @Column(name = "line_id")
    private long id;
    private int num;
    private String direction;
    private Integer directionState;

    public static Line create(int num, String direction, Integer directionState) {
        Line line = new Line();
        line.num = num;
        line.direction = direction;
        line.directionState = directionState;
        return line;
    }

}
