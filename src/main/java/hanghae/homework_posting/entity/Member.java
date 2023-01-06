package hanghae.homework_posting.entity;

import hanghae.homework_posting.dto.MemberRequestDto;
import io.swagger.annotations.ApiParam;
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
public class Member {

    @Id
    @ApiParam(value = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotBlank
//    @NotNull
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z]).{4,10}")
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member")
    private List<Posting> postings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    public Member(MemberRequestDto requestDto) {
        username = requestDto.getUsername();
        password = requestDto.getPassword();
        role = requestDto.getRole();
    }
}
