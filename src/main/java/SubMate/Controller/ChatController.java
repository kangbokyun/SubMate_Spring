package SubMate.Controller;

import SubMate.Domain.DTO.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class ChatController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	// /app/message 어노테이션에 발행하는 경로를 @SendTo( 1 : n )와 @SendToUser( 1 : 1 ) 어노테이션에 구독 경로를 작성
	// 예를 들어, 특정 사용자가 message라는 경로로 메세지를 보내면 /room/public 이라는 토픽을 구독하는 사용자에게 모두 메세지를 뿌린다.
	@MessageMapping("/message") @SendTo("/room/public")
	public MessageDTO receivePublicMessage(@Payload MessageDTO messageDTO) {
		System.out.println("PublicMessageDTO : " + messageDTO);
		return messageDTO;
	}

	@MessageMapping("/private-message")
	public MessageDTO receivePrivateMessage(@Payload MessageDTO messageDTO) {
		System.out.println("PrivateMessageDTO : " + messageDTO);
		System.out.println("messageDTO.getReceiverName() : " + messageDTO.getReceiverName());
		// 연결된 클라에게 문자를 보낼 때 사용하는 방법
		// 브로커를 설정하지 않은 경우 simpMessagingTemplate을 주입받아 사용
		// /private을 구독하는 클라(들)에게 messageDTO를 받아 전송
		simpMessagingTemplate.convertAndSendToUser(messageDTO.getSenderName(), "/private", messageDTO);
		return messageDTO;
	}
}
