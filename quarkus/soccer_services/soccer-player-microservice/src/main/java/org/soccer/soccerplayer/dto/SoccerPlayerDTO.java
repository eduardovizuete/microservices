package org.soccer.soccerplayer.dto;

import jakarta.validation.constraints.NotBlank;

public record SoccerPlayerDTO(@NotBlank String name,
                              @NotBlank String surname,
                              @NotBlank int age,
                              @NotBlank String team) {
}
