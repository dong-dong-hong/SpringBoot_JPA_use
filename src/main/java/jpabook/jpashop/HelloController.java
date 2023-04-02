package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 뷰를 반환
public class HelloController {

    @GetMapping("hello") // hello로 GET요청이 들어오면
    public String hello(Model model) {
        model.addAttribute("data","hello");
        return "hello";
    }
}
