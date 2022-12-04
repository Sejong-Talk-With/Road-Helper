package sejong.transport.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.transport.domain.entity.Elevator;

import java.util.List;

import static sejong.transport.domain.entity.QElevator.elevator;

@Repository
@RequiredArgsConstructor
public class ElevatorRepository {

    private final JPAQueryFactory qm;

    public List<Elevator> findByStationName(String stationName) {
        return qm.selectFrom(elevator)
                .where(elevator.stationName.eq(stationName))
                .fetch();
    }
}
