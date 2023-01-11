package hanghae.homework_posting.service;

import hanghae.homework_posting.dto.PostingRequestDto;
import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.entity.*;
import hanghae.homework_posting.jwt.JwtUtil;
import hanghae.homework_posting.repository.MemberRepository;
import hanghae.homework_posting.repository.PostingLikesRepository;
import hanghae.homework_posting.repository.PostingRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final PostingLikesRepository likesRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PostingResponseDto createPosting(PostingRequestDto requestDto, HttpServletRequest request) {
        //토큰 검증
        Claims claims = getClaims(request);

        //사용자 찾기
        Member member = findMember(claims.getSubject());
        
        Posting posting = new Posting(requestDto, member);        
        posting.createPosting(member);
        
        postingRepository.save(posting);
        return new PostingResponseDto(posting);
    }

    //전체 게시글 조회
    @Transactional
    public List<PostingResponseDto> getPostings() {
        return postingRepository.getPostings();
    }

    //게시글 1개 조회
    @Transactional
    public PostingResponseDto getPosting(Long id) {
        Posting posting = findPosting(id);
        return new PostingResponseDto(posting);
    }

    @Transactional
    public PostingResponseDto update(Long id, PostingRequestDto requestDto, HttpServletRequest request) {
       //사용자 검증
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        String role = (String) claims.get("role");

        Posting posting = findPosting(id);

        // 관리자 or 본인확인
        if (username.equals(posting.getMember().getUsername()) || role.equals(MemberRole.ADMIN.toString())) {
            posting.update(requestDto);
            return new PostingResponseDto(id, posting);
        }
        throw new IllegalArgumentException("본인의 글만 수정 가능합니다");
    }

    @Transactional
    public void deletePosting(Long id, HttpServletRequest request) {
        //토큰 검증
        Claims claims = getClaims(request);
        String username = claims.getSubject();
        String role = (String) claims.get("role");

        Posting posting = findPosting(id);

        if (username.equals(posting.getMember().getUsername()) || role.equals(MemberRole.ADMIN.toString())) {
            postingRepository.delete(posting);
            return;
        }
        throw new IllegalArgumentException("본인의 글만 삭제할 수 있습니다");
    }

    @Transactional
    public boolean likePosting(Long postingId, HttpServletRequest request) {
        // 토큰 검증
        Claims claims = getClaims(request);
        String username = claims.getSubject();

        //사용자 검색
        Member member = findMember(username);

        //게시글 검색
        Posting posting = postingRepository.findById(postingId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다")
        );

        PostingLikes like = likesRepository.findByMemberIdAndPostingId(member.getId(), postingId);
        if (like == null) { //좋아요 테이블에 이력이 없음
            PostingLikes postingLikes = new PostingLikes(posting, member);
            likesRepository.save(postingLikes);         //좋아요 테이블에 저장
            postingRepository.likePosting(postingId);   // 게시글 테이블에 좋아요갯수 추가
            return true;
        }

        if (like.getStatus() == 0) {  //좋아요 테이블에 있으나 좋아요 취소 상태
            likesRepository.likePosting(like.getId());  // 좋아요 테이블에 상태 변경 -> 1
            postingRepository.likePosting(postingId);          // 게시글에 좋아요 갯수 추가
            return true;
        } else {
            likesRepository.cancelLike(like.getId()); // 좋아요 테이블 상태 변경 -> 0
            postingRepository.cancelLike(postingId);         // 게시글에 좋아요 갯수 감소
            return false;
        }
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다")
        );
    }

    private Posting findPosting(Long postingId) {
        return postingRepository.findById(postingId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
        );
    }

    private Claims getClaims(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);

//        jwtUtil.validateToken(token);
        return jwtUtil.getUserInfoFromToken(token);
    }

    @Transactional
    public void deleteAll() {
        postingRepository.deleteAll();
    }
}
