package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString @Builder @Data
@Entity@Table(name = "mate")
public class MateEntity extends BaseTimeEntity{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mateno;
	@Column
	private String mategwst;
	@Column
	private String mategwet;
	@Column
	private String matelwst;
	@Column
	private String matelwet;
	@Column
	private String matetline;
	@Column
	private String matestartstaion;
	@Column
	private String mateendstation;
	@Column
        private String matestartstaionname;
	@Column
        private String mateendstaionname;

	@OneToOne @JoinColumn(name = "mno")
	private MemberEntity memberEntity;
}
