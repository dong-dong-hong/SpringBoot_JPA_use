package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    protected Address(){}

    public Address(String city, String stereet, String zipCode) {
        this.city = city;
        this.stereet = stereet;
        this.zipCode = zipCode;
    }
    private String city;
    private String stereet;
    private String zipCode;



}
