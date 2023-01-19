package SubMate.Domain.DTO;

import lombok.*;

import javax.persistence.OneToMany;

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
    private String sgender;
    private String rgender;
    private String chlastmessage;
    private String createdate;
    private String simg;
    private String rimg;
}
