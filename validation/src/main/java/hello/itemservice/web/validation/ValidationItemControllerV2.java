package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

	private final ItemRepository itemRepository;

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "validation/v2/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v2/item";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", new Item());
		return "validation/v2/addForm";
	}

	/**
	 * 문제점: 필드 에러 시, 입력된 값이 유지되지 않음.
	 * @param item
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
//	@PostMapping("/add")
	public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		// 검증 로직
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
		}
		if (Objects.isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
		}
		if (Objects.isNull(item.getQuantity()) || item.getQuantity() > 9999) {
			bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
		}

		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(item.getPrice()) && Objects.nonNull(item.getQuantity())) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) { // 부정의 부정은 읽기가 어려우니 리팩토링하라는 조언.
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	/**
	 * 필드 에러 시, 입력된 값을 유지시킨다.
	 * 에러코드 미적용.
	 * @param item
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
//	@PostMapping("/add")
	public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		// 검증 로직
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수입니다."));
		}
		if (Objects.isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
		}
		if (Objects.isNull(item.getQuantity()) || item.getQuantity() > 9999) {
			bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9,999 까지 허용합니다."));
		}

		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(item.getPrice()) && Objects.nonNull(item.getQuantity())) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.addError(new ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) { // 부정의 부정은 읽기가 어려우니 리팩토링하라는 조언.
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	/**
	 * 필드 에러 시, 입력된 값을 유지시킨다.
	 * 에러코드를 적용한다.
	 * @param item
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
//	@PostMapping("/add")
	public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		// 검증 로직
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, "상품 이름은 필수입니다."));
		}
		if (Objects.isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{"1,000", "1,000,000"},
					"가격은 1,000 ~ 1,000,000 까지 허용합니다."));
		}
		if (Objects.isNull(item.getQuantity()) || item.getQuantity() > 9999) {
			bindingResult.addError(
					new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{"9,999"}, "수량은 최대 9,999 까지 허용합니다."));
		}

		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(item.getPrice()) && Objects.nonNull(item.getQuantity())) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{"10,000", resultPrice},
						"가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) { // 부정의 부정은 읽기가 어려우니 리팩토링하라는 조언.
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}

	/**
	 * 필드 에러 시, 입력된 값을 유지시킨다.
	 * 에러코드를 적용한다.
	 * 소스 복잡도 개선
	 * @param item
	 * @param bindingResult
	 * @param redirectAttributes
	 * @param model
	 * @return
	 */
	@PostMapping("/add")
	public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		// 바인딩 에러가 있는경우 검증로직은 SKIP
		if (bindingResult.hasErrors()) {
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}

		log.info("objectName={}", bindingResult.getObjectName());
		log.info("target={}", bindingResult.getTarget());

		// 검증 로직
//		ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//		ValidationUtils.rejectIfEmpty(bindingResult, "itemName", "required");
		if (!StringUtils.hasText(item.getItemName())) {
			bindingResult.rejectValue("itemName", "required");
		}
		if (Objects.isNull(item.getPrice()) || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			bindingResult.rejectValue("price", "range", new Object[]{"1,000", "1,000,000"}, null);
		}
		if (Objects.isNull(item.getQuantity()) || item.getQuantity() > 9999) {
			bindingResult.rejectValue("quantity", "max", new Object[]{"9,999"}, null);
		}

		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(item.getPrice()) && Objects.nonNull(item.getQuantity())) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[]{"10,000", resultPrice}, null);
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) { // 부정의 부정은 읽기가 어려우니 리팩토링하라는 조언.
			log.info("errors={}", bindingResult);
			return "validation/v2/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v2/items/{itemId}";
	}


	private boolean hasError(Map<String, String> errors) {
		return !errors.isEmpty();
	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v2/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
		itemRepository.update(itemId, item);
		return "redirect:/validation/v2/items/{itemId}";
	}

}

