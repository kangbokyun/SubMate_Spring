package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "ranking")
@Data @ToString @Builder
public class RankEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int rankerno; // PK
        @Column
        private String rankerline; // 랭커 호선
        @Column
        private String rankingno; // 랭커 순위
        @Column
        private String rankernickname; // 랭커 닉네임
        @Column
        private int heartcount;;

        @OneToOne @JoinColumn(name = "mno")
        private MemberEntity memberEntity;
}
