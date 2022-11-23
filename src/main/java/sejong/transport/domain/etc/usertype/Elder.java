package sejong.transport.domain.etc.usertype;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sejong.transport.domain.etc.Times;
import sejong.transport.domain.etc.UserType;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Elder extends UserType {
    private final String type = "노약자";
    private final double rate = 1.6;

    @Override
    public Times calRate(Times original) {
        int totalMin = original.getTotalMin();
        int result = (int) (totalMin * rate);
        return new Times(result);
    }
}
