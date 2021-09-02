package hello.login.web;

import static java.util.Objects.isNull;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final MemberRepository memberRepository;
	private final SessionManager sessionManager;

	//    @GetMapping("/")
	public String home() {
		return "home";
	}

	//    @GetMapping("/")
	public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
		if (isNull(memberId)) {
			return "home";
		}

		// 로그인
		Member loginMember = memberRepository.findById(memberId);
		if (isNull(loginMember)) {
			return "home";
		}
		model.addAttribute("member", loginMember);
		return "loginHome";
	}

	//	@GetMapping("/")
	public String homeLoginV2(HttpServletRequest request, Model model) {
		Member member = (Member) sessionManager.getSession(request);
		if (isNull(member)) {
			return "home";
		}
		model.addAttribute("member", member);
		return "loginHome";
	}

	//	@GetMapping("/")
	public String homeLoginV3(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		if (Objects.isNull(session)) {
			return "home";
		}
		Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
		if (isNull(member)) {
			return "home";
		}
		model.addAttribute("member", member);
		return "loginHome";
	}

	@GetMapping("/")
	public String homeLoginV4(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {
		if (isNull(member)) {
			return "home";
		}
		model.addAttribute("member", member);
		return "loginHome";
	}

}
