package SubMate.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // 웹소켓 서버를 사용할 때 사용하는 어노테이션. WebSocketMessageBrokerConfigurer를 상속받아 몇몇 메소드를 구현하여 websocket 연결 속성을 설정한다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	// ConfigurerMesageBorker는 한 클라에서 다른 클라로 메세지를 라우팅 할 때 사용하는 브로커를 구성
	// 첫번 째 라인에서 정의된 /app으로 시작하는 메세지만 메세지 핸들러로 라우팅 한다고 정의
	// 두번 째 라인에서 정의된 /topic으로 시작하는 주제를 가진 메세지를 핸들러로 라우팅하여 해당 주제에 가입한 모든 클라에게 메세지 발송
	// ex) /app으로 시작하는 메세지를 핸들러로 라우팅 => /room은 한 명이 message를 발행했을 때 해당 토픽을 구독하고 있는 n명에게 다시 메세지를 뿌림
	// /user는 한 명이 message를 발행했을 때 발행한 한 명에게 다시 정보를 보내는 경우에 사용

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 도착 경로에 대한 prefix를 설정 => /topic/hello라는 토픽에 대해 구독을 신청했을 때 실제 경로는 /app/topic/hello가 된다.
		registry.setApplicationDestinationPrefixes("/app");

		// registry.setUserDestinationPrefix("/user") : 사용자 대상을 식별하는데 사용되는 접두사를 구성. 사용자 대상은 사용자가 자신의 세션에 고유한 대기열 이름을 구독하고 다른 사용자가 고유한 사용자 별 대기엘에 베세지를 보낼 수 있는 기능을 제공
		// 예를 들어 사용자가 "/user/queue/position-updates"에 가입을 시도하면 대상이 "/queue/position-updatesi9apdfzo"로 변환되어 시도하는 다른 사용자와 충돌하지 않는 고유한 대기열 이름을 생성할 수 있다. 이후에 메세지가 "/user/{username}/queue/position-updates"로 전송되면
		// 대상이 "/queue/position-updatesi9oqdfzo"로 변환된다. 이러한 대상을 식별하는데 사용되는 기본 접두사는 "/user/이다.
		registry.enableSimpleBroker("/room", "/user"); // room => topic, user => queue
		registry.setUserDestinationPrefix("/user");
	}

	// 채팅 클라가 서버와 연결하는 웹소켓 세팅 부분 => 웹소켓 연결 주소 => registerStompEndpoints를 이용하여 클라이언트에서 websocket에 접속하는 endpoint를 등록
	// Stomp(Simple Text Oriented Messaging Protocol) : 스프링 프레임워크의 stomp 구현체를 사용한다는 의미
	// Stomp가 필요한 이유는 websocket은 통신 프로토콜이지 특정 주제에 가입한 사용자에게 메세지를 전송하는 기능을 제공하지 않는다.
	// 이를 쉽게 만들어주는게 stomp이다.
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
	}
}
