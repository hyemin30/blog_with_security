package hanghae.homework_posting.repository.custom_posting;

import hanghae.homework_posting.dto.PostingResponseDto;

import java.util.List;

public interface CustomPostingRepository {

    void cancelLike(Long id);
    void likePosting(Long id);

    List<PostingResponseDto> getPostings();

}
