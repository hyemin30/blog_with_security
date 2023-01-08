package hanghae.homework_posting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hanghae.homework_posting.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    //양방향으로 참조를 한 이유는 댓글 가져올 때, 원본 게시글 작성자도 가져오려구..
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posting_id")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLikes> likes = new ArrayList<>();

    private Integer likeCount = 0;

    public Comment(CommentRequestDto commentDto) {
        content = commentDto.getContent();
    }

    // 생성 메서드
    public Comment createComment(Posting posting, Comment comment, Member member) {
        comment.setPosting(posting);
        comment.setMember(member);
        List<Comment> comments = posting.getComments();
        comments.add(comment);
        posting.setComments(comments);
        return comment;
    }

    public void update(CommentRequestDto requestDto) {
        content = requestDto.getContent();
    }
}

