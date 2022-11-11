package org.acme;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/movies")
public class MovieResource {
    public static List<Movie> movies = new ArrayList<>(); //變成物件

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies(){
        return Response.ok(movies).build();
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countMovies(){
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON) // 轉物件 TEXT_PLAIN ->APPLICATION_JSON
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie newMovie){
        movies.add(newMovie);
        return Response.ok(movies).build();
    }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMovie(
            @PathParam("id") Long id,
            @PathParam("title") String title) {
        movies = movies.stream().map(movie -> {
            if(movie.getId().equals(id)) {
                movie.setTitle(title);
            }
            return movie;
        }).collect(Collectors.toList());
        return Response.ok(movies).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(
            @PathParam("id") Long id) {
       
        Optional<Movie> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id))
        .findFirst();
        boolean removed = false;
        if(movieToDelete.isPresent()) {
            removed = movies.remove(movieToDelete.get());
        }
        if(removed) {
            return Response.noContent().build();
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build(); //失敗發送帶有狀態的code-BAD REQUEST    
    }
    
}
