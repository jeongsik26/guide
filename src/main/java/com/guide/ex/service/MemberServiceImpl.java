package com.guide.ex.service;

import com.guide.ex.domain.member.Member;
import com.guide.ex.domain.member.MemberProfile;
import com.guide.ex.domain.post.Post;
import com.guide.ex.dto.member.MemberDTO;
import com.guide.ex.dto.member.MemberProfileDTO;
import com.guide.ex.dto.post.PostDTO;
import com.guide.ex.repository.member.MemberProfileRepository;
import com.guide.ex.repository.member.MemberRepository;
import com.guide.ex.repository.post.PostRepository;
import com.guide.ex.repository.search.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    @Value("${com.guide.upload.path}")
    private String uploadPath;

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final CommentRepository commentRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 회원 등록 작업(회원가입)
    @Override
    public void signUp(MemberDTO memberDto) {
        if (isIdAlreadyExists(memberDto.getUid())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        // BCrypt는 해시 안에 salt를 포함한다. salt 컬럼은 기존 스키마 호환용으로 빈 값 저장
        memberDto.setSalt("");
        memberDto.setPwd(passwordEncoder.encode(memberDto.getPwd()));

        Member member = modelMapper.map(memberDto, Member.class);

        memberRepository.save(member);
    }

    @Override
    public boolean login(String uid, String pwd) {
        Member member = memberRepository.findByUid(uid);

        if (member == null || member.isBan() || pwd == null) {
            return false;
        }
        if (member.getPwd().startsWith("$2")) {
            return passwordEncoder.matches(pwd, member.getPwd());
        }
        // 이전 방식(MD5 + salt)으로 가입한 회원
        return legacyHashPassword(pwd, member.getSalt()).equals(member.getPwd());
    }

    @Override
    public Long setLoginSession(String uid) {
        Member member = memberRepository.findByUid(uid);
        return member.getMemberId();
    }

    @Override
    public boolean isIdAlreadyExists(String uid) {
        Member member = memberRepository.findByUid(uid);
        if (member != null)
            return true;
        return false;
    }

    @Override
    public void fileUpload(MemberProfileDTO dto, MultipartFile file) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다."));
        Optional<MemberProfile> existing = memberProfileRepository.findByMember(member);

        boolean hasFile = file != null && !file.isEmpty();
        if (!hasFile && existing.isEmpty()) {
            throw new IllegalArgumentException("프로필 사진을 선택해 주세요.");
        }

        if (hasFile) {
            String originalName = StringUtils.getFilename(StringUtils.cleanPath(file.getOriginalFilename()));
            String uuid = UUID.randomUUID().toString();
            try {
                Path dir = Paths.get(uploadPath).toAbsolutePath();
                Files.createDirectories(dir);
                file.transferTo(dir.resolve(uuid + "_" + originalName));
            } catch (IOException e) {
                throw new RuntimeException("파일을 저장하는 도중 에러가 발생했습니다.", e);
            }
            dto.setUuid(uuid);
            dto.setFileName(originalName);
        }

        // 이미 프로필이 있으면 수정(사진을 새로 올리지 않았으면 기존 사진 유지), 없으면 등록
        if (existing.isPresent()) {
            MemberProfile profile = existing.get();
            profile.change(
                    hasFile ? dto.getUuid() : profile.getUuid(),
                    hasFile ? dto.getFileName() : profile.getFileName(),
                    dto.getContent(),
                    dto.getTravelType());
            return;
        }

        MemberProfile memberProfile = modelMapper.map(dto, MemberProfile.class);
        memberProfile.setMember(member);
        memberProfileRepository.save(memberProfile);
    }

    private String legacyHashPassword(String password, String salt) {
        String saltedPassword = password + salt;
        return DigestUtils.md5DigestAsHex(saltedPassword.getBytes());
    }

