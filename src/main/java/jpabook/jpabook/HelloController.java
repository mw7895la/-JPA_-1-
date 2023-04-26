package jpabook.jpabook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!!");
        return "hello";
        //model에 데이터를 심어서 뷰에 넘길 수 있다.
    }
}
