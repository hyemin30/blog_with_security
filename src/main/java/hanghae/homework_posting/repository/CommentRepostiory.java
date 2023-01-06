package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepostiory extends JpaRepository<Comment, Long> {

    public List<Comment> findAllByPostingId(Long id);
}
