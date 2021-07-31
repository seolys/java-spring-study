package hello.itemservice.validation;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

public class MessageCodesResolverTest {

	MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

	/**
	 * 객체 오류의 경우 다음 순서로 2가지 생성
	 * 1.: code + "." + object name
	 * 2.: code
	 *
	 * 예) 오류 코드: required, object name: item
	 * 1.: required.item
	 * 2.: required
	 */
	@Test
	void messageCodesResolverObject() {
		String errorCode = "required";
		String objectName = "item";

		String[] messageCodes = codesResolver.resolveMessageCodes(errorCode, objectName);
		System.out.println(Arrays.toString(messageCodes));

		assertThat(messageCodes)
				.containsExactly("required.item", "required");
//	ex) new ObjectError("item", messageCodes, arguments, "defaultMessage");
	}

	/**
	 * 필드 오류의 경우 다음 순서로4가지 메시지 코드 생성
	 *  1.: code + "." + object name + "." + field
	 *  2.: code + "." + field
	 *  3.: code + "." + field type
	 *  4.: code
	 *
	 * 예) 오류 코드: typeMismatch, object name "user", field "age", field type: int
	 * 1. "typeMismatch.user.age"
	 * 2. "typeMismatch.age"
	 * 3. "typeMismatch.int"
	 * 4. "typeMismatch"
	 */
	@Test
	void messageCodesResolverField() {
		String required = "required";
		String objectName = "item";
		String field = "itemName";
		Class<String> fieldType = String.class;

		String[] messageCodes = codesResolver.resolveMessageCodes(required, objectName, field, fieldType);
		System.out.println(Arrays.toString(messageCodes));

		assertThat(messageCodes)
				.containsExactly(
						"required.item.itemName",
						"required.itemName",
						"required.java.lang.String",
						"required");
//	ex) new FieldError("item", "itemName", rejectedValue, bindingFailure, messageCodes, arguments, "defaultMessage");
	}

}
