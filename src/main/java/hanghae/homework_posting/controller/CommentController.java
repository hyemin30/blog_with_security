package hanghae.homework_posting.controller;

import hanghae.homework_posting.dto.CommentRequestDto;
import hanghae.homework_posting.dto.CommentResponseDto;
import hanghae.homework_posting.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=utf8")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{id}")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.update(id, requestDto, request);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        if (!commentService.deleteComment(id, request)) {
            return new ResponseEntity<>("본인의 글만 삭제할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("삭제 성공", HttpStatus.CREATED);
    }

    @PostMapping("/comments/{id}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long id, HttpServletRequest request) {
        if (!commentService.likeComment(id, request)) {
            return new ResponseEntity<>("좋아요 취소", HttpStatus.OK);
        }
        return new ResponseEntity<>("좋아요 성공", HttpStatus.OK);
    }

}

