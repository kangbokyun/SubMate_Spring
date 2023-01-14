package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class RoomDTO {
	private int roomno;
	// (receiverno * senderno) + receiverno - senderno
	private String roomid;
	private String receiverno;
	private String senderno;
}
