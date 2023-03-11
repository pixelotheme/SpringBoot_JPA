package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    //ItemService 는 ItemRepository 클래스에 위임만 하는 단순한 클래스 이다
    public final ItemRepository itemRepository;

    @Transactional // readOnly 안먹게 override 시켜서  저장 가능하게
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item fineOne(Long itemId){
        return itemRepository.findOne(itemId);

    }

}
