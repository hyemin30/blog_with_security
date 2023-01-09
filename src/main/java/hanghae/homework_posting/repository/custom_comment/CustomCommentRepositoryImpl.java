package hanghae.homework_posting.repository.custom_comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import static hanghae.homework_posting.entity.QComment.comment;

public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    public CustomCommentRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void cancelLike(Long id) {
        queryFactory
                .update(comment)
                .set(comment.likeCount, comment.likeCount.add(-1))
                .where(comment.id.eq(id))
                .execute();
    }

    @Override
    public void likeComment(Long id) {
        queryFactory
                .update(comment)
                .set(comment.likeCount, comment.likeCount.add(1))
                .where(comment.id.eq(id))
                .execute();
    }
}

