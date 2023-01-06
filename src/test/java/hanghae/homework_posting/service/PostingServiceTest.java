//package hanghae.homework_posting.service;
//
//import hanghae.homework_posting.dto.PostingRequestDto;
//import hanghae.homework_posting.dto.PostingResponseDto;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@SpringBootTest
//class PostingServiceTest {
//
//    @Autowired
//    PostingService postingService;
//
//    @AfterEach
//    void afterEach() {
//        postingService.deleteAll();
//    }
//
//    @Test
//    void createPostingTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        Long id = postingService.createPosting(requestDto);
//        Long getId = postingService.getPosting(id).getId();
//
//        assertThat(id).isEqualTo(getId);
//    }
//
//    @Test
//    void getPostingsTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setPassword("1234");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        postingService.createPosting(requestDto);
//        postingService.createPosting(requestDto);
//
//        List<PostingResponseDto> postings = postingService.getPostings();
//
//        assertThat(postings).size().isEqualTo(2);
//    }
//
//    @Test
//    void updateTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setPassword("1234");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        Long id = postingService.createPosting(requestDto);
//        requestDto.setTitle("aaaaa");
//
//        PostingResponseDto update = postingService.update(id, requestDto);
//
//        assertThat(update.getTitle()).isEqualTo(requestDto.getTitle());
//    }
//
//    @Test
//    void updateFailTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setPassword("1234");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        Long id = postingService.createPosting(requestDto);
//        requestDto.setTitle("aaaaa");
//        requestDto.setPassword("aaaaa");
//
//        assertThatThrownBy(() -> postingService.update(id, requestDto))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void getPostingTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setPassword("1234");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        Long id = postingService.createPosting(requestDto);
//
//        PostingResponseDto posting = postingService.getPosting(id);
//        assertThat(posting.getUsername()).isEqualTo(requestDto.getUsername());
//    }
//
//    @Test
//    void deletePostingTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setPassword("1234");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        Long id = postingService.createPosting(requestDto);
//
//        postingService.deletePosting(id, requestDto);
//
//        assertThatThrownBy(() -> postingService.getPosting(id))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//
//    @Test
//    void deletePostingFailTest() {
//        PostingRequestDto requestDto = new PostingRequestDto();
//        requestDto.setUsername("userA");
//        requestDto.setPassword("1234");
//        requestDto.setTitle("this is title");
//        requestDto.setContent("this is content");
//
//        Long id = postingService.createPosting(requestDto);
//        requestDto.setPassword("1111");
//
//        assertThatThrownBy(() -> postingService.deletePosting(id, requestDto))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//
//
//}