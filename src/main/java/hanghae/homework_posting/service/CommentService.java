package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.CommentRequestDto;
import hanghae.homework_posting.dto.CommentResponseDto;
import hanghae.homework_posting.entity.*;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.CommentLikesRepository;
import hanghae.homework_posting.repository.CommentRepository;
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

    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final CommentLikesRepository likesRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(Long postingId, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        Member member = findMember(username);
        Posting posting = findPosting(postingId);

        Comment comment = new Comment(requestDto);
        comment.createComment(posting, comment, member);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto update(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        String role = (String) claims.get("role");

        Comment comment = findComment(commentId);

        if (username.equals(comment.getMember().getUsername()) || role.equals(MemberRole.ADMIN.toString())) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        }
        throw new IllegalArgumentException("본인의 댓글만 수정 가능합니다");
    }

    @Transactional
    public boolean deleteComment(Long commentId, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        String role = (String) claims.get("role");

        Comment comment = findComment(commentId);

        if (username.equals(comment.getMember().getUsername()) || role.equals(MemberRole.ADMIN.toString())) {
            commentRepository.delete(comment);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean likeComment(Long commentId, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        Member member = findMember(username);
        Comment comment = findComment(commentId);

        CommentLikes like = likesRepository.findByMemberIdAndCommentId(member.getId(), commentId);
        if (like == null) { // 좋아요 이력이 아예 없으면
            CommentLikes commentLikes = new CommentLikes(comment, member);
            likesRepository.save(commentLikes);     //좋아요 테이블에 저장
            commentRepository.likeComment(commentId);      // 댓글 테이블에 좋아요갯수 추가
            return true;
        }

        if (like.getStatus() == 0) {  //좋아요 취소 상태
            likesRepository.likeComment(like.getId());  // 좋아요 테이블에 상태 변경 1
            commentRepository.likeComment(commentId);          // 댓글에 좋아요 갯수 추가
            return true;
        } else {
            likesRepository.cancelLike(like.getId()); // 좋아요 테이블 상태 변경 0
            commentRepository.cancelLike(commentId);         // 댓글에 좋아요 갯수 감소
            return false;
        }
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
        );
    }

    private Posting findPosting(Long postingId) {
        return postingRepository.findById(postingId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다")
        );
    }

    private Claims getClaims(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

//        jwtUtil.validateToken(token);
        return jwtUtil.getUserInfoFromToken(token);
    }
}
