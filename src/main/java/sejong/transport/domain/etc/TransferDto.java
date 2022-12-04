package sejong.transport.domain.etc;

import java.util.ArrayList;
import java.util.List;

public class TransferDto {
    public String name;
    public String title;
    public String imgSrc;
    public List<StationDtoDetail> details = new ArrayList<>();

    public TransferDto(String name, String lineFrom, String lineTo, String direction, String stationRoute, String imgSrc) {
        this.name = name;
        this.title = String.format("%s 안내도 (환승, %s→%s호선 %s행) ", name, lineFrom, lineTo , direction);
        this.imgSrc = imgSrc;
        String[] routes = stationRoute.split("-");
            for (int i = 0; i < routes.length-1; i++) {
                String curr = routes[i];
                String next = routes[i + 1];
                if (i == 0) {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail("지하철 하차, "+lineFrom + "호선 " + curr+ "엘리베이터 탑승 후 <br>" + next + "에서 하차");
                    stationDtoDetail.setTitle(curr + " → " + next);
                    details.add(stationDtoDetail);
                } else if (i == routes.length - 2) {
                    StationDtoDetail stationDtoDetail = new StationDtoDetail();
                    stationDtoDetail.setDetail(curr + " 엘리베이터 탑승 후 " + lineTo + "호선 " + next + "에서 하차, 지하철 탑승");
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
