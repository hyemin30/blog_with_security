package hanghae.homework_posting.repository.custom_posting;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.entity.Posting;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static hanghae.homework_posting.entity.QComment.comment;
import static hanghae.homework_posting.entity.QMember.member;
import static hanghae.homework_posting.entity.QPosting.posting;

public class CustomPostingRepositoryImpl implements CustomPostingRepository{

    private final JPAQueryFactory queryFactory;

    public CustomPostingRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void cancelLike(Long id) {
        queryFactory
                .update(posting)
                .set(posting.likeCount, posting.likeCount.add(-1))
                .where(posting.id.eq(id))
                .execute();
    }

    @Override
    public void likePosting(Long id) {
        queryFactory
                .update(posting)
                .set(posting.likeCount, posting.likeCount.add(1))
                .where(posting.id.eq(id))
                .execute();
    }

    @Override
    public List<PostingResponseDto> getPostings() {

        List<Posting> postings = queryFactory
                .selectFrom(posting).distinct()
                .join(posting.member, member).fetchJoin()
                .leftJoin(posting.comments, comment).fetchJoin()
                .orderBy(posting.createdAt.desc())
                .fetch();

        List<PostingResponseDto> responses = new ArrayList<>();

        for (Posting posting : postings) {
            responses.add(new PostingResponseDto(posting));
        }
        return responses;
    }
}
