package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.CommentRequestDto;
import hanghae.homework_posting.dto.CommentResponseDto;
import hanghae.homework_posting.dto.PostingRequestDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepostiory commentRepostiory;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        Member member = new Member();
        member = getMember(claims, member);

        Posting posting = getPosting(id);


        Comment comment = new Comment(requestDto);
        comment.createComment(posting, comment, member);
        commentRepostiory.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto update(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        Comment comment = getComment(id);   // comment id로 댓글 조회

        Member member = memberRepository.findByUsername(username).get();

        if (username.equals(comment.getMember().getUsername()) || member.getRole().equals(MemberRole.ADMIN)) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        }
        throw new IllegalArgumentException("본인의 댓글만 수정 가능합니다");
    }

    @Transactional
    public boolean deleteComment(Long id, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        Comment comment = getComment(id);

        Member member = memberRepository.findByUsername(username).get();

        if (username.equals(comment.getMember().getUsername()) || member.getRole().equals(MemberRole.ADMIN)) {
            commentRepostiory.delete(comment);
            return true;
        }
        return false;
    }

    private Comment getComment(Long id) {
        Comment comment = commentRepostiory.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다")
        );
        return comment;
    }

    private Posting getPosting(Long id) {
        Posting posting = postingRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );
        return posting;
    }

    /*
        getMember : 게시글 작성자를 가져옴
        param member : 댓글 작성자
     */
    private Member getMember(Claims claims, Member member) {
        try {
            member = memberRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return member;
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


}
