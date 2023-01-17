package SubMate.Service;

import SubMate.Domain.DTO.ChatCallDTO;
import SubMate.Domain.DTO.ChatRoomDTO;
import SubMate.Domain.Entity.ChatCallEntity;
import SubMate.Domain.Entity.ChatRoomEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.ChatCallRepository;
import SubMate.Domain.Repository.ChatRoomRepository;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
	@Autowired
	ChatCallRepository chatCallRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ChatRoomRepository chatRoomRepository;

	public boolean ChatCall(ChatCallDTO chatCallDTO) {
		List<ChatCallEntity> chatCallEntities = chatCallRepository.findAll();
		if(chatCallDTO != null) {
			for(ChatCallEntity chatCallEntity : chatCallEntities) {
				if(!(chatCallEntity.getCallreceiverno() == chatCallDTO.getCallreceiverno() && chatCallEntity.getCallsenderno() == chatCallDTO.getCallsenderno())) {
					ChatCallEntity chatCallEntity2 = ChatCallEntity.builder()
						.callreceiverno(chatCallDTO.getCallreceiverno()).callsenderno(chatCallDTO.getCallsenderno())
						.build();
					chatCallRepository.save(chatCallEntity2);
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public List<ChatCallDTO> CallList(int mno) {
		List<ChatCallEntity> chatCallEntities = chatCallRepository.findAll();
		List<ChatCallDTO> chatCallDTOS = new ArrayList<>();
		for(ChatCallEntity chatCallEntity : chatCallEntities) {
			if(Integer.parseInt(chatCallEntity.getCallreceiverno()) == mno) {
				MemberEntity rMemberEntity = memberRepository.findById(Integer.parseInt(chatCallEntity.getCallreceiverno())).get();
				MemberEntity sMemberEntity = memberRepository.findById(Integer.parseInt(chatCallEntity.getCallsenderno())).get();
				ChatCallDTO chatCallDTO = new ChatCallDTO();
				chatCallDTO.setCallno(chatCallEntity.getCallno());
				chatCallDTO.setCallreceiverno(chatCallEntity.getCallreceiverno());
				chatCallDTO.setCallsenderno(chatCallEntity.getCallsenderno());
				chatCallDTO.setReceivername(rMemberEntity.getMnickname());
				chatCallDTO.setSendername(sMemberEntity.getMnickname());
				chatCallDTOS.add(chatCallDTO);
			}
		}
		return chatCallDTOS;
	}

	public boolean CreateChatRoom(ChatRoomDTO chatRoomDTO) {
		if(chatRoomDTO != null) {
			List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
			for(ChatRoomEntity chatRoomEntity : chatRoomEntities) {
				if(chatRoomEntity.getReceivername().equals(chatRoomEntity.getReceivername()) && chatRoomEntity.getReceiverno() == chatRoomDTO.getReceiverno() && chatRoomEntity.getSenderno() == chatRoomDTO.getSenderno() && chatRoomEntity.getSendername().equals(chatRoomDTO.getSendername())) {
					chatRoomRepository.delete(chatRoomEntity);
				}
			}
			MemberEntity memberEntity1 = memberRepository.findById(chatRoomDTO.getSenderno()).get();
			MemberEntity memberEntity2 = memberRepository.findById(chatRoomDTO.getReceiverno()).get();
			ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
				.receiverno(chatRoomDTO.getReceiverno())
				.senderno(chatRoomDTO.getSenderno())
				.sendername(chatRoomDTO.getSendername())
				.receivername(chatRoomDTO.getReceivername())
				.sgender(memberEntity1.getMgender())
				.rgender(memberEntity2.getMgender())
				.build();
			UUID uuid = UUID.randomUUID();
			String roomname = uuid.toString().replace("_", "-") + (chatRoomDTO.getSenderno() * chatRoomDTO.getReceiverno() + chatRoomDTO.getReceiverno());
			chatRoomEntity.setRoomname(roomname);
			chatRoomRepository.save(chatRoomEntity);
			return true;
		} else {
			return false;
		}
	}

	public ChatRoomDTO ChatRoom(ChatRoomDTO chatRoomDTO) {
		List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
		ChatRoomDTO roomDTO = new ChatRoomDTO();
		if(chatRoomDTO != null) {
			for(ChatRoomEntity chatRoomEntity : chatRoomEntities) {
				if(chatRoomEntity.getSenderno() == chatRoomDTO.getSenderno() && chatRoomEntity.getSendername().equals(chatRoomDTO.getSendername()) && chatRoomEntity.getReceiverno() == chatRoomDTO.getReceiverno() && chatRoomEntity.getReceivername().equals(chatRoomDTO.getReceivername())) {
					MemberEntity memberEntity1 = memberRepository.findById(chatRoomDTO.getReceiverno()).get();
					MemberEntity memberEntity2 = memberRepository.findById(chatRoomDTO.getSenderno()).get();
					System.out.println("chatRoomEntity : " + chatRoomEntity);
					roomDTO.setReceivername(chatRoomEntity.getReceivername());
					roomDTO.setReceiverno(chatRoomEntity.getReceiverno());
					roomDTO.setSenderno(chatRoomEntity.getSenderno());
					roomDTO.setRoomno(chatRoomEntity.getRoomno());
					roomDTO.setSendername(chatRoomEntity.getSendername());
					roomDTO.setRoomname(chatRoomEntity.getRoomname());
					roomDTO.setRgender(memberEntity1.getMgender());
					roomDTO.setSgender(memberEntity2.getMgender());
				}
			}
		}
		return roomDTO;
	}

	public List<ChatRoomDTO> ChatRoomList() {
		List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
		List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
		for(ChatRoomEntity chatRoomEntity : chatRoomEntities) {
			ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
					.senderno(chatRoomEntity.getSenderno())
					.roomno(chatRoomEntity.getRoomno())
					.sendername(chatRoomEntity.getSendername())
					.receiverno(chatRoomEntity.getReceiverno())
					.receivername(chatRoomEntity.getReceivername())
					.roomname(chatRoomEntity.getRoomname())
					.rgender(chatRoomEntity.getRgender())
					.sgender(chatRoomEntity.getSgender())
					.build();
			chatRoomDTOS.add(chatRoomDTO);
		}
		return chatRoomDTOS;
	}

	public boolean ChatHistorySave() {
		return true;
	}
}
