package hanghae.homework_posting.dto;

import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.Posting;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    private Long id;
    private String content;
    private Posting posting;
    private Member member;


}
