package hanghae.homework_posting.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class CommentLikes {

    @Id
    @GeneratedValue
    @Column(name = "comment_likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int status;

    public CommentLikes(Comment comment, Member member) {
        this.comment = comment;
        this.member = member;
        status = 1;
    }

    public CommentLikes() {

    }
}
