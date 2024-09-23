package com.emazon.mscart.adapters.driven.jpa.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long articleId;
    private Long quantity;
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @Column(name = "update_date", nullable = false)
    private LocalDate updateDate;
}
