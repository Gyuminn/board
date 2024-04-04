package org.kb.board;

import org.junit.jupiter.api.Test;
import org.kb.board.domain.PostEntity;
import org.kb.board.domain.ReplyEntity;
import org.kb.board.domain.UserEntity;
import org.kb.board.repository.PostRepository;
import org.kb.board.repository.ReplyRepository;
import org.kb.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    // 회원 데이터 삽입
    public void insertUser() {
        for (int i = 1; i <= 100; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .emailId("user" + i + "test.com")
                    .password("testPw")
                    .nickname("nickname" + i)
                    .introContent("hello my name is nickname" + i)
                    .provider("Test")
                    .build();
            userRepository.save(userEntity);
        }
    }

    @Test
    // 게시글 데이터 삽입
    public void insertPost() {
        for (int i = 1; i <= 100; i++) {
            /*
            UserEntity userEntity = UserEntity.builder()
                    .emailId("user" + i + "test.com")
                    .password("testPw")
                    .nickname("nickname" + i)
                    .provider("TEST")
                    .build();
            userRepository.save(userEntity);
             */

            UserEntity userEntity = userRepository.findUserEntityByEmailId("security User" + i);

            PostEntity postEntity = PostEntity.builder()
                    .writer(userEntity)
                    .title("title..." + i)
                    .content("content..." + i)
                    .build();

            // userEntity가 저장되지 않은 상태에서 저장하려고 해서 에러가 난다.
            // 영속성 전이에 대해서 Entity를 수정해 주어야 한다.(Cascade)
            postRepository.save(postEntity);
        }
    }
    @Test
    public void insertReplies() {
        for (int i = 0; i <= 100; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .emailId("user" + i + "test.com")
                    .password("testPw")
                    .nickname("nickname" + i)
                    .provider("TEST")
                    .build();
            userRepository.save(userEntity);

            PostEntity postEntity = PostEntity
                    .builder()
                    .writer(userEntity)
                    .title("title..." + i)
                    .content("content..." + i)
                    .build();
            postRepository.save(postEntity);

            ReplyEntity replyEntity = ReplyEntity
                    .builder()
                    .post(postEntity)
                    .replyer("TEST REPLYER")
                    .content("댓글..." + i)
                    .build();

            // userEntity가 저장되지 않은 상태에서 저장하려고 해서 에러가 난다.
            // 영속성 전이에 대해서 Entity를 수정해 주어야 한다.(Cascade)
            replyRepository.save(replyEntity);
        }
    }

    @Test
    @Transactional
    // 게시글 1개를 가져오는 메서드
    public void readBoard() {
        Optional<PostEntity> result = postRepository.findById(50L);
        PostEntity postEntity = result.get();
        System.out.println("postEntity :" + postEntity);
        System.out.println("postEntity writer: "+ postEntity.getWriter());
    }

    @Test
    @Transactional
    // 댓글 1개를 가져오는 메서드
    public void readReply() {
        Optional<ReplyEntity> result = replyRepository.findById(50L);
        ReplyEntity replyEntity = result.get();
        System.out.println("replyEntity : " + replyEntity);
        System.out.println("replyEntity Post : " + replyEntity.getPost());
    }


    @Test
    // Post를 가져올 때 Writer도 가져오기
    public void joinTest1() {
        List<Object[]> result = postRepository.getPostEntityWithWriter(50L);
        result.forEach(row -> System.out.println(Arrays.toString(row)));
        /*
        Object result = postRepository.getPostEntityWithWriter(100L);
        System.out.println("result : " + result);
        Object[] ar = (Object[]) result;
        System.out.println("형변환: " + ar);
        System.out.println("Arrays.toString() 결과 : " + Arrays.toString(ar));

        PostEntity postEntity = (PostEntity) ar[0];
        UserEntity userEntity = (UserEntity) ar[1];
        System.out.println("게시글 : " + postEntity);
        System.out.println("작성자 : " + userEntity);

         */
    }

    @Test
    // Post를 가져올 떄 Reply도 가져오기
    public void joinTest2() {
        List<Object[]> result = postRepository.getPostEntityWithReply(50L);
        for (Object[] a : result) {
            System.out.println(Arrays.toString(a));
        }
    }

    @Test
    public void joinTest3() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("postId").descending());
        Page<Object[]> result = postRepository.getPostEntityWithWriterAndReplyCount(pageable);

        result.get().forEach(row -> {
            System.out.println(Arrays.toString(row));
        });
    }

    @Test
    public void joinTest4() {
        List<Object[]> result = postRepository.getPostEntityByPostId(50L);
        for (Object[] a : result) {
            System.out.println(Arrays.toString(a));
        }
    }

    @Test
    public void testSearch1() {
        postRepository.search1();
    }

    @Test
    public void testSearch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("postId").descending()
                .and(Sort.by("title").ascending()));

        Page<Object[]> result = postRepository.searchPage("t", "1", pageable);

        for (Object[] row : result.getContent()) {
            System.out.println(Arrays.toString(row));
        }
    }

    @Test
    public void testInsertUsers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            UserEntity userEntity = UserEntity.builder()
                    .emailId("security User" + i)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            userRepository.save(userEntity);
        });
    }

    @Test
    public void comparePwTest() {

        UserEntity userEntity = userRepository.findById(100L).orElseThrow(() -> new UsernameNotFoundException("사용자 없다~"));

        boolean result = passwordEncoder.matches("1111", userEntity.getPassword());
        System.out.println(result);
    }
}
