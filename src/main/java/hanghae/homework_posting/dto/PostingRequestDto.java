package hanghae.homework_posting.dto;

import hanghae.homework_posting.entity.Posting;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostingRequestDto {
    private Long id;

    private String username;
    private String title;
    private String content;


}


