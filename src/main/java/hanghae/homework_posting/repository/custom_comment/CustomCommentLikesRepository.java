package hanghae.homework_posting.repository.custom_comment;

public interface CustomCommentLikesRepository {



    void cancelLike(Long id);
    void likeComment(Long id);
}
