package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "user_tb")
@Entity // ddl-auto가 create 일때 table이 만들어짐
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY -> DB마다 전략이 바뀐다. 오라클 -> sequence전략
    private Integer id;
    @Column(nullable = false, length = 20, unique = true)
    private String username;
    @Column(nullable = false, length = 20, unique = true)
    private String password;
    @Column(nullable = false, length = 20, unique = true)
    private String email;

}
