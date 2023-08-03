package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @PostMapping("/board/{id}/update") // form이 없으면 업데이트하겟다는 것
    public String update(@PathVariable Integer id, UpdateDTO updateDTO) {
        // 1. 인증검사

        // 2. 권한체크

        // 3. 핵심 로직
        // update board_tb set title = :title, content = :content where id = :id
        boardRepository.update(id, updateDTO);

        return "redirect:/board/" + id;

    }

    @GetMapping("/board/{id}/updateForm") // form이 있으면 달라는 것
    public String updateForm(@PathVariable Integer id, HttpServletRequest request) {
        // 1. 인증검사

        // 2. 권한 체크

        // 3. 핵심 로직
        Board board = boardRepository.findbyId(id);
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable Integer id) { // 1.Pathvariable 값 받기
        // 2.인증검사 (로그인 페이지 보내기)
        // ssession에 접근해서 ssesionUser 키 값을 가져오세요
        // null이면, 로그인 페이지로 보내고
        // null 아니면, 3번을 실행하세요.
        // * 유효성 검사 X(바디데이터 없음)
        // 3. 권한검사
        // 4. 모델에 접근해서 삭제
        // boardRepository.deleteById(id); 호출하세요 -> 리턴을 받지마세요
        // delete from board_tb where id = :id
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm"; // 401
        }
        Board board = boardRepository.findbyId(id);
        if (sessionUser.getId() != board.getUser().getId()) {
            return "redirect:/40x";
        }
        boardRepository.deleteById(id);
        return "redirect:/";

    }

    // 쿼리스트링은 문자열이라서 "0" 으로 표현
    // localhost:8080?page=0
    @GetMapping({ "/", "/board" })
    public String index(
            @RequestParam(defaultValue = "0") Integer page, HttpServletRequest request) {
        // 1. 유효성 검사 X
        // 2. 인증검사 X

        List<Board> boardList = boardRepository.findAll(page); // page = 1
        int totalCount = boardRepository.count(); // totalCount = 5

        System.out.println("테스트 : totalCount :" + totalCount);
        int totalPage = totalCount / 3; // totalPage = 1
        if (totalCount % 3 > 0) {
            totalPage = totalPage + 1; // totalPage = 2
        }
        boolean last = totalPage - 1 == page;

        System.out.println("테스트 :" + boardList.size());
        System.out.println("테스트 :" + boardList.get(0).getTitle());

        request.setAttribute("boardList", boardList);
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);
        request.setAttribute("first", page == 0 ? true : false);
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

        return "index";
    }

    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";

        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";

        }
        // 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        boardRepository.save(writeDTO, sessionUser.getId());
        return "redirect:/";

    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser"); // 세션 접근
        Board board = boardRepository.findbyId(id);

        boolean pageOwner = false;
        if (sessionUser != null) {
            pageOwner = sessionUser.getId() == board.getUser().getId();
        }

        request.setAttribute("board", board);
        request.setAttribute("pageOwner", pageOwner);
        request.setAttribute("userId", board.getUser().getUsername());
        return "board/detail";
    }

}
