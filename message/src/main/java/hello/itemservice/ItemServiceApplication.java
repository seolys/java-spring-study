package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// Spring Boot는 기본적으로 등록해주기때문에, MessageSource를 수동으로 등록하지 않아도 됨.
//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		messageSource.setBasenames("messages", "errors"); // application.properties -> spring.messages.basename
//		messageSource.setDefaultEncoding("utf-8");
//		return messageSource;
//	}

}
