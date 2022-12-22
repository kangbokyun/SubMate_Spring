package SubMate.Service;

import SubMate.Config.Auth.Role;
import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.DTO.QnADTO;
import SubMate.Domain.DTO.TendinousDTO;
import SubMate.Domain.Entity.MemberEntity;
import SubMate.Domain.Entity.QnAEntity;
import SubMate.Domain.Entity.TendinousEntity;
import SubMate.Domain.Repository.MemberRepository;
import SubMate.Domain.Repository.QnARepository;
import SubMate.Domain.Repository.TendinousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	QnARepository qnARepository;
	@Autowired
	TendinousRepository tendinousRepository;

	public List<MemberDTO> UserManage() {
		List<MemberEntity> memberEntities = memberRepository.findAll();
		List<MemberDTO> memberDTOS = new ArrayList<>();
		if(memberEntities != null) {
			for(MemberEntity memberEntity : memberEntities) {
				MemberDTO memberDTO = new MemberDTO();
				memberDTO.setMplatform(memberEntity.getMplatform());
				memberDTO.setMager(memberEntity.getMager());
				memberDTO.setProfileimg(memberEntity.getProfileimg());
				memberDTO.setCreateddate(memberEntity.getCreateDate());
				memberDTO.setMaddress(memberEntity.getMaddress());
				memberDTO.setMbirth(memberEntity.getMbirth());
				memberDTO.setMbti(memberEntity.getMbti());
				memberDTO.setMgender(memberEntity.getMgender());
				memberDTO.setMhobby(memberEntity.getMhobby());
				memberDTO.setMid(memberEntity.getMid());
				memberDTO.setMname(memberEntity.getMname());
				memberDTO.setMnickname(memberEntity.getMnickname());
				memberDTO.setMno(memberEntity.getMno());
				memberDTO.setMphone(memberEntity.getMphone());
				memberDTO.setMrole(memberEntity.getRole());

				memberDTOS.add(memberDTO);
			}
		}
		return memberDTOS;
	}

	@Transactional
	public boolean ChangeRole(int mno, String mrole) {
		MemberEntity memberEntity = memberRepository.findById(mno).get();
		if(memberEntity != null) {
			memberEntity.setRole(Role.valueOf(mrole));
			memberRepository.save(memberEntity);
			return true;
		} else {
			return false;
		}
	}

	public List<QnADTO> QnAList() {
		List<QnAEntity> qnAEntities = qnARepository.findAll();
		List<QnADTO> qnADTOS = new ArrayList<>();
		for(QnAEntity qnAEntity : qnAEntities) {
			QnADTO qnADTO = QnADTO.builder()
				.qnano(qnAEntity.getQnano()).qnacontents(qnAEntity.getQnacontents())
				.qnamno(qnAEntity.getQnano()).qnastatus(qnAEntity.getQnastatus())
				.qnatitle(qnAEntity.getQnatitle()).build();
			qnADTOS.add(qnADTO);
		}
		return qnADTOS;
	}
	public List<TendinousDTO> TendinousList() {
		List<TendinousEntity> tendinousEntities = tendinousRepository.findAll();
		List<TendinousDTO> tendinousDTOS = new ArrayList<>();
		for(TendinousEntity tendinousEntity : tendinousEntities) {
			TendinousDTO tendinousDTO = TendinousDTO.builder()
				.mno(tendinousEntity.getMemberEntity().getMno())
				.tno(tendinousEntity.getTno())
				.tcontents(tendinousEntity.getTcontents())
				.tselectcontentkind(tendinousEntity.getTselectcontentkind())
				.tselecttendinouskind(tendinousEntity.getTselecttendinouskind())
				.tstatus(Integer.toString(tendinousEntity.getTstatus())).build();
			tendinousDTOS.add(tendinousDTO);
		}
		return tendinousDTOS;
	}
}
