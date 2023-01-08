package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.repository.custom_comment.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepostiory extends JpaRepository<Comment, Long>, CustomCommentRepository {

    List<Comment> findAllByPostingId(Long id);

}
