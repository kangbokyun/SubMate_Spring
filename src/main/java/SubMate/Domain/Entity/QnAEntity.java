package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "qna")
public class QnAEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int qnano;
        private String qnatitle;
        private String qnacontents;
        // QnA_Status : 1 => 담당자열람 | 2 => 답변 중 | 3 => 답변완료
        private String qnastatus;

        @ManyToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
