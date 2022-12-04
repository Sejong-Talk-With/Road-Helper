package sejong.transport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.transport.domain.entity.StationImg;
import sejong.transport.domain.entity.TransferImg;
import sejong.transport.domain.etc.StationDto;
import sejong.transport.domain.etc.TransferDto;
import sejong.transport.repository.StationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public StationDto getInfo(String name, String exitNum, String subwayType, String direction, String line) {
        Integer directionState = stationRepository.findDirectionState(Integer.parseInt(line), direction);
        StationImg stationInfo = stationRepository.findStationInfo(name, directionState, Integer.parseInt(exitNum));
        String stationRoute = stationRepository.findStationRoute(name);
        return new StationDto(name, line, subwayType, direction, stationRoute, stationInfo.getUrl(), exitNum);
    }

    public TransferDto getTInfo(String name, String lineFrom, String lineTo, String direction) {
        Integer directionState = stationRepository.findDirectionState(Integer.parseInt(lineTo), direction);
        TransferImg transferImg = stationRepository.findTransferImg(name, directionState, Integer.parseInt(lineFrom), Integer.parseInt(lineTo));
        String transferRoute = stationRepository.findTransferRoute(name);
        return new TransferDto(name, lineFrom, lineTo, direction, transferRoute, transferImg.getUrl());
    }
}