//    @Override
//    public MemberDTO memberReadOne(Long memberId) {
//        // Member 엔티티 조회
//        Optional<Member> result = memberRepository.findById(memberId);
//        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생
//
//        // MemberDTO로 변환
//        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
//
//        // MemberProfile 조회
//        Optional<MemberProfile> memberProfileResult = memberProfileRepository.findByMember(member);
//        MemberProfile memberProfile = memberProfileResult.orElse(null); // 조회된 MemberProfile이 없을 경우 null을 반환
//
//        // MemberProfile 정보를 MemberDTO에 추가
//        // 예를 들어 MemberDTO에 setProfileInfo 메서드를 정의했다고 가정
//        if (memberProfile != null) {
//            memberDTO.setProfileInfo(modelMapper.map(memberProfile, MemberProfileDTO.class));
//        } else {
//            memberDTO.setProfileInfo(null); // 또는 빈 MemberProfileDTO 객체를 넣어도 됩니다.
//        }
//
//        return memberDTO;
//    }

    @Override
    public MemberDTO memberReadOne(Long memberId) {
        // Member 엔티티 조회
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생

        // MemberDTO로 변환
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        // MemberProfile 조회
        Optional<MemberProfile> memberProfileResult = memberProfileRepository.findByMember(member);
        MemberProfile memberProfile = memberProfileResult.orElse(null); // 조회된 MemberProfile이 없을 경우 null을 반환

        // MemberProfile 정보를 MemberDTO에 추가
        // 예를 들어 MemberDTO에 setProfileInfo 메서드를 정의했다고 가정
        if (memberProfile != null) {
            memberDTO.setProfileInfo(modelMapper.map(memberProfile, MemberProfileDTO.class));
        } else {
            memberDTO.setProfileInfo(null); // 또는 빈 MemberProfileDTO 객체를 넣어도 됩니다.
        }

        List<Post> memberPosts = postRepository.findTop5ByMemberOrderByPostIdDesc(member);
        List<PostDTO> postDTOs = new ArrayList<>();
        if (memberPosts != null) {
            for (Post post : memberPosts) {
                postDTOs.add(modelMapper.map(post, PostDTO.class));
            }
        } else {
            postDTOs = null;
        }
        memberDTO.setPosts(postDTOs);
        return memberDTO;
    }

    @Override
    public void profileRegister(MemberProfileDTO memberProfileDTO) {
        Member member = memberRepository.findById(memberProfileDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberProfileDTO.getMemberId()));

        MemberProfile memberProfile = modelMapper.map(memberProfileDTO, MemberProfile.class);
        memberProfile.setMember(member); // Member 설정
        memberProfileRepository.save(memberProfile);
    }

    @Override
    public List<MemberDTO> findProfileMember() {
        List<Member> members = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for (Member member : members) {
            Optional<MemberProfile> memberProfileResult = memberProfileRepository.findByMember(member);
            memberProfileResult.ifPresent(memberProfile -> {
                MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
                MemberProfileDTO memberProfileDTO = modelMapper.map(memberProfile, MemberProfileDTO.class);
                memberDTO.setProfileInfo(memberProfileDTO);
                memberDTOList.add(memberDTO);
            });
        }
        return memberDTOList;
    }

    @Override
    public Page<PostDTO> memberPosts(HttpSession session, int size, int page) {
        Optional<Member> result = memberRepository.findById((Long) session.getAttribute("member_id"));
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다."));

        PageRequest pageable = PageRequest.of(Math.max(page, 1) - 1, size);
        Page<Post> postsPage = postRepository.findAllByMember(member, pageable);

        Page<PostDTO> postDTOPage = postsPage.map(post -> {
            PostDTO postDTO = modelMapper.map(post, PostDTO.class);
            return postDTO;
        });

        return postDTOPage;
    }

    @Override
    public void profileModify(MemberProfileDTO memberProfileDTO) {
        // MemberProfile 엔티티 조회 및 변경
        MemberProfile memberProfile = memberProfileRepository.findById(memberProfileDTO.getMemberProfileId())
                .orElseThrow(() -> new RuntimeException("MemberProfile not found"));
        memberProfile.change(
                memberProfileDTO.getUuid(),
                memberProfileDTO.getFileName(),
                memberProfileDTO.getContent(),
                memberProfileDTO.getTravelType()
        );
        memberProfileRepository.save(memberProfile);

    }

    @Override
    public boolean setProfileSession(Long member_id) {
        return memberProfileRepository.findByMember_MemberId(member_id).isPresent();
    }

    //    // 회원 삭제 작업
//    @Override
//    public void memberRemove(Long memberId) {
//        memberProfileRepository.deleteByMemberId(memberId);
//        memberRepository.deleteById(memberId);
//    }
    @Override
    public void updateCommentCount(Long memberId) {
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생
        int memberCommentCount = commentRepository.countByMember(member);
        member.setCommentCount(memberCommentCount);

        memberRepository.save(member);
    }

    @Override
    public void updateBoardCount(HttpSession session) {
        Long memberId = (Long) session.getAttribute("member_id");
        Optional<Member> result = memberRepository.findById(memberId);
        Member member = result.orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다.")); // 조회된 Member가 없을 경우 예외 발생
        int memberPostCount = postRepository.countByMember(member);
        member.setPostCount(memberPostCount);

        memberRepository.save(member);
    }

}
