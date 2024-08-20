package org.soccer.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.soccer.entity.TeamEntity;
import org.soccer.repository.TeamRepository;

import java.util.List;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    TeamRepository teamRepository;

    @POST
    @Path("/team")
    public String createTeam(TeamEntity team) {
        return teamRepository.add(team);
    }

    @GET
    @Path("/team")
    public List<TeamEntity> getTeams() {
        return teamRepository.getTeams();
    }

    @PUT
    @Path("/team/{id}")
    public void updateTeam(@PathParam("id") String id, TeamEntity team) {
        teamRepository.updateTeam(id, team);
    }

    @DELETE
    @Path("/team/{id}")
    public long deleteTeam(@PathParam("id") String id) {
        return teamRepository.deleteTeam(id);
    }

}
