package hello.blog.repository.member;

import hello.blog.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository jpaRepository;

    @Test
    void saveTest(){
        //given
        Member member = createMember();

        //when
        Member savedMember = jpaRepository.save(member);

        //then
        assertThat(savedMember.getId()).isEqualTo(member.getId());
        assertThat(savedMember.getUserId()).isEqualTo(member.getUserId());
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getName()).isEqualTo(member.getName());
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findByNameTest(){
        //given, when
        Member member = jpaRepository.findMemberByName("testMember2").orElse(new Member());

        //then
        assertThat(member.getName()).isEqualTo("testMember2");
    }

    @Test
    void findByIdTest() {
        //given
        Long id = jpaRepository.findMemberByName("testMember2").orElse(new Member()).getId();

        //when
        Member findMember = jpaRepository.findMemberById(id).orElse(new Member());

        //then
        assertThat(findMember.getId()).isEqualTo(id);
    }

    @Test
    void findByNameNoResult(){
        assertThrows(NoSuchElementException.class, () ->jpaRepository.findMemberByName("noName").get());
    }

    @Test
    void findAllTest(){
        //given, when
        List<Member> all = jpaRepository.findAll().get();

        //then
        assertThat(all.size()).isEqualTo(6);
    }

    @Test
    void dirtyCheckingTest(){

        //givem
        Long id = jpaRepository.findMemberByName("testMember2").orElse(new Member()).getId();
        Member member = jpaRepository.findMemberById(id).orElse(new Member());

        //when
        member.changeName("changedName");
        jpaRepository.save(member);
        Member findMember = jpaRepository.findMemberById(id).orElse(new Member());

        //then
        assertThat(findMember.getName()).isEqualTo("changedName");

    }

    @Test
    void deleteTest(){
        //given
        Member abc1 = jpaRepository.findMemberByUserId("abc1").get();

        //when
        jpaRepository.removeById(abc1.getId());
        List<Member> all = jpaRepository.findAll().get();

        //then
        assertThat(all.size()).isEqualTo(5);
    }


    @BeforeEach
    public void beforeTest(){
        jpaRepository.save(new Member("testMember1", "abc1", "123456!", "123@naver.com"));
        jpaRepository.save(new Member("testMember2", "abc2", "123456!", "123@naver.com"));
        jpaRepository.save(new Member("testMember2", "abc3", "123456!", "123@naver.com"));
        jpaRepository.save(new Member("testMember3", "abc4", "123456!", "123@naver.com"));
    }

    @AfterEach
    public void clear(){
        jpaRepository.clear();
    }

    public Member createMember(){
        return new Member("testMember", "abc123", "123456!", "123@naver.com");
    }
}