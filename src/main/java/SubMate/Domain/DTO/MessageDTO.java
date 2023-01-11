package SubMate.Domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private String status;
}
