package org.soccer.footballmanager.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soccer.footballmanager.domain.FootballManager;
import org.soccer.footballmanager.entity.FootballManagerEntity;
import org.soccer.footballmanager.mapper.FootballManagerMapper;
import org.soccer.footballmanager.repository.FootballManagerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class FootballManagerService {

    private final FootballManagerRepository footballManagerRepository;
    private final FootballManagerMapper footballManagerMapper;

    public List<FootballManager> findAll() {
        return footballManagerMapper.toDomainList(footballManagerRepository.findAll().list());
    }

    public Optional<FootballManager> findById(long footballManagerId) {
        return footballManagerRepository.findByIdOptional(footballManagerId)
                .map(footballManagerMapper::toDomain);
    }

    @Transactional
    public FootballManager create(@Valid FootballManager footballManager) {
        log.debug("Creating FootballManager: {}", footballManager);

        FootballManagerEntity entity = footballManagerMapper.toEntity(footballManager);
        footballManagerRepository.persist(entity);
        footballManagerMapper.updateDomainFromEntity(entity, footballManager);

        return footballManager;
    }

    @Transactional
    public FootballManager update(@Valid FootballManager footballManager) {
        log.debug("Updating FootballManager: {}", footballManager);

        FootballManagerEntity entity = footballManagerRepository.findById(footballManager.getId());
        footballManagerMapper.updateEntityFromDomain(footballManager, entity);
        footballManagerRepository.persist(entity);
        footballManagerMapper.updateDomainFromEntity(entity, footballManager);

        return footballManager;
    }

    @Transactional
    public void delete(long footballManagerId) {
        footballManagerRepository.deleteById(footballManagerId);
    }

}
