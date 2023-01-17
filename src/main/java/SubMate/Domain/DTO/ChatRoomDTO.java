package SubMate.Domain.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
public class ChatRoomDTO {
    private int roomno;
    private String roomname;
    private int receiverno;
    private String receivername;
    private int senderno;
    private String sendername;
}