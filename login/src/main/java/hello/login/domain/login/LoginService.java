package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class LoginService {
	private  final MemberRepository memberRepository;

	/**
	 * 로그인
	 * @return null 로그인 실패
	 */
	public Member login(String loginId, String password) {
		Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
		return findMemberOptional
				.filter(m -> m.getPassword().equals(password))
				.orElse(null);
	}

}
