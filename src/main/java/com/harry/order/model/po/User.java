package com.harry.order.model.po;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;

@BatchSize(size = 64)
@Entity
@Table(name = "t_user",
        indexes = {
                @Index(name = "uk_username", columnList = "username", unique = true),
                @Index(name = "idx_phone", columnList = "phone")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(length = 50)
    private String nickname;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    /** BCrypt Hash Value Password */
    @Column(nullable = false, length = 100)
    private String password;

    /** 交给数据库的 DEFAULT CURRENT_TIMESTAMP 生成 */
    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
