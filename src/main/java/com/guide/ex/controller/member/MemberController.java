package com.guide.ex.controller.member;

import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.dto.member.MemberProfileDTO;
import com.guide.ex.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/signUp")
    public void signUp() {

    }

    @PostMapping("/signUp")
    public String signUp(MemberDTO memberDTO) {
        if (memberService.isIdAlreadyExists(memberDTO.getUid())) {
            return "redirect:/member/signUp?error=duplicate";
        }
        memberService.signUp(memberDTO);
        return "redirect:/member/login";
    }

    @PostMapping("/checkDuplicateId")
    @ResponseBody
    public ResponseEntity<String> checkDuplicateId(@RequestParam String uid) {

        if (memberService.isIdAlreadyExists(uid)) {
            return ResponseEntity.ok("Duplicate");
        } else {
            return ResponseEntity.ok("Available");
        }
    }

    @GetMapping("/login")
    public void login() {

    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("uid") String uid, @RequestParam("pwd") String pwd, HttpSession session,Model model) {
        boolean check = memberService.login(uid, pwd);

        if (check) {
            Long memberId = memberService.setLoginSession(uid);
            MemberDTO member = memberService.memberReadOne(memberId);
            session.setAttribute("member_id", memberId);
            session.setAttribute("member_name", member.getName());
            if (memberService.setProfileSession(memberId)) {
                session.setAttribute("member_profile", true);
            }
            return "redirect:/main";

        } else {
            return "redirect:/member/login?error=incorrect";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/main";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        if (session.getAttribute("member_id") == null) {
            return "redirect:/member/login";
        }
        return "member/profile";
    }

    @PostMapping("/profile")
    public String uploadFile(HttpSession session, MemberProfileDTO memberProfileDTO,
                             @RequestParam(value = "file", required = false) MultipartFile file) {
        Long memberId = (Long) session.getAttribute("member_id");
        if (memberId == null) {
            return "redirect:/member/login";
        }
        memberProfileDTO.setMemberId(memberId);
        memberService.fileUpload(memberProfileDTO, file);
        session.setAttribute("member_profile", true);

        return "redirect:/main";
    }

    @GetMapping("/profileModify")
    public String profileModify(Model model, HttpSession session) {
        Long sessionMemberId = (Long) session.getAttribute("member_id");
        if (sessionMemberId == null) {
            return "redirect:/member/login";
        }
        MemberDTO member = memberService.memberReadOne(sessionMemberId);
        model.addAttribute("member", member);
        return "member/profileModify";
    }

    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model model) {

        Long memberId = (Long) session.getAttribute("member_id");

        if (memberId == null) {
            return "redirect:/member/login";
        } else if (memberId != null && !memberService.setProfileSession(memberId)) {
            return "redirect:/member/profile";
        }

        MemberDTO member = memberService.memberReadOne(memberId);

        model.addAttribute("member", member);
        return "member/myPage";
    }

    @GetMapping("/otherPage/{memberId}")
    public String otherPage(@PathVariable Long memberId, HttpSession session, Model model) {
        Long senderId = (Long) session.getAttribute("member_id");

        if(senderId != null && senderId.equals(memberId)){
            return "redirect:/member/myPage";
        }

        // 상대방의 memberId와 로그인한 사용자의 memberId를 모델에 추가
        model.addAttribute("receiverId", memberId);
        model.addAttribute("senderId", senderId);

        MemberDTO memberDTO = memberService.memberReadOne(memberId);
        model.addAttribute("member", memberDTO);
        return "member/otherPage";
    }
}
