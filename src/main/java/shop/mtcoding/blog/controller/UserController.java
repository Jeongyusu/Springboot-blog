package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository UserRepository;

    @PostMapping("/join")
    public String join(JoinDTO joinDTO) throws IOException {

        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";

        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";

        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";

        }

        try {
            UserRepository.save(joinDTO);
        } catch (Exception e) {
            return "redirect:/50x";

        }

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

}
