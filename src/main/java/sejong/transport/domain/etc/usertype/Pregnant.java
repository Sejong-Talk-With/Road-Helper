package sejong.transport.domain.etc.usertype;

import lombok.*;
import sejong.transport.domain.etc.Times;
import sejong.transport.domain.etc.UserType;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pregnant extends UserType {
    private final String type = "임산부";
    private final double rate = 1.3;

    @Override
    public Times calRate(Times original) {
        int totalMin = original.getTotalMin();
        int result = (int) (totalMin * rate);
        return new Times(result);
    }
}
