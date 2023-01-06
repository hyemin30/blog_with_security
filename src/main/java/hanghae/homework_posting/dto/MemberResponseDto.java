package hanghae.homework_posting.dto;


import hanghae.homework_posting.entity.Member;
import hanghae.homework_posting.entity.MemberRole;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private String username;
    private String password;

    private MemberRole role = MemberRole.USER;

    private String adminToken = "";

    public MemberResponseDto(Member member) {
        username = member.getUsername();
        password = member.getPassword();
        role = member.getRole();

    }
}
