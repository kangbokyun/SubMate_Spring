package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class ChatCallDTO {
	private int callno;
	private String callsenderno;
	private String callreceiverno;
}
