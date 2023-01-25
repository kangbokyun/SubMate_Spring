package SubMate.Controller;

import SubMate.Domain.DTO.ChatCallDTO;
import SubMate.Domain.DTO.ChatHistoryDTO;
import SubMate.Domain.DTO.ChatRoomDTO;
import SubMate.Domain.DTO.MessageDTO;
import SubMate.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.List;

@RestController
public class ChatController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	ChatService chatService;

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
		chatService.ChatHistorySave(messageDTO);
		simpMessagingTemplate.convertAndSendToUser(messageDTO.getReceiverName(), "/private", messageDTO);
		return messageDTO;
	}

	@PostMapping("/ChatCall")
	public ResponseEntity<?> ChatCall(@RequestParam("receivermno") int receivermno, @RequestParam("sendermno") int sendermno) {
		ChatCallDTO chatCallDTO = ChatCallDTO.builder().callreceiverno(Integer.toString(receivermno)).callsenderno(Integer.toString(sendermno)).build();
		boolean result = chatService.ChatCall(chatCallDTO);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/CallList")
	public ResponseEntity<?> CallList(@RequestParam("mno") int mno) {
		System.out.println("/CallList/mno : " + mno);
		List<ChatCallDTO> chatCallDTOS = chatService.CallList(mno);
		return ResponseEntity.ok().body(chatCallDTOS);
	}

	@PostMapping("/CreateChatRoom")
	public ResponseEntity<?> CreateChatRoom(@RequestParam("receiverno") int receiverno, @RequestParam("receivername") String receivername, @RequestParam("senderno") int senderno, @RequestParam("sendername") String sendername) {
		System.out.println("receiverno : " + receiverno);
		ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder().receivername(receivername).receiverno(receiverno).sendername(sendername).senderno(senderno).build();
		boolean result = chatService.CreateChatRoom(chatRoomDTO);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/ChatRoom")
	public ResponseEntity<?> ChatRoom(@RequestParam("receiverno") int receiverno, @RequestParam("receivername") String receivername, @RequestParam("senderno") int senderno, @RequestParam("sendername") String sendername) {
		System.out.println("try@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		ChatRoomDTO temp = ChatRoomDTO.builder().receivername(receivername).receiverno(receiverno).sendername(sendername).senderno(senderno).build();
		ChatRoomDTO roomDTO = chatService.ChatRoom(temp);
		return ResponseEntity.ok().body(roomDTO);
	}

	@PostMapping("/ChatRoomList")
	public ResponseEntity<?> ChatRoomList(@RequestParam("mno") String mno) {
		System.out.println("ChatRoomList Init");
		List<ChatRoomDTO> chatRoomDTOS = chatService.ChatRoomList(Integer.parseInt(mno));
		return ResponseEntity.ok().body(chatRoomDTOS);
	}

	@PostMapping("/ChatHistoryList")
	public ResponseEntity<?> ChatHistoryList(@RequestParam("roomname") String roomname) {
		System.out.println("ChatHistoryList Init" + roomname);
		List<ChatHistoryDTO> chatHistoryDTOS = chatService.ChatHistoryList(roomname);
		return ResponseEntity.ok().body(chatHistoryDTOS);
	}
}
