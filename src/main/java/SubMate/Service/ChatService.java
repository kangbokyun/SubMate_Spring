package SubMate.Service;

import SubMate.Domain.DTO.ChatCallDTO;
import SubMate.Domain.Entity.ChatCallEntity;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Repository.ChatCallRepository;
import SubMate.Domain.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
	@Autowired
	ChatCallRepository chatCallRepository;
	@Autowired
	MemberRepository memberRepository;

	public boolean ChatCall(ChatCallDTO chatCallDTO) {
		if(chatCallDTO != null) {
			ChatCallEntity chatCallEntity = ChatCallEntity.builder()
				.callreceiverno(chatCallDTO.getCallreceiverno()).callsenderno(chatCallDTO.getCallsenderno())
				.build();
			chatCallRepository.save(chatCallEntity);
			return true;
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
}
