package hanghae.homework_posting.repository.custom_posting;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomPostingLikesRepositoryImpl implements CustomPostingLikesRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void likePosting(Long id) {
        em.createQuery("update PostingLikes p set status = 1" +
                        " where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
    @Override
    public void cancelLike(Long id) {
        em.createQuery("update PostingLikes p set status = 0" +
                        " where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
