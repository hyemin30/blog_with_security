package hanghae.homework_posting.controller;

import hanghae.homework_posting.dto.MemberRequestDto;
import hanghae.homework_posting.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=utf8")
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> createMember(@Valid @RequestBody MemberRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("아이디는 영문소문자, 숫자 4~10자리, 비밀번호는 영문대소문자, 숫자, 특수문자 8~15자리로 입력하세요");
        }

        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        memberService.createMember(requestDto);

        return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Transactional(readOnly = true)
    public ResponseEntity<String> login(@RequestBody MemberRequestDto requestDto, HttpServletResponse response) {

        memberService.login(requestDto, response);
        return new ResponseEntity<>("로그인 성공", HttpStatus.CREATED);
    }
}
