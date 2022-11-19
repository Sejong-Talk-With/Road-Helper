package sejong.transport.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer {

    @Id
    @GeneratedValue
    @Column(name = "transfer_id")
    private long id;
    private String name;
    private String route;

    public static Transfer create(String name, String route) {
        Transfer transfer = new Transfer();
        transfer.name = name;
        transfer.route = route;
        return transfer;
    }
}
