package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.Posting;
import hanghae.homework_posting.repository.custom_posting.CustomPostingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> , CustomPostingRepository {
}

