package sejong.transport.domain.etc;

import lombok.Data;
import sejong.transport.domain.etc.usertype.Elder;
import sejong.transport.domain.etc.usertype.Pregnant;
import sejong.transport.domain.etc.usertype.Wheel;

import static sejong.transport.domain.etc.UserTypeConst.pregnant;
import static sejong.transport.domain.etc.UserTypeConst.elder;
import static sejong.transport.domain.etc.UserTypeConst.wheel;

@Data
public class SearchForm {

    public String start;
    public String end;
    public String type;
    public UserType userType;

    public void setType(String type) {
        this.type = type;
        this.userType = switch (type) {
            case pregnant-> new Pregnant();
            case elder -> new Elder();
            case wheel -> new Wheel();
            default -> new UserType();
        };
    }
}
