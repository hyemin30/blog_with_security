package hanghae.homework_posting.repository.custom_comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void cancelLike(Long id) {
        em.createQuery("update Comment c set c.likeCount = c.likeCount - 1" +
                        " where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();

    }
    @Override
    public void likeComment(Long id) {
        em.createQuery("update Comment c set c.likeCount = c.likeCount + 1" +
                        " where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();

    }
}

