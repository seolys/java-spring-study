package hello.login.web.filter;

import static java.util.Objects.nonNull;

import hello.login.web.SessionConst;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

@Slf4j
public class LoginCheckFilter implements Filter {

	private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURI = httpRequest.getRequestURI();

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			log.info("인증 체크 필터 시작 - {}", requestURI);
			if (isSendRedirectTarget(httpRequest, httpResponse, requestURI)) {
				// 로그인으로 redirect
				httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
				return;
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			throw e;
		} finally {
			log.info("인증 체크 필터 종료 - {}", requestURI);
		}
	}

	private boolean isSendRedirectTarget(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String requestURI) throws IOException {
		// 로그인 체크해야하는 URI가 아니면 false
		if (!isLoginCheckPath(requestURI)) {
			return false;
		}
		log.info("인증 체크 로직 실행 {}", requestURI);

		// 로그인 체크해야하는 URI이면서 세션이 존재하는경우 false
		HttpSession session = httpRequest.getSession(false);
		if (nonNull(session) && nonNull(session.getAttribute(SessionConst.LOGIN_MEMBER))) {
			return false;
		}

		// sendRedirect 대상
		log.info("미인증 사용자 요청 {}", requestURI);
		return true;
	}

	/**
	 * 화이트 리스트의 경우 인증 체크X
	 */
	private boolean isLoginCheckPath(String requestUri) {
		return !PatternMatchUtils.simpleMatch(whiteList, requestUri);
	}

}
