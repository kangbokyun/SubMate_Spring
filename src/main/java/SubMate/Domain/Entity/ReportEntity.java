package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "report")
public class ReportEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int reportno; // PK
        @Column
        private int reportmno; // 신고자
        @Column // 1 => 유저 2 => 게시글
        private int reportkind; // 게시글, 유저 등의 구분
        @Column
        private int reportbno;
        @Column
        private int reportrno;
        @Column
        private int reportuserno;
        @Column
        private int reportcheck;
        @Column
        private int reportclickvalue;
        @Column // 1 => 음란매체 2 => 사진도용 3 => 명예훼손 4 => 기타
	private String reportvalue;
        @Column
	private String reportcontents;

        @ManyToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
