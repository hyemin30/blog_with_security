package hanghae.homework_posting.repository.custom_posting;

import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.entity.Posting;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class CustomPostingRepositoryImpl implements CustomPostingRepository{

    @PersistenceContext
    EntityManager em;

    @Override
    public void cancelLike(Long id) {
        em.createQuery("update Posting p set p.likeCount = p.likeCount - 1" +
                        " where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();

    }
    @Override
    public void likePosting(Long id) {
        em.createQuery("update Posting p set p.likeCount = p.likeCount + 1" +
                        " where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();

    }

    @Override
    public List<PostingResponseDto> getPostings() {
        List<Posting> postings = em.createQuery("select distinct p from Posting p" +
                " join fetch p.member" +
                " left outer join fetch p.comments" +
                " order by p.createdAt desc ", Posting.class).getResultList();

        List<PostingResponseDto> responses = new ArrayList<>();

        for (Posting posting : postings) {
            responses.add(new PostingResponseDto(posting));
        }

        return responses;
    }




}
