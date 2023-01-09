package hanghae.homework_posting.repository;

import hanghae.homework_posting.entity.PostingLikes;
import hanghae.homework_posting.repository.custom_posting.CustomPostingLikesRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostingLikesRepository extends JpaRepository<PostingLikes, Long>, CustomPostingLikesRepository {

    PostingLikes findByMemberIdAndPostingId(Long memberId, Long postingId);
}