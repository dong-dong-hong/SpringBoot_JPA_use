package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
public class Category {

    @Id@GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}

// 실무에서는 @ManyToMany를 사용하지 말자
//> @ManyToMany는 편리한 것 같지만, 중간 테이블( CATEGORY_ITEM )에 컬럼을 추가할 수 없고,
// 세밀하게 쿼리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있다.
// 중간 엔티티(CategoryItem)를 만들고 @ManyToOne , @OneToMany 로 매핑해서 사용하자.
// 정리하면 대다대 매핑을 일대다, 다대일 매핑으로 풀어내서 사용하자.
