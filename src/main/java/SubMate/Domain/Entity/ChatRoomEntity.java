package SubMate.Domain.Entity;

import jdk.jfr.Enabled;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data @ToString @Builder
@Entity @Table(name = "chatroom")
public class ChatRoomEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomno;
    @Column
    private String roomname;
    @Column
    private int receiverno;
    private String receivername;
    @Column
    private int senderno;
    @Column
    private String sendername;
    @Column
    private String sgender;
    @Column
    private String rgender;
}
