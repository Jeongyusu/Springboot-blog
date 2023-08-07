package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session; // request는 가방, session은 서랍

    // ResponseEntitiy<> ==> Responsebody를 안붙여도 <>타입의 데이터를 응답. HttpServletResponse
    // 변수가 필요없음.
    @GetMapping("/check")
    public ResponseEntity<String> check(String username) {

        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new ResponseEntity<>("중복됨", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("중복되지 않음", HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping("/test/login")
    public String testLogin() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "로그인이 되지 않았습니다";
        } else {
            return "로그인 됨 : " + sessionUser.getUsername();
        }
    }

    @PostMapping("/login")
    public String Login(LoginDTO loginDTO) {

        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return "redirect:/40x";

        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return "redirect:/40x";

        }

        // 핵심기능
        try {
            User user = userRepository.findByUsernameAndPassword(loginDTO);
            session.setAttribute("sessionUser", user);
            return "redirect:/";

        } catch (Exception e) {
            return "redirect:/exLogin";
        }

    }

    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {
        // validation check (유효성 검사)
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }
        // DB에 해당 username이 있는지 체크해보기
        User user = userRepository.findByUsername(joinDTO.getUsername());
        if (user != null) {
            return "redirect:/50x";
        }
        userRepository.save(joinDTO); // 핵심 기능
        return "redirect:/loginForm";
    }

    // 정상인
    // @PostMapping("/join")
    // public String join(String username, String password, String email) throws
    // IOException {

    // // String username = request.getParameter("username");
    // // String password = request.getParameter("passsword");
    // // String email = request.getParameter("email");

    // // BufferedReader br = request.getReader();
    // // //버퍼가 소비됨
    // // String body = br.readLine();

    // // // 버퍼를 소비해서, 못 꺼냄
    // // String username = request.getParameter("username");

    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);

    // return "redirect:/";
    // }
    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {

        return "user/updateForm";
    }

    @PostMapping("/user/update")
    public String update(String password) {

        userRepository.update(password);

        return "redirect:/";
    }

}
