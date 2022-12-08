package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString @Table(name = "profile")
@Getter @Setter @Builder @Entity
public class ProfileEntity extends BaseTimeEntity {
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int pno;
        @Column
	private String pintro; // 소개말
        @Column
	private String plike1; // 좋아요1
        @Column
	private String plike2; // 좋아요2
        @Column
	private String plike3; // 좋아요3
        @Column
	private String punlike1; // 싫어요1
        @Column
	private String punlike2; // 싫어요2
        @Column
	private String punlike3; // 싫어요3
        @Column
	private String phobby1; // 취미1
        @Column
	private String phobby2; // 취미2
        @Column
	private String phobby3; // 취미3

        @OneToOne @JoinColumn(name = "mno")
        MemberEntity memberEntity;
}
