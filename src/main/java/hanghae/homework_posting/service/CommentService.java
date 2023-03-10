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

        if (isSelfOrAdmin(username, role, comment)) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        }
        throw new IllegalArgumentException("????????? ????????? ?????? ???????????????");
    }

    @Transactional
    public boolean deleteComment(Long commentId, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        String role = (String) claims.get("role");

        Comment comment = findComment(commentId);

        if (isSelfOrAdmin(username, role, comment)) {
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
        if (like == null) { // ????????? ????????? ?????? ?????????
            CommentLikes commentLikes = new CommentLikes(comment, member);
            likesRepository.save(commentLikes);     //????????? ???????????? ??????
            commentRepository.likeComment(commentId);      // ?????? ???????????? ??????????????? ??????
            return true;
        }

        if (like.getStatus() == 0) {  //????????? ?????? ??????
            likesRepository.likeComment(like.getId());  // ????????? ???????????? ?????? ?????? 1
            commentRepository.likeComment(commentId);          // ????????? ????????? ?????? ??????
            return true;
        } else {
            likesRepository.cancelLike(like.getId()); // ????????? ????????? ?????? ?????? 0
            commentRepository.cancelLike(commentId);         // ????????? ????????? ?????? ??????
            return false;
        }
    }

    private static boolean isSelfOrAdmin(String username, String role, Comment comment) {
        return username.equals(comment.getMember().getUsername()) || role.equals(MemberRole.ADMIN.toString());
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("???????????? ???????????? ????????????")
        );
    }

    private Posting findPosting(Long postingId) {
        return postingRepository.findById(postingId).orElseThrow(
                () -> new IllegalArgumentException("???????????? ???????????? ????????????")
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("???????????? ?????? ???????????????")
        );
    }

    private Claims getClaims(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

//        jwtUtil.validateToken(token);
        return jwtUtil.getUserInfoFromToken(token);
    }
}
