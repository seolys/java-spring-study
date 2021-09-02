package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LogInterceptor logInterceptor;
	private final LoginCheckInterceptor loginCheckInterceptor;

//	@Bean
//	public FilterRegistrationBean logFilter() {
//		FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//		filterFilterRegistrationBean.setFilter(new LogFilter());
//		filterFilterRegistrationBean.setOrder(1);
//		filterFilterRegistrationBean.addUrlPatterns("/*");
//		return filterFilterRegistrationBean;
//	}

//	@Bean
//	public FilterRegistrationBean loginCheckerFilter() {
//		FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//		filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
//		filterFilterRegistrationBean.setOrder(2);
//		filterFilterRegistrationBean.addUrlPatterns("/*");
//		return filterFilterRegistrationBean;
//	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor)
				.order(1)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**", "/*.ico", "/error");

		registry.addInterceptor(loginCheckInterceptor)
				.order(2)
				.addPathPatterns("/**")
				.excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginMemberArgumentResolver());
	}
}
