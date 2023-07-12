package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.entity.OrderList;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.Item.repository.OrderRepository;
import com.popit.popitproject.common.s3.S3Service;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Data
@ToString
public class ItemService {

    private final ItemRepository itemRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Item registerItem(ItemInput itemInput, String userId) throws IOException {

        MultipartFile file = itemInput.getFile();
        String imageUrl = s3Service.uploadFile(file);

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new IllegalArgumentException("가입 된 회원이 아닙니다.");
        }

        Item item = Item.builder()
            .sellerId(userEntity.getStore().getId())
            .userId(userId)
            .itemNm(itemInput.getItemNm())
            .price(itemInput.getPrice())
            .stockNumber(itemInput.getStockNumber())
            .itemSellStatus(itemInput.getItemSellStatus())
            .itemImgURL(imageUrl)
            .regTime(LocalDateTime.now())
            .updateTime(LocalDateTime.now())
            .build();

        return itemRepository.save(item);
    }

    public List<Item> getfindBySellerId(Long sellerId) {
        return itemRepository.findBySellerId(sellerId);
    }


    public Item updateItemImage(Long id, MultipartFile file) throws IOException {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        if (file != null) {
            String fileUrl = s3Service.uploadFile(file);
            item.setItemImgURL(fileUrl);
            item.setUpdateTime(LocalDateTime.now());
        }
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, ItemInput itemInput) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        if (itemInput.getItemNm() != null) {
            item.setItemNm(itemInput.getItemNm());
        }
        if (itemInput.getPrice() != null) {
            item.setPrice(itemInput.getPrice());
        }
        if (itemInput.getItemSellStatus() != null) {
            item.setItemSellStatus(itemInput.getItemSellStatus());
        }
        if (itemInput.getStockNumber() != null) {
            item.setStockNumber(itemInput.getStockNumber());

            if (itemInput.getStockNumber() > 0) {
                item.setItemSellStatus(null);
            } else if (itemInput.getStockNumber() == 0) {
                item.setItemSellStatus("sold_out");
            }
        }

        item.setUpdateTime(LocalDateTime.now());

        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException(id));
        String itemImageUrl = item.getItemImgURL();
        String fileName = itemImageUrl.replace(
            "https://" + s3Service.getBucketName() + ".s3." + s3Service.getRegion()
                + ".amazonaws.com/", "");
        s3Service.deleteFile(fileName);
        itemRepository.deleteById(id);
    }

    public void deleteItems(Long sellerId) {
        List<Item> items = itemRepository.findBySellerId(sellerId);

        for (Item item : items) {
            setOrdersNullItemNm(item);

            String itemImageUrl = item.getItemImgURL();
            String fileName = itemImageUrl.replace(
                "https://" + s3Service.getBucketName() + ".s3." + s3Service.getRegion()
                    + ".amazonaws.com/", "");
            s3Service.deleteFile(fileName);
        }

        itemRepository.deleteAll(items);
    }

    public void setOrdersNullItemNm(Item item) {
        List<OrderList> orderLists = orderRepository.findByItem(item).orElseThrow();

        for (OrderList order : orderLists) {
            order.setItem(null);
            orderRepository.save(order);
        }
    }

    public class ItemNotFoundException extends RuntimeException {

        public ItemNotFoundException(Long id) {
            super("Item with id " + id + " does not exist.");
        }
    }

}
