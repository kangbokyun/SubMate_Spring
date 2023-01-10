package SubMate.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class ChatController {
	@Autowired
	private SimpleMessagingTemplate simpleMessagingTemplate;

}
