package org.soccer.soccerplayer.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.soccer.soccerplayer.dto.SoccerPlayerDTO;
import org.soccer.soccerplayer.entity.SoccerPlayer;
import org.soccer.soccerplayer.repository.SoccerPlayerRepository;

import java.util.List;

@ApplicationScoped
public class SoccerPlayerService {

    @Inject
    SoccerPlayerRepository soccerPlayerRepository;

    @Transactional
    public void create(SoccerPlayerDTO soccerPlayerDTO) {
        SoccerPlayer soccerPlayer = new SoccerPlayer();
        soccerPlayer.setName(soccerPlayerDTO.name());
        soccerPlayer.setSurname(soccerPlayerDTO.surname());
        soccerPlayer.setAge(soccerPlayerDTO.age());
        soccerPlayer.setTeam(soccerPlayerDTO.team());

        soccerPlayerRepository.persist(soccerPlayer);
    }

    public SoccerPlayer getById(Long id) {
        return soccerPlayerRepository.findByIdOptional(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Soccer player with id " + id + " not found")
                );
    }

    public List<SoccerPlayer> getAll() {
        return soccerPlayerRepository.listAll();
    }

    @Transactional
    public void update(Long id, SoccerPlayerDTO soccerPlayerDTO) {
        soccerPlayerRepository.findByIdOptional(id)
                .ifPresent(player -> {
                    player.setName(soccerPlayerDTO.name());
                    player.setSurname(soccerPlayerDTO.surname());
                    player.setAge(soccerPlayerDTO.age());
                    player.setTeam(soccerPlayerDTO.team());

                    soccerPlayerRepository.persist(player);
                });
    }

    @Transactional
    public void delete(Long id) {
        soccerPlayerRepository.deleteById(id);
    }

}
