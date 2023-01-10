package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.CommentLikes;
import hanghae.homework_posting.repository.custom_comment.CustomCommentLikesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long>, CustomCommentLikesRepository {

    CommentLikes findByMemberIdAndCommentId(Long memberId, Long commentId);
}