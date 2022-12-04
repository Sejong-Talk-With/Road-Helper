package sejong.transport.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.transport.domain.entity.*;

import static sejong.transport.domain.entity.QLine.line;
import static sejong.transport.domain.entity.QStation.station;
import static sejong.transport.domain.entity.QStationImg.stationImg;
import static sejong.transport.domain.entity.QTransfer.transfer;
import static sejong.transport.domain.entity.QTransferImg.transferImg;

@Repository
@RequiredArgsConstructor
public class StationRepository {

    private final JPAQueryFactory qm;

    public Integer findDirectionState(int num, String direction) {
        return qm.select(line.directionState)
                .from(line)
                .where(line.direction.eq(direction), line.num.eq(num))
                .fetchOne();
    }

    public StationImg findStationInfo(String stationName,Integer directionState, int exitNum) {
        return qm.selectFrom(stationImg)
                .where(stationImg.stationName.eq(stationName), stationImg.directionState.eq(directionState)
                        , stationImg.exitNum.eq(exitNum))
                .fetchOne();
    }

    public String findStationRoute(String stationName) {
        return qm.select(station.route)
                .from(station)
                .where(station.name.eq(stationName))
                .fetchOne();
    }

    public TransferImg findTransferImg(String stationName, Integer directionState, int lineFrom, int lineTo) {
        return qm.selectFrom(transferImg)
                .where(transferImg.stationName.eq(stationName), transferImg.directionState.eq(directionState)
                        , transferImg.lineFrom.eq(lineFrom), transferImg.lineTo.eq(lineTo))
                .fetchOne();
    }

    public String findTransferRoute(String stationName) {
        return qm.select(transfer.route)
                .from(transfer)
                .where(transfer.name.eq(stationName))
                .fetchOne();
    }


}
