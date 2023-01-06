package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.PostingRequestDto;
import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.MemberRole;
import hanghae.homework_posting.entity.Posting;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.CommentRepostiory;
import hanghae.homework_posting.repository.MemberRepository;
import hanghae.homework_posting.repository.PostingRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final CommentRepostiory commentRepostiory;
    private final JwtUtil jwtUtil;


    @Transactional
    public Long createPosting(PostingRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaims(request);
        Member member = new Member();
        try {
            member = memberRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Posting posting = new Posting(requestDto, member);
        posting.createPosting(member);
        postingRepository.save(posting);
        return posting.getId();
    }

    @Transactional
    public List<PostingResponseDto> getPostings() {
        return postingRepository.getPostings();
    }

    @Transactional
    public List<Object> getPosting(Long id) {
        Posting posting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
        );

        List<Object> responses = new ArrayList<>();

        responses.add(new PostingResponseDto(posting));
        return responses;
    }

    @Transactional
    public PostingResponseDto update(Long id, PostingRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        Posting posting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );

        Member member = memberRepository.findByUsername(username).get();

        if (username.equals(posting.getMember().getUsername()) || member.getRole().equals(MemberRole.ADMIN)) {
            posting.update(requestDto);
            return new PostingResponseDto(id, posting);
        }
        throw new IllegalArgumentException("본인의 글만 수정 가능합니다");
    }

    @Transactional
    public boolean deletePosting(Long id, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        Posting posting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );

        Member member = memberRepository.findByUsername(username).get();

        if (username.equals(posting.getMember().getUsername()) || member.getRole().equals(MemberRole.ADMIN)) {

            List<Comment> comments = commentRepostiory.findAllByPostingId(id);
            for (Comment comment : comments) {
                commentRepostiory.delete(comment);
            }

            postingRepository.delete(posting);
            return true;
        }
        return false;
    }
    private Claims getClaims(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims = null;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("토큰이 유효하지 않습니다");
            }
        }
        return claims;
    }

    @Transactional
    public void deleteAll() {
        postingRepository.deleteAll();
    }


}
