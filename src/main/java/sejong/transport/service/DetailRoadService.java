package sejong.transport.service;

import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import sejong.transport.domain.etc.*;
import sejong.transport.domain.etc.traffictype.Walking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetailRoadService {

    public ResultDetail findWalkingDetail(Point start, Point end, UserType userType) throws IOException, ParseException {
        String src = String.format("https://map.naver.com/v5/api/dir/findwalk?lo=ko&st=1&o=all&l=%s,%s,%s,%s;%s,%s,%s,%s&lang=ko",
                start.longitude, start.latitude, start.encodedName, start.id,
                end.longitude, end.latitude, end.encodedName, end.id
        );
        URL url = new URL(src);
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        JSONParser jsonParser = new JSONParser();
        String result = bf.readLine();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        List<JSONObject> routes = (List<JSONObject>) jsonObject.get("routes");
        JSONObject route = routes.get(0);
        JSONObject summary = (JSONObject) route.get("summary");
        List<JSONObject> legs = (List<JSONObject>) route.get("legs");
        JSONObject leg = legs.get(0);
        List<JSONObject> steps = (List<JSONObject>) leg.get("steps");
        List<DetailElements> detailElementsList = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            if (i == 0) {
                detailElementsList.add(new DetailElements(steps.get(i), userType, 0));
            } else if (i == steps.size() - 1) {
                detailElementsList.add(new DetailElements(steps.get(i), userType, -1));

            } else {
                detailElementsList.add(new DetailElements(steps.get(i), userType, 1));
            }
        }
        return new ResultDetail(start, end, (Long) summary.get("distance"), Math.round((Long) summary.get("duration") * userType.getRate()), detailElementsList);
    }
}
