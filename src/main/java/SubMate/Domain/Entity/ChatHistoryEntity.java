package SubMate.Domain.Entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "chathistory")
public class ChatHistoryEntity extends BaseTimeEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int chno;
	private String chsendername;
	private String chsenderno;
	private String chsgender;
	private String chroomname;
}
