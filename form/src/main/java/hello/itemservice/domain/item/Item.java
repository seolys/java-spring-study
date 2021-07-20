package hello.itemservice.domain.item;

import java.util.List;
import lombok.Data;

@Data
public class Item {

    private Long id;
    private String itemName; // 상품명
    private Integer price; // 가격
    private Integer quantity; // 수량
    private Boolean open; // 판매 여부
    private List<String> regions; // 등록지역
    private ItemType itemType; // 상품구분
    private String deliveryCode; // 배송코드

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
