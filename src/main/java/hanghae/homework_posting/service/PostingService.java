package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.PostingRequestDto;
import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.entity.*;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.CommentRepostiory;
import hanghae.homework_posting.repository.MemberRepository;
import hanghae.homework_posting.repository.PostingLikesRepository;
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
    private final PostingLikesRepository likesRepository;
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
            postingRepository.delete(posting);
            return true;
        }
        return false;
    }

    @Transactional             // 게시글 id
    public boolean likePosting(Long id, HttpServletRequest request) {
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        Member member = memberRepository.findByUsername(username).get();
        Posting posting = postingRepository.findById(id).get();

        PostingLikes like = likesRepository.findByMemberIdAndPostingId(member.getId(), id);
        if (like == null) { //좋아요 이력이 없음
            PostingLikes postingLikes = new PostingLikes(posting, member);
            likesRepository.save(postingLikes); //좋아요 테이블에 저장
            postingRepository.likePosting(id);   // 게시글 db에 좋아요갯수 추가
            return true;
        }
        if (like.getStatus() == 0) {  //좋아요 X
            likesRepository.likePosting(like.getId());  // 좋아요 테이블에 상태 변경 1
            postingRepository.likePosting(id);          // 게시글에 좋아요 갯수 추가
            return true;
        }
        if (like.getStatus() == 1) {  // 좋아요 O
            likesRepository.cancelLike(like.getId()); // 좋아요 테이블 상태 변경 0
            postingRepository.cancelLike(id);         // 게시글에 좋아요 갯수 감소
            return false;
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
