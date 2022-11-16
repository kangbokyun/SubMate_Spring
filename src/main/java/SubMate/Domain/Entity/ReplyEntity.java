package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "reply")
public class ReplyEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int rno;
        @Column
        private String rcontents;
        @Column
        private String rwriter;

        @ManyToOne @JoinColumn(name = "mno")
        private MemberEntity memberReplyEntity;

        @ManyToOne @JoinColumn(name = "bno")
        private BoardEntity boardReplyEntity;
}
