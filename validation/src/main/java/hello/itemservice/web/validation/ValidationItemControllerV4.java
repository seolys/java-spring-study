package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
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
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

	private final ItemRepository itemRepository;

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "validation/v4/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v4/item";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", new Item());
		return "validation/v4/addForm";
	}

	/**
	 * 필드 에러 시, 입력된 값을 유지시킨다.
	 * 에러코드를 적용한다.
	 * 소스 복잡도 개선.
	 * Validator 적용.(@Validated)
	 */
	@PostMapping("/add")
	public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(form.getPrice()) && Objects.nonNull(form.getQuantity())) {
			int resultPrice = form.getPrice() * form.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[]{"10,000", resultPrice}, null);
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) { // 부정의 부정은 읽기가 어려우니 리팩토링하라는 조언.
			log.info("errors={}", bindingResult);
			return "validation/v4/addForm";
		}

		Item item = Item.builder()
				.itemName(form.getItemName())
				.price(form.getPrice())
				.quantity(form.getQuantity())
				.build();
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/v4/items/{itemId}";
	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/v4/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
		// 특정 필드가 아닌 복합 룰 검증
		if (Objects.nonNull(form.getPrice()) && Objects.nonNull(form.getQuantity())) {
			int resultPrice = form.getPrice() * form.getQuantity();
			if (resultPrice < 10000) {
				bindingResult.reject("totalPriceMin", new Object[]{"10,000", resultPrice}, null);
			}
		}

		// 검증에 실패하면 다시 입력 폼으로
		if (bindingResult.hasErrors()) { // 부정의 부정은 읽기가 어려우니 리팩토링하라는 조언.
			log.info("errors={}", bindingResult);
			return "validation/v4/editForm";
		}

		Item item = Item.builder()
				.id(form.getId())
				.itemName(form.getItemName())
				.price(form.getPrice())
				.quantity(form.getQuantity())
				.build();
		itemRepository.update(itemId, item);
		return "redirect:/validation/v4/items/{itemId}";
	}

}

