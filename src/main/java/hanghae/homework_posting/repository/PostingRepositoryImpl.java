package hanghae.homework_posting.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostingRepositoryImpl {

    @PersistenceContext
    EntityManager em;




}
