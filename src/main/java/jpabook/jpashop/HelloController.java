package jpabook.jpashop;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {

        model.addAttribute("data", "hello!!"); // view 로 데이터를 넘기기위한 객체
        return "hello";
        // return 은 화면 이름이다 resources 안에 templates 안에 hello.html 을 만들어두면
        // 해당 템플릿에 자동으로 렌더링 된다
    }
}
