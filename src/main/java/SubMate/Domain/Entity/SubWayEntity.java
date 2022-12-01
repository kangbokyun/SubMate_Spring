package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "subway")
public class SubWayEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int sno;
        @Column
        private String sline;
        @Column
        private String sname;
        @Column
        private String scode;
        @Column
        private String slng;
        @Column
        private String slat;
}
