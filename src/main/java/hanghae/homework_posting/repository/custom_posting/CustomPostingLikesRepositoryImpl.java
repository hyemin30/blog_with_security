package hanghae.homework_posting.repository.custom_posting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import static hanghae.homework_posting.entity.QPostingLikes.postingLikes;

public class CustomPostingLikesRepositoryImpl implements CustomPostingLikesRepository {
    private final JPAQueryFactory queryFactory;

    public CustomPostingLikesRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void likePosting(Long id) {
        queryFactory
                .update(postingLikes)
                .set(postingLikes.status, 1)
                .where(postingLikes.id.eq(id))
                .execute();
    }

    @Override
    public void cancelLike(Long id) {
        queryFactory
                .update(postingLikes)
                .set(postingLikes.status, 0)
                .where(postingLikes.id.eq(id))
                .execute();
    }
}
