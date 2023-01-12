package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.MemberRequestDto;
import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.MemberRole;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void createMember(MemberRequestDto requestDto) {

        //중복 사용자 검색
        Optional<Member> findMember = memberRepository.findByUsername(requestDto.getUsername());
        if (findMember.isPresent()) {
            throw new IllegalArgumentException("중복 사용자가 있습니다");
        }
        // 사용자 ROLE 확인
        MemberRole role = MemberRole.USER;
        if (isAdminRole(requestDto) && !isAdminTokenValid(requestDto)) {
            throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
        }

        if (isAdminRole(requestDto) && isAdminTokenValid(requestDto)) {
            role = MemberRole.ADMIN;
        }

        requestDto.setRole(role);

        Member member = new Member(requestDto);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void login(MemberRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        //사용자 확인
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다")
        );

        //비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getUsername(), member.getRole()));
    }

    private static boolean isAdminTokenValid(MemberRequestDto requestDto) {
        return requestDto.getAdminToken().equals(ADMIN_TOKEN);
    }

    private static boolean isAdminRole(MemberRequestDto requestDto) {
        return requestDto.getRole().equals(MemberRole.ADMIN);
    }
}
