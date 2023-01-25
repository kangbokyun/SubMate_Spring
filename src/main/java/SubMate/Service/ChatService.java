package SubMate.Service;

import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.ChatCallEntity;
import SubMate.Domain.Entity.ChatHistoryEntity;
import SubMate.Domain.Entity.ChatRoomEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.ChatCallRepository;
import SubMate.Domain.Repository.ChatHistoryRepository;
import SubMate.Domain.Repository.ChatRoomRepository;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class ChatService {
	@Autowired
	ChatCallRepository chatCallRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	ChatRoomRepository chatRoomRepository;
	@Autowired
	ChatHistoryRepository chatHistoryRepository;

	public boolean ChatCall(ChatCallDTO chatCallDTO) {
		System.out.println("@@@@@@@@@@@@@@\nchatCallDTO : " + chatCallDTO);
		List<ChatCallEntity> chatCallEntities = chatCallRepository.findAll();
		if(chatCallDTO != null) {
			if(chatCallEntities.size() == 0) {
				ChatCallEntity chatCallEntity2 = ChatCallEntity.builder()
						.callreceiverno(chatCallDTO.getCallreceiverno()).callsenderno(chatCallDTO.getCallsenderno())
						.build();
				chatCallRepository.save(chatCallEntity2);
				return true;
			} else {
				for (ChatCallEntity chatCallEntity : chatCallEntities) {
					if (!(chatCallEntity.getCallreceiverno() == chatCallDTO.getCallreceiverno() && chatCallEntity.getCallsenderno() == chatCallDTO.getCallsenderno())) {
						ChatCallEntity chatCallEntity2 = ChatCallEntity.builder()
								.callreceiverno(chatCallDTO.getCallreceiverno()).callsenderno(chatCallDTO.getCallsenderno())
								.build();
						chatCallRepository.save(chatCallEntity2);
						return true;
					}
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
			List<ChatCallEntity> chatCallEntities = chatCallRepository.findAll();
			for(ChatCallEntity chatCallEntity : chatCallEntities) {
				if(Integer.parseInt(chatCallEntity.getCallsenderno()) == chatRoomDTO.getSenderno() && Integer.parseInt(chatCallEntity.getCallreceiverno()) == chatRoomDTO.getReceiverno()) {
					chatCallRepository.delete(chatCallEntity);
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

	public List<ChatRoomDTO> ChatRoomList(int mno) {
		List<ChatRoomEntity> chatRoomEntities = chatRoomRepository.findAll();
		List<MemberEntity> memberEntities = memberRepository.findAll();
		List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
		List<ChatHistoryEntity> chatHistoryEntities = chatHistoryRepository.findAll();
		List<ChatHistoryDTO> chatHistoryDTOS = new ArrayList<>();
		for(ChatRoomEntity chatRoomEntity : chatRoomEntities) {
			if(mno == chatRoomEntity.getReceiverno() || mno == chatRoomEntity.getSenderno()) {
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
				for(MemberEntity memberEntity : memberEntities) {
					if(memberEntity.getMno() == chatRoomEntity.getSenderno()) {
						chatRoomDTO.setSimg(memberEntity.getProfileimg().split("MemberImg/")[1]);
					}
					if(memberEntity.getMno() == chatRoomEntity.getReceiverno()) {
						chatRoomDTO.setRimg(memberEntity.getProfileimg().split("MemberImg/")[1]);
					}
				}
				if(chatHistoryEntities.size() > 0) {
					for (ChatHistoryEntity chatHistoryEntity : chatHistoryEntities) {
						if (chatHistoryEntity.getChroomname().equals(chatRoomDTO.getRoomname())) {
							ChatHistoryDTO chatHistoryDTO = ChatHistoryDTO.builder()
									.chno(chatHistoryEntity.getChno())
									.chcontents(chatHistoryEntity.getChcontents())
									.createddate(chatHistoryEntity.getCreateDate().toString().split("T")[0])
									.build();
							chatRoomDTO.setCreatedate(chatHistoryEntity.getCreateDate().toString().split("T")[0]);
							chatHistoryDTOS.add(chatHistoryDTO);
						}
					}
				}
				if(chatHistoryDTOS.size() > 0) {
					Comparator<ChatHistoryDTO> listSort = new Comparator<ChatHistoryDTO>() {
						@Override
						public int compare(ChatHistoryDTO o1, ChatHistoryDTO o2) {
							int a = o1.getChno();
							int b = o2.getChno();

							if(a > b) { return -1; } else {	return 1;	 }
						}
					};
					Collections.sort(chatHistoryDTOS, listSort);

		//			if(chatHistoryDTOS.size())
					System.out.println("chatHistoryDTOS.size() : " + chatHistoryDTOS.size());
					System.out.println("chatHistoryDTOS : " + chatHistoryDTOS);
					chatRoomDTO.setChlastmessage(chatHistoryDTOS.get(0).getChcontents());
				}
				chatRoomDTOS.add(chatRoomDTO);
			}
		}
		return chatRoomDTOS;
	}

	public boolean ChatHistorySave(MessageDTO messageDTO) {
		ChatRoomEntity chatRoomEntity = chatRoomRepository.findByRoomname(messageDTO.getReceiverName());
		if(messageDTO.getReceiverName() != null && messageDTO.getStatus().equals("MESSAGE") && !messageDTO.getMessage().equals("")) {
			ChatHistoryEntity chatHistoryEntity = ChatHistoryEntity.builder()
				.chroomname(messageDTO.getReceiverName())
				.chsendername(messageDTO.getSenderName())
				.chsgender(messageDTO.getMgender())
				.chatRoomEntity(chatRoomEntity)
				.chcontents(messageDTO.getMessage())
				.chsenderno(messageDTO.getSenderno())
				.build();
			chatHistoryRepository.save(chatHistoryEntity);
			return true;
		} else {
			return false;
		}
	}

	public List<ChatHistoryDTO> ChatHistoryList(String roomname) {
		List<ChatHistoryEntity> chatHistoryEntities = chatHistoryRepository.findAll();
		List<ChatHistoryDTO> chatHistoryDTOS = new ArrayList<>();
		for(ChatHistoryEntity chatHistoryEntity : chatHistoryEntities) {
			if(chatHistoryEntity.getChroomname().equals(roomname)) {
				ChatHistoryDTO chatHistoryDTO = ChatHistoryDTO.builder()
					.chsenderno(chatHistoryEntity.getChsenderno())
					.chcontents(chatHistoryEntity.getChcontents())
					.chsgender(chatHistoryEntity.getChsgender())
					.chsendername(chatHistoryEntity.getChsendername())
					.chno(chatHistoryEntity.getChno())
					.createddate(chatHistoryEntity.getCreateDate().toString().split("T")[0])
					.build();
				chatHistoryDTOS.add(chatHistoryDTO);
			}
		}
		return chatHistoryDTOS;
	}
}
