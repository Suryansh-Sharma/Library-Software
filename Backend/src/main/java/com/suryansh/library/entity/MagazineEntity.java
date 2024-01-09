package com.suryansh.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "magazines")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MagazineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate publishedDate;
    private String editor;
}
