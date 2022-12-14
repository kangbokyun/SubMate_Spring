package SubMate.Service;

import SubMate.Config.Auth.Role;
import SubMate.Domain.DTO.*;
import SubMate.Domain.Entity.*;
import SubMate.Domain.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AdminService {
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	QnARepository qnARepository;
	@Autowired
	TendinousRepository tendinousRepository;
	@Autowired
	NoticeRepository noticeRepository;
	@Autowired
	ReportRepository reportRepository;


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
			MemberEntity memberEntity = memberRepository.findById(tendinousEntity.getMemberEntity().getMno()).get();
			TendinousDTO tendinousDTO = TendinousDTO.builder()
				.mno(tendinousEntity.getMemberEntity().getMno())
				.tno(tendinousEntity.getTno())
				.tcontents(tendinousEntity.getTcontents())
				.tselectcontentkind(tendinousEntity.getTselectcontentkind())
				.tselecttendinouskind(tendinousEntity.getTselecttendinouskind())
				.twriter(memberEntity.getMname())
				.tstatus(Integer.toString(tendinousEntity.getTstatus())).build();
			tendinousDTOS.add(tendinousDTO);
		}
		return tendinousDTOS;
	}

	public TendinousDTO TendinousView(int tno) {
		TendinousEntity tendinousEntity = tendinousRepository.findById(tno).get();
		if(tendinousEntity != null) {
			TendinousDTO tendinousDTO = TendinousDTO.builder()
				.tno(tendinousEntity.getTno())
				.mno(tendinousEntity.getMemberEntity().getMno())
				.twriter(tendinousEntity.getMemberEntity().getMname())
				.tcontents(tendinousEntity.getTcontents())
				.tselectcontentkind(tendinousEntity.getTselectcontentkind())
				.tselecttendinouskind(tendinousEntity.getTselecttendinouskind())
				.tstatus(Integer.toString(tendinousEntity.getTstatus()))
				.tanswer(tendinousEntity.getTanswer())
				.build();
			return tendinousDTO;
		}
		return null;
	}

	@Transactional
	public boolean TendinousAnswer(TendinousDTO tendinousDTO) {
		if(tendinousDTO != null) {
			TendinousEntity tendinousEntity = tendinousRepository.findById(tendinousDTO.getTno()).get();
			tendinousEntity.setTanswer(tendinousDTO.getTanswer());
			tendinousEntity.setTstatus(Integer.parseInt(tendinousDTO.getTstatus()));
			tendinousRepository.save(tendinousEntity);
			return true;
		} else {
			return false;
		}
	}

	public List<NoticeDTO> NoticeList() {
		List<NoticeEntity> noticeEntities = noticeRepository.findAll();
		System.out.println("noticeEntities : " + noticeEntities);
		List<NoticeDTO> noticeDTOS = new ArrayList<>();
		for(NoticeEntity noticeEntity : noticeEntities) {
			NoticeDTO noticeDTO = NoticeDTO.builder()
				.nno(noticeEntity.getNno()).ntitle(noticeEntity.getNtitle())
				.ncontents(noticeEntity.getNcontents()).mno(noticeEntity.getMemberEntity().getMno())
				.nkind(noticeEntity.getNkind())
				.build();
			System.out.println("noticeDTO : " + noticeDTO);
			noticeDTOS.add(noticeDTO);
		}
		return noticeDTOS;
	}

	public boolean NoticeWrite(NoticeDTO noticeDTO) {
		if(noticeDTO != null) {
			MemberEntity memberEntity = memberRepository.findById(noticeDTO.getMno()).get();
			NoticeEntity noticeEntity = NoticeEntity.builder()
				.ntitle(noticeDTO.getNtitle())
				.ncontents(noticeDTO.getNcontents())
				.nkind(noticeDTO.getNkind())
				.memberEntity(memberEntity)
				.build();
			noticeRepository.save(noticeEntity);

			return true;
		} else {
			return false;
		}
	}

	public List<ReportDTO> ReportList() {
		List<ReportEntity> reportEntities = reportRepository.findAll();
		List<ReportDTO> reportDTOS = new ArrayList<>();
		for(ReportEntity reportEntity : reportEntities) {
			ReportDTO reportDTO = ReportDTO.builder()
				.reportno(reportEntity.getReportno()).reportkind(reportEntity.getReportkind())
				.reportcontents(reportEntity.getReportcontents()).reportvalue(reportEntity.getReportvalue())
				.reportbno(reportEntity.getReportbno()).reportcheck(reportEntity.getReportcheck())
				.reportclickvalue(reportEntity.getReportclickvalue()).reportmno(reportEntity.getReportmno())
				.reportrno(reportEntity.getReportrno()).reportuserno(reportEntity.getReportuserno())
				.build();
			reportDTOS.add(reportDTO);
			if(reportDTOS.size() > 3) {
				break;
			}
		}
		return reportDTOS;
	}

	public List<MainChartDTO> MainChart() {
		List<QnAEntity> qnAEntities = qnARepository.findAll();
		List<TendinousEntity> tendinousEntities = tendinousRepository.findAll();
		List<ReportEntity> reportEntities = reportRepository.findAll();
		List<MainChartDTO> mainChartDTOS = new ArrayList<>();

		Calendar nowDate = Calendar.getInstance();
		Calendar vsDate = Calendar.getInstance();
		nowDate.setTime(new Date());
		vsDate.setTime(new Date());
		SimpleDateFormat nowDateSDF = new SimpleDateFormat("YY-MM-dd");

		vsDate.add(Calendar.DATE, - 10);
		Calendar calendar = Calendar.getInstance();
		int day = Integer.parseInt((nowDate.getTimeInMillis() - vsDate.getTimeInMillis()) / (1000 * 60 * 60 * 24) + "");
		// qnAEntities.get(i).getCreateDate().toString().split("T")[0].substring(2, 10)

		for(int i = 0; i < day; i++) {
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, - i);

			MainChartDTO mainChartDTO = new MainChartDTO();
			mainChartDTO.setChartdate(nowDateSDF.format(calendar.getTimeInMillis()));

			for(int j = 0; j < qnAEntities.size(); j++) {
				if(qnAEntities.get(j).getCreateDate().toString().split("T")[0].substring(2, 10).equals(nowDateSDF.format(calendar.getTimeInMillis()))) {
					mainChartDTO.setChartqna(mainChartDTO.getChartqna() + 1);
				} else {
					if(mainChartDTO.getChartqna() == 0) {
						mainChartDTO.setChartqna(0);
					}
				}
			}
			for(int j = 0; j < tendinousEntities.size(); j++) {
				if(tendinousEntities.get(j).getCreateDate().toString().split("T")[0].substring(2, 10).equals(nowDateSDF.format(calendar.getTimeInMillis()))) {
					mainChartDTO.setCharttendinous(mainChartDTO.getCharttendinous() + 1);
				} else {
					if(mainChartDTO.getCharttendinous() == 0) {
						mainChartDTO.setCharttendinous(0);
					}
				}
			}
			for(int j = 0; j < reportEntities.size(); j++) {
				if(reportEntities.get(j).getCreateDate().toString().split("T")[0].substring(2, 10).equals(nowDateSDF.format(calendar.getTimeInMillis()))) {
					mainChartDTO.setChartreport(mainChartDTO.getChartreport() + 1);
				} else {
					if(mainChartDTO.getChartreport() == 0) {
						mainChartDTO.setChartreport(0);
					}
				}
			}

			mainChartDTOS.add(mainChartDTO);
		}
		Comparator<MainChartDTO> listSort = new Comparator<MainChartDTO>() {
			@Override
			public int compare(MainChartDTO o1, MainChartDTO o2) {
				int a = Integer.parseInt(o1.getChartdate().replace("-", ""));
				int b = Integer.parseInt(o2.getChartdate().replace("-", ""));

				if(a < b) { return -1; } else {	return 1; }
			}
		};
		Collections.sort(mainChartDTOS, listSort);
		return mainChartDTOS;
	}
}
