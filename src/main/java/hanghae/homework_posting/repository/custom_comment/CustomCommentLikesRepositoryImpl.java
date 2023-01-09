package hanghae.homework_posting.repository.custom_comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import static hanghae.homework_posting.entity.QCommentLikes.commentLikes;

public class CustomCommentLikesRepositoryImpl implements CustomCommentLikesRepository {

    private final JPAQueryFactory queryFactory;

    public CustomCommentLikesRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void likeComment(Long id) {
        queryFactory
                .update(commentLikes)
                .set(commentLikes.status, 1)
                .where(commentLikes.id.eq(id))
                .execute();
    }

    @Override
    public void cancelLike(Long id) {
        queryFactory
                .update(commentLikes)
                .set(commentLikes.status, 0)
                .where(commentLikes.id.eq(id))
                .execute();
    }
}
