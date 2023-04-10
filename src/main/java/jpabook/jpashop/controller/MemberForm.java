package jpabook.jpashop.controller;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "회원아이디는 필수입니다.")
    private String idname;

    @NotEmpty(message = "비밀번호는 필수입니다. ")
    private String pw;

    private String city;
    private String street;
    private String zipcode;

}
