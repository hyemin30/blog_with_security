package hanghae.homework_posting.repository.custom_posting;

public interface CustomPostingLikesRepository {

    void cancelLike(Long id);
    void likePosting(Long id);
}
