package sejong.transport.domain.etc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StationDto {

    public String name;
    public String title;
    public String imgSrc;
    public List<StationDtoDetail> details = new ArrayList<>();

    public StationDto(String name, String line, String subwayType, String direction, String stationRoute, String imgSrc, String exitNum) {
        this.name = name;
        this.title = String.format("%s 안내도 (%s, %s호선 %s행) ", name, subwayType, line , direction);
        this.imgSrc = imgSrc;
        String[] routes = stationRoute.split("-");
        if (subwayType.equals("승차")) {
            for (int i = 0; i < routes.length-1; i++) {
                String curr = routes[i];
                String next = routes[i+1];
                if (i == 0) {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail(exitNum + "번 출구 엘리베이터 탑승 후 " + next + "에서 하차");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                } else if (i == routes.length - 2) {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail(curr + " 엘리베이터 탑승 후 " + next + "에서 하차, 지하철 탑승");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                } else {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail(curr + " 엘리베이터 탑승 후 " + next + "에서 하차");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                }


            }
        } else if (subwayType.equals("하차")) {
            for (int i = routes.length - 1; i > 0; i--) {
                String curr = routes[i];
                String next = routes[i - 1];
                if (i == routes.length - 1) {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail("지하철 하차, " + curr + "엘리베이터 탑승 후 " + next + "로 이동");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                } else if (i == 1) {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail(curr + "에서 " + exitNum + "번 출구 엘리베이터를 통해 "+ next + "으로 이동");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                } else {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail(curr + " 엘리베이터 탑승 후 " + next + "에서 하차");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                }
            }
        }
    }
}
