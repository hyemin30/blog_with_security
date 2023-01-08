package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.CommentRequestDto;
import hanghae.homework_posting.dto.CommentResponseDto;
import hanghae.homework_posting.entity.*;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.CommentLikesRepository;
import hanghae.homework_posting.repository.CommentRepostiory;
import hanghae.homework_posting.repository.MemberRepository;
import hanghae.homework_posting.repository.PostingRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepostiory commentRepostiory;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final CommentLikesRepository likesRepository;
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

    @Transactional              // 댓글 id
    public boolean likeComment(Long id, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username).get();
        Comment comment = getComment(id);

        CommentLikes like = likesRepository.findByMemberIdAndCommentId(member.getId(), id);
        if (like == null) {
            CommentLikes commentLikes = new CommentLikes(comment, member);
            likesRepository.save(commentLikes);     //좋아요 테이블에 저장
            commentRepostiory.likeComment(id);      // 댓글 테이블에 좋아요갯수 추가
            return true;
        }
        if (like.getStatus() == 0) {  //좋아요 X
            likesRepository.likeComment(like.getId());  // 좋아요 테이블에 상태 변경 1
            commentRepostiory.likeComment(id);          // 댓글에 좋아요 갯수 추가
            return true;
        }
        if (like.getStatus() == 1) {  // 좋아요 O
            likesRepository.cancelLike(like.getId()); // 좋아요 테이블 상태 변경 0
            commentRepostiory.cancelLike(id);         // 댓글에 좋아요 갯수 감소
            return false;
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
