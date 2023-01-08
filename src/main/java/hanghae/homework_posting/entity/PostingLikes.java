package hanghae.homework_posting.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PostingLikes {

    @Id
    @GeneratedValue
    @Column(name = "posting_likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int status;

    public PostingLikes(Posting posting, Member member) {
        this.posting = posting;
        this.member = member;
        status = 1;
    }

    public PostingLikes() {

    }
}
