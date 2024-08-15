package org.soccer.soccerplayer.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.soccer.soccerplayer.dto.SoccerPlayerDTO;
import org.soccer.soccerplayer.service.SoccerPlayerService;

@Path("/api/v1/soccerplayers")
public class SoccerPlayerResource {

    @Inject
    SoccerPlayerService soccerPlayerService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(SoccerPlayerDTO soccerPlayerDTO) {
        soccerPlayerService.create(soccerPlayerDTO);

        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(soccerPlayerService.getById(id)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(soccerPlayerService.getAll()).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response update(@PathParam("id") Long id, SoccerPlayerDTO soccerPlayerDTO) {
        soccerPlayerService.update(id, soccerPlayerDTO);

        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        soccerPlayerService.delete(id);

        return Response.ok().build();
    }


}
