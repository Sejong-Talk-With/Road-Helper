package sejong.transport.domain.etc;

import lombok.Data;

@Data
public class Times {

    public int hour;
    public int min;

    public int getTotalMin() {
        return hour * 60 + min;
    }

    public Times(int totalMin) {
        this.hour = totalMin / 60;
        this.min = totalMin % 60;
    }
}
