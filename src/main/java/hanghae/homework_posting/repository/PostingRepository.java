package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long>, CustomPostingRepository {

    List<Posting> findAllByOrderByCreatedAtDesc();

}

    //조회
