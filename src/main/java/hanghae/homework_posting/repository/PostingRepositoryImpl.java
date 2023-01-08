package hanghae.homework_posting.repository;

import hanghae.homework_posting.dto.PostingResponseDto;
import hanghae.homework_posting.entity.Posting;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostingRepositoryImpl {

    @PersistenceContext
    EntityManager em;

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
