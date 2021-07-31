package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

	/**
	 * supports의 결과가 true일때, validate가 실행된다.
	 * @param clazz
	 * @return
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		// item === clazz
		// item === subItem
		return Item.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Item item = (Item) target;

		// 검증 로직
//		ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//		ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");
		if (!StringUtils.hasText(item.getItemName())) {
			errors.rejectValue("itemName", "required");
		}
		if (Objects.isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			errors.rejectValue("price", "range", new Object[]{"1,000", "1,000,000"}, null);
		}
		if (Objects.isNull(item.getQuantity()) || item.getQuantity() > 9999) {
			errors.rejectValue("quantity", "max", new Object[]{"9,999"}, null);
		}

		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(item.getPrice()) && Objects.nonNull(item.getQuantity())) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				errors.reject("totalPriceMin", new Object[]{"10,000", resultPrice}, null);
			}
		}
	}

}
