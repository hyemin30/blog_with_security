package hanghae.homework_posting.controller;

import hanghae.homework_posting.dto.PostingRequestDto;
import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.service.PostingService;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=utf8")
public class PostingController {

    private final PostingService postingService;

    @GetMapping("/")
    public String home() {
        return "this is home";
    }

    @PostMapping("/postings")
    public PostingResponseDto createPosting(@RequestBody PostingRequestDto requestDto, HttpServletRequest request) {
        return postingService.createPosting(requestDto, request);
    }

    @GetMapping("/postings")
    public Result membersV2() {
        //List로 바로 반환하는 것이 아니라 Result로 감싸서 반환한다 -> 유연성
        List<Object> postings = postingService.getPostings().stream().collect(Collectors.toList());
        return new Result(postings);
    }

    @PutMapping("/postings/{id}")
    public PostingResponseDto updatePosting(@PathVariable Long id, @RequestBody PostingRequestDto requestDto, HttpServletRequest request) {

        return postingService.update(id, requestDto, request);
    }

    @GetMapping("/postings/{id}")
    public PostingResponseDto getPosting(@PathVariable Long id) {
        return postingService.getPosting(id);
    }

    @DeleteMapping("/postings/{id}")
    public ResponseEntity<String> deletePosting(@PathVariable Long id, HttpServletRequest request) {
        postingService.deletePosting(id, request);
        return new ResponseEntity<>("삭제 성공", HttpStatus.CREATED);
    }

    @PostMapping("/postings/{id}/like")
    public ResponseEntity<String> likePosting(@PathVariable Long id, HttpServletRequest request) {
        if (!postingService.likePosting(id, request)) {
            return new ResponseEntity<>("좋아요 취소", HttpStatus.OK);
        }
        return new ResponseEntity<>("좋아요 성공", HttpStatus.OK);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Result<T> { //<T> 이렇게 감싸서 반환하면 배열타입보다 유연성이 생겨서 좋음
        private T data;
    }
}
