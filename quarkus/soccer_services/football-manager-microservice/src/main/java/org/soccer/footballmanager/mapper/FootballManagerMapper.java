package org.soccer.footballmanager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.soccer.footballmanager.domain.FootballManager;
import org.soccer.footballmanager.entity.FootballManagerEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface FootballManagerMapper {

    FootballManager toDomain(FootballManagerEntity entity);

    FootballManagerEntity toEntity(FootballManager domain);

    List<FootballManager> toDomainList(List<FootballManagerEntity> entities);

    void updateEntityFromDomain(FootballManager domain, @MappingTarget FootballManagerEntity entity);

    void updateDomainFromEntity(FootballManagerEntity entity, @MappingTarget FootballManager domain);

}
