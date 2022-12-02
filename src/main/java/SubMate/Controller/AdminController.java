package SubMate.Controller;

import SubMate.Domain.DTO.MemberDTO;
import SubMate.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}