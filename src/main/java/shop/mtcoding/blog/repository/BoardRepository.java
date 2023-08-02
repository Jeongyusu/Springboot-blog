package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;

// BoardController, UserController, UserRepository - 우리가 띄운 것
// EntitiyManager, HttpSession - spring이 띄운 것
@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

    // select id, title frm board_tb
    // resultClass 안붙이고 직접 파싱하려면!!
    // Object[] 로 리턴됨
    // object[0] = 1
    // object[1] = 제목1
    public int count() {
        // Entitiy 타입만 가능
        Query query = em.createNativeQuery("select count(*) from board_tb");
        // 원래는 Object 배열로 리턴 받는다, Object 배열은 칼럼의 연속이다.
        // 그룹함수를 써서, 하나의 칼럼을 조회하면, Object로 리턴된다.
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public int count2() {
        Query query = em.createNativeQuery("select count(*) from board_tb", Board.class);
        List<Board> boardList = (List<Board>) query.getResultList();
        return boardList.size();
    }

    @Transactional
    public void save(WriteDTO writeDTO, Integer userId) {
        Query query = em.createNativeQuery(
                "insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())");
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        query.executeUpdate();

    }

    public List<Board> findAll(int page) {
        final int SIZE = 3;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page, :size", Board.class);
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
        // execute query와 유사한 역할
        List<Board> boardList = query.getResultList();
        return boardList;

    }

    public Board findbyId(Integer id) {
        Query query = em.createNativeQuery("select * from board_tb where id = :id",
                Board.class);
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

}
