package hello.login.web.interceptor;

import static java.util.Objects.nonNull;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

	public static final String LOG_ID = "logId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		String logId = UUID.randomUUID().toString();
		request.setAttribute(LOG_ID, logId);

		// @RequestMapping: HandlerMethod
		// 정적 리소스: ResourceHttpRequestHandler
//		if (handler instanceof HandlerMethod) {
//			HandlerMethod handlerMethod = (HandlerMethod) handler;// 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있음.
//		}

		log.info("REQUEST [{}][{}][{}]", logId, requestURI, handler);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		String logId = (String) request.getAttribute(LOG_ID);
		log.info("postHandle [{}][{}]", logId, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		String requestURI = request.getRequestURI();
		String logId = (String) request.getAttribute(LOG_ID);
		log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);

		if (nonNull(ex)) {
			log.error("afterCompletion error catch", ex);
		}

	}
}
