package org.soccer.footballmanager.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FootballManager {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotNull
    private Integer age;

    @NotEmpty
    private String nickname;

}
