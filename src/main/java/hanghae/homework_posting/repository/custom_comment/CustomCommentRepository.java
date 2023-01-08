package hanghae.homework_posting.repository.custom_comment;

public interface CustomCommentRepository {

    void likeComment(Long id);

    void cancelLike(Long id);
}
