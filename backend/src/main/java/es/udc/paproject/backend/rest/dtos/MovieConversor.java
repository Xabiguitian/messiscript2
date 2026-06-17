package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Movie;

public class MovieConversor {

    private MovieConversor() {}

    public static MovieDto toMovieDto(Movie movie) {
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getSummary(), movie.getDuration());
    }

}
