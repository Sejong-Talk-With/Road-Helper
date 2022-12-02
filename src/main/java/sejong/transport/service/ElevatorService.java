package sejong.transport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.transport.domain.entity.Elevator;
import sejong.transport.repository.ElevatorRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ElevatorService {

    private final ElevatorRepository elevatorRepository;

    public List<Elevator> findByStationName(String stationName) {
        return elevatorRepository.findByStationName(stationName);
    }



}
