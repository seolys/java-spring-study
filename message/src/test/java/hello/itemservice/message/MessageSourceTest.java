package hello.itemservice.message;


import static org.assertj.core.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
public class MessageSourceTest {

	@Autowired MessageSource messageSource;

	@Test
	void hello() {
		String hello = messageSource.getMessage("hello", null, null);
		assertThat(hello).isEqualTo("안녕");
	}

	@Test
	void notFoundMessageCode() {
		assertThatThrownBy(() -> messageSource.getMessage("no_code", null, null))
				.isInstanceOf(NoSuchMessageException.class);
	}

	@Test
	void defaultMessage() {
		String defaultMessage = messageSource.getMessage("no_code", null, "기본 메시지", null);
		assertThat(defaultMessage).isEqualTo("기본 메시지");
	}

	@Test
	void argumentMessage() {
		String defaultMessage = messageSource.getMessage("hello.name", new Object[]{"Spring"}, null);
		assertThat(defaultMessage).isEqualTo("안녕 Spring");
	}

	@Test
	void defaultLang() {
		assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");
		assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
	}

	@Test
	void enLang() {
		assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
	}

}
