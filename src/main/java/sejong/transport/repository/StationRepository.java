package sejong.transport.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.transport.domain.entity.QStationImg;
import sejong.transport.domain.entity.Station;
import sejong.transport.domain.entity.StationImg;

import static sejong.transport.domain.entity.QStationImg.stationImg;

@Repository
@RequiredArgsConstructor
public class StationRepository {

    private final JPAQueryFactory qm;

    public StationImg findStation(String stationName,Integer directionState, int exitNum) {
        return qm.selectFrom(stationImg)
                .where(stationImg.stationName.eq(stationName), stationImg.directionState.eq(directionState)
                        , stationImg.exitNum.eq(exitNum))
                .fetchOne();
    }


}
