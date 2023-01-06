package hanghae.homework_posting.repository;

import hanghae.homework_posting.dto.PostingResponseDto;

import java.util.List;

public interface CustomPostingRepository {

    List<PostingResponseDto> getPostings();
}
