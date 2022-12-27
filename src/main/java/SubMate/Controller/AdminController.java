package SubMate.Controller;

import SubMate.Domain.DTO.MemberDTO;
import SubMate.Domain.DTO.NoticeDTO;
import SubMate.Domain.DTO.QnADTO;
import SubMate.Domain.DTO.TendinousDTO;
import SubMate.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AdminController {
	@Autowired
	AdminService adminService;

	@PostMapping("/Admin/UserManage")
	public ResponseEntity<?> UserManage() {
		List<MemberDTO> memberDTOS = adminService.UserManage();
		return ResponseEntity.ok().body(memberDTOS);
	}

	@PostMapping("/Admin/ChangeRole")
	public ResponseEntity<?> ChangeRole(@RequestParam("mno") int mno, @RequestParam("mrole") String mrole) {
		boolean result = adminService.ChangeRole(mno, mrole);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/Admin/QnA")
	public ResponseEntity<?> QnAList() {
		List<QnADTO> qnADTOS = adminService.QnAList();
		return ResponseEntity.ok().body(qnADTOS);
	}

	@PostMapping("/Admin/Tendinous")
	public ResponseEntity<?> TendinousList() {
		List<TendinousDTO> tendinousDTOS = adminService.TendinousList();
		return ResponseEntity.ok().body(tendinousDTOS);
	}

	@PostMapping("Admin/NoticeList")
	public ResponseEntity<?> NoticeList() {
		List<NoticeDTO> noticeDTOS = adminService.NoticeList();
		return ResponseEntity.ok().body(noticeDTOS);
	}

	@PostMapping("/Admin/NoticeWrite")
	public ResponseEntity<?> NoticeWrite(@RequestParam("ntitle") String ntitle, @RequestParam("ncontents") String ncontents, @RequestParam("nkind") int nkind, @RequestParam("mno") int mno) {
		System.out.println("mno : " + mno);
		NoticeDTO noticeDTO = NoticeDTO.builder().nkind(nkind).mno(mno).ncontents(ncontents).ntitle(ntitle).build();
		boolean result = adminService.NoticeWrite(noticeDTO);
		if(result) {
			return ResponseEntity.ok().body(HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
		}
	}
}
