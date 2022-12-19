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
        private String rankerprofileimg; // 랭커 프로필사진
        @Column
        private String rankerager; // 랭커 나이대
        @Column
        private String rankerhobby; // 랭커 취미
        @Column
        private String rankermbti; // 랭커 성향
        @Column
        private int heartcount;; // 랭커 하트 수

        @OneToOne @JoinColumn(name = "mno")
        private MemberEntity memberEntity;
}
