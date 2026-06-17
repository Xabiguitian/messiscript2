package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface CatalogService {

    List<Session> findSessionsByDate(LocalDate date);

    Movie findMovieById(Long id) throws InstanceNotFoundException;

    Session findSessionById(Long id) throws InstanceNotFoundException;

}
