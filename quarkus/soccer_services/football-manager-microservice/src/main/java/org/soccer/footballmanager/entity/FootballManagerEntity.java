package org.soccer.footballmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity(name = "FootballManager")
@Table(name = "FOOTBALL_MANAGER")
@Data
public class FootballManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @NotEmpty(message = "{FootballManager.name.required}")
    private String name;

    @Column(name = "SURNAME")
    @NotEmpty(message = "{FootballManager.surname.required}")
    private String surname;

    @Column(name = "AGE")
    @NotNull(message = "{FootballManager.age.required}")
    private Integer age;

    @Column(name = "NICKNAME")
    @NotEmpty(message = "{FootballManager.nickname.required}")
    private String nickname;

}
