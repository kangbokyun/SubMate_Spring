package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "chatcall")
public class ChatCallEntity extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int callno;
	@Column
	private String callsenderno;
	@Column
	private String callreceiverno;
}
