package hanghae.homework_posting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hanghae.homework_posting.dto.PostingRequestDto;
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
public class Posting extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "posting_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE)
    private List<PostingLikes> likes = new ArrayList<>();

    private Integer likeCount = 0;

    public Posting(PostingRequestDto requestDto, Member member) {
        this.member = member;
        title = requestDto.getTitle();
        content = requestDto.getContent();
    }

    public void update(PostingRequestDto requestDto) {
        title = requestDto.getTitle();
        content = requestDto.getContent();
    }

    // 생성 메서드
    public Posting createPosting(Member member) {
        Posting posting = new Posting();
        posting.setMember(member);
        List<Posting> postings = member.getPostings();
        postings.add(posting);
        member.setPostings(postings);
        return posting;
    }
}
