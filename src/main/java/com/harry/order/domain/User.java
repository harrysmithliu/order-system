package com.harry.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    /** 交给数据库的 DEFAULT CURRENT_TIMESTAMP 生成 */
    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createTime;
}
