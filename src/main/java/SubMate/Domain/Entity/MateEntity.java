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
	private String mategwst; // 출근 시작 시간
	@Column
	private String mategwet; // 출근 도착 시간
	@Column
	private String matelwst; // 퇴근 시작 시간
	@Column
	private String matelwet; // 퇴근 도착 시간
	@Column
	private String matetline; // 노선(초지, 고잔, 중앙, ..., 강남)
	@Column
	private String matestartstation; // 출발 호선
	@Column
	private String mateendstation; // 도착 호선
	@Column
        private String matestartstationname; // 출발 역
	@Column
        private String mateendstationname; // 도착 역

	@OneToOne @JoinColumn(name = "mno")
	private MemberEntity memberEntity;
}
