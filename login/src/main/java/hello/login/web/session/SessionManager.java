package hello.login.web.session;

import static java.util.Objects.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * 세션관리
 */
@Component
public class SessionManager {

	public static final String SESSION_COOKIE_NAME = "mySessionId";

	private Map<String, Object> sessionStore = new ConcurrentHashMap<>();


	/**
	 * 세션 생성
	 */
	public void createSession(Object value, HttpServletResponse response) {
		// sessionId 생성(임의의 추정 불가능한 랜덤 값)
		// 세션 ID를 생성하고, 값을 세션에 저장
		String sessionId = UUID.randomUUID().toString();
		sessionStore.put(sessionId, value);

		// 쿠키 생성
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
		response.addCookie(cookie);
	}

	/**
	 * 세션 조회
	 */
	public Object getSession(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(sessionCookie == null) {
			return null;
		}
		return sessionStore.get(sessionCookie.getValue());
	}

	private Cookie findCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(isNull(cookies)) {
			return null;
		}
		return Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findAny()
				.orElse(null);
	}

	/**
	 * 세션 만료
	 */
	public void expire(HttpServletRequest request) {
		Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
		if(Objects.isNull(sessionCookie)) {
			return;
		}
		sessionStore.remove(sessionCookie.getValue());
	}

}
