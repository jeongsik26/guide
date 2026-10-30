package com.guide.ex.controller;

import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/include/header")
    public void header() {

    }

    @GetMapping("/main")
    public void main(HttpSession session, Model model) {
        if(session.getAttribute("member_id") != null){
            MemberDTO memberInfo = memberService.memberReadOne((Long) session.getAttribute("member_id"));
            model.addAttribute("member_info", memberInfo);
        }
        model.addAttribute("otherMembers", memberService.findProfileMember());
    }

}
