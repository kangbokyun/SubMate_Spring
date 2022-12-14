package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "rank")
@Getter @Setter @ToString @Builder
public class RankEntity extends BaseTimeEntity {
        private int rankno;
        private String rankline;
        private String rankerno;
        private String rankernickname;

        @OneToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
