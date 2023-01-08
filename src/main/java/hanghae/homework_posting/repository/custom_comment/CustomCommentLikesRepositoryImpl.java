package hanghae.homework_posting.repository.custom_comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomCommentLikesRepositoryImpl implements CustomCommentLikesRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void likeComment(Long id) {
        em.createQuery("update CommentLikes c set status = 1" +
                        " where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
    @Override
    public void cancelLike(Long id) {
        em.createQuery("update CommentLikes c set status = 0" +
                        " where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
