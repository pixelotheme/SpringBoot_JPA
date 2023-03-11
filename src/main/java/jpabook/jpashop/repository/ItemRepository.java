package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    // 처음에는 item id 가 없어서 em 으로 저장 - 새로 생성해서 등록하는것이다
    // else -> 이미 등록된(id 가 있는경우 ) 경우 merge 시킨다
    public void save(Item item){
        if(item.getId() == null){
            em.persist(item); //아이템 저장
        }else{
            em.merge(item); // update 와 비슷
        }
    }
    //단건 조회
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
