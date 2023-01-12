package hanghae.homework_posting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.Posting;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class PostingResponseDto {
    private String username;
    private String title;
    private String content;

    private Long id;
    private int likes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    private List<CommentResponseDto> comments;

    public PostingResponseDto(Long id, Posting posting) {
        this.id = id;
        title = posting.getTitle();
        username = posting.getMember().getUsername();
        content = posting.getContent();
        createdAt = posting.getCreatedAt();
        modifiedAt = posting.getModifiedAt();
        comments = posting.getComments().stream()
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
        likes = posting.getLikeCount();

    }

    public PostingResponseDto(Posting posting) {
        id = posting.getId();
        title = posting.getTitle();
        username = posting.getMember().getUsername();
        content = posting.getContent();
        createdAt = posting.getCreatedAt();
        modifiedAt = posting.getModifiedAt();
        comments = posting.getComments().stream()
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());
        likes = posting.getLikeCount();
    }
}
