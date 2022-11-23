package sejong.transport.domain.etc;

import lombok.*;
import sejong.transport.domain.etc.Times;

@Getter
@Setter
public class UserType {
    private final String type = "유형을 선택해주세요";
    private final double rate = 1;

    public Times calRate(Times original) {
        int totalMin = original.getTotalMin();
        int result = (int) (totalMin * rate);
        return new Times(result);
    }

}
