package org.soccer.footballmanager.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.soccer.footballmanager.domain.FootballManager;
import org.soccer.footballmanager.service.FootballManagerService;

import java.net.URI;
import java.util.Objects;

@Path("/api/v1/footballmanagers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "footballManager", description = "Football Manager Operations")
@AllArgsConstructor
@Slf4j
public class FootballManagerResource {

    private final FootballManagerService fmService;

    @GET
    @APIResponse(
            responseCode = "200",
            description = "Get All Football Managers",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = FootballManager.class)
            )
    )
    public Response get() {
        return Response.ok(fmService.findAll()).build();
    }

    @GET
    @Path("/{fmId}")
    @APIResponse(
            responseCode = "200",
            description = "Get FootballManager by fmId",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = FootballManager.class)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "FootballManager does not exist for fmId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response getById(@Parameter(name = "fmId", required = true) @PathParam("fmId") Long fmId) {
        return fmService.findById(fmId)
                .map(fm -> Response.ok(fm).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @APIResponse(
            responseCode = "201",
            description = "FootballManager Created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = FootballManager.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid FootballManager",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "FootballManager already exists for fmId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response post(@NotNull @Valid FootballManager fm, @Context UriInfo uriInfo) {
        FootballManager created = fmService.create(fm);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(created.getId())).build();
        return Response.created(uri).entity(created).build();
    }

    @PUT
    @Path("/{fmId}")
    @APIResponse(
            responseCode = "204",
            description = "FootballManager updated",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.OBJECT, implementation = FootballManager.class)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid FootballManager",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "FootballManager object does not have fmId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "400",
            description = "Path variable fmId does not match FootballManager.fmId",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @APIResponse(
            responseCode = "404",
            description = "No FootballManager found for fmId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response put(@Parameter(name = "fmId", required = true) @PathParam("fmId") Long fmId, @NotNull @Valid FootballManager fm) {
        if (!Objects.equals(fmId, fm.getId())) {
            throw new WebApplicationException("Path variable fmId does not match FootballManager.fmId", Response.Status.BAD_REQUEST);
        }
        fmService.update(fm);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{fmId}")
    @APIResponse(
            responseCode = "204",
            description = "FootballManager deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "No FootballManager found for fmId provided",
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    public Response delete(@Parameter(name = "fmId", required = true) @PathParam("fmId") Long fmId) {
        if (fmService.findById(fmId).isEmpty()) {
            throw new WebApplicationException(String.format("No FootballManager found for fmId[%s]", fmId), Response.Status.NOT_FOUND);
        }
        fmService.delete(fmId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
