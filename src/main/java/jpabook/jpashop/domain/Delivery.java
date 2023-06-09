package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class Delivery {

    @Id@GeneratedValue
    @Column(name = "delibery_id")
    public Long id;

    private String idname;

    @OneToOne(mappedBy = "delivery" , fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // READY(준비), COMP(배송)
}
