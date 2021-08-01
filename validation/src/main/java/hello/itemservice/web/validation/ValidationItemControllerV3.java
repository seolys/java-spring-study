package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

	private final ItemRepository itemRepository;

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "validation/v3/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v3/item";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", new Item());
		return "validation/v3/addForm";
	}

	/**
	 * 필드 에러 시, 입력된 값을 유지시킨다.
	 * 에러코드를 적용한다.
	 * 소스 복잡도 개선.
	 * Validator 적용.(@Validated)
	 */
//	@PostMapping("/add")
	public String addItemV1(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
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
			return "validation/v3/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v3/items/{itemId}";
	}

	/**
	 * 필드 에러 시, 입력된 값을 유지시킨다.
	 * 에러코드를 적용한다.
	 * 소스 복잡도 개선.
	 * Validator 적용.(@Validated)
	 */
	@PostMapping("/add")
	public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
			return "validation/v3/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v3/items/{itemId}";
	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v3/editForm";
	}

	//		@PostMapping("/{itemId}/edit")
	public String editV1(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {
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
			return "validation/v3/editForm";
		}

		itemRepository.update(itemId, item);
		return "redirect:/validation/v3/items/{itemId}";
	}

	@PostMapping("/{itemId}/edit")
	public String editV2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
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
			return "validation/v3/editForm";
		}

		itemRepository.update(itemId, item);
		return "redirect:/validation/v3/items/{itemId}";
	}

}

