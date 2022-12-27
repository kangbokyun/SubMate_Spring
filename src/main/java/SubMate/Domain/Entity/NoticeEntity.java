package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "notice")
public class NoticeEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int nno;
        @Column
        private String ntitle;
        @Column
        private String ncontents;
        @Column
        private String nfile;
        @Column
        private int nkind;

        @ManyToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
