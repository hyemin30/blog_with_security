package hanghae.homework_posting.dto;

import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.Posting;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String postingMember;
    private String commentMember;
    private int likes;


    public CommentResponseDto(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        postingMember = comment.getPosting().getMember().getUsername();
        commentMember = comment.getMember().getUsername();
        likes = comment.getLikeCount();

    }
}

