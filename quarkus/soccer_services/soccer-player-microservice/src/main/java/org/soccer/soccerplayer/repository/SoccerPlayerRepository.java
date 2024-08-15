package org.soccer.soccerplayer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.soccer.soccerplayer.entity.SoccerPlayer;

@ApplicationScoped
public class SoccerPlayerRepository implements PanacheRepository<SoccerPlayer> {
}
