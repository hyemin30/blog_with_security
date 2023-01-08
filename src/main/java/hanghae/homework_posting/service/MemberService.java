package hanghae.homework_posting.service;

import hanghae.homework_posting.controller.EncryptionUtils;
import hanghae.homework_posting.dto.MemberRequestDto;
import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.MemberRole;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    @Transactional
    public boolean createMember(MemberRequestDto requestDto) {

        List<Member> members = memberRepository.findAll();

        //중복 사용자 검색
        for (Member member : members) {
            if (member.getUsername().equals(requestDto.getUsername())) {
                return false;
            }
        }

        // 사용자 ROLE 확인
        MemberRole role = MemberRole.USER;
        if (requestDto.getRole().equals(MemberRole.ADMIN)) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = MemberRole.ADMIN;
        }

        //암호화한 비밀번호 저장
        requestDto.setPassword(EncryptionUtils.encryptSHA256(requestDto.getPassword()));
        requestDto.setRole(role);
        Member member = new Member(requestDto);
        Member findMember = memberRepository.save(member);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean login(MemberRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = EncryptionUtils.encryptSHA256(requestDto.getPassword());

        Member member = memberRepository.findByUsername(username).orElse(new Member());

        if (!member.getPassword().equals(password) || member.getPassword().equals("0")) {
            return false;
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getUsername()));
        return true;
    }


}
