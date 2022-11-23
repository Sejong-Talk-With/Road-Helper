package sejong.transport.domain.etc;

import lombok.*;

@Getter
@Setter
public class UserType {
    private final String type = "기본";
    private final double rate = 1;

    public Times calRate(Times original) {
        int totalMin = original.getTotalMin();
        int result = (int) (totalMin * rate);
        return new Times(result);
    }

}
