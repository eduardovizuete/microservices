package org.soccer.footballmanager.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.soccer.footballmanager.entity.FootballManagerEntity;

@ApplicationScoped
public class FootballManagerRepository implements PanacheRepositoryBase<FootballManagerEntity, Long> {
}
