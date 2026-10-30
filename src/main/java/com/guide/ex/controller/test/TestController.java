package com.guide.ex.controller.test;

import com.guide.ex.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MemberService memberService;

//    css, js 연결 테스트
    @GetMapping("/test")
    public void test(){

    }

    @Operation(summary = "location input", description = "M: GET 위도, 경도값")
    @GetMapping("/test_location_input")
    public void test_location_input(){

    }

    @PostMapping("/test_location_input")
    public String test_location_save(HttpRequest req){
        return "redirect:/test_success";
    }

    @GetMapping("/test_img")
    public void imgTest() {

    }

//    @GetMapping("/test_out_member")
//    public void test_out_member(Model model) {
//        model.addAttribute("member_info", memberService.memberInfo(2L));
//    }


}
