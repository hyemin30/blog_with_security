package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.Comment;
import hanghae.homework_posting.repository.custom_comment.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {


}
