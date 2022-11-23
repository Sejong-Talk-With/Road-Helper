package sejong.transport.domain.etc.usertype;

import org.junit.jupiter.api.Test;
import sejong.transport.domain.etc.Times;
import sejong.transport.domain.etc.UserType;

import static org.assertj.core.api.Assertions.assertThat;

class UserTypeTest {

    @Test
    public void typeTest() throws Exception{
        //given (주어진 것들을 통해)
        UserType pregnant = new Pregnant();
        UserType elder = new Elder();
        UserType wheel = new Wheel();
        //when (이런 기능을 동작했을 때)
        Times pResult = pregnant.calRate(new Times(20));
        Times eResult = elder.calRate(new Times(20));
        Times wResult = wheel.calRate(new Times(20));
        //then (이런 결과를 확인할 것)
        assertThat(pResult.getTotalMin()).isEqualTo(26);
        assertThat(eResult.getTotalMin()).isEqualTo(32);
        assertThat(wResult.getTotalMin()).isEqualTo(36);
    }

}