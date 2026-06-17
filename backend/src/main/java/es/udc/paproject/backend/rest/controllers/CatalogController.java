package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.services.CatalogService;
import es.udc.paproject.backend.model.exceptions.SessionAlreadyStartedException;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.rest.dtos.MovieDto;
import es.udc.paproject.backend.rest.dtos.SessionDto;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static es.udc.paproject.backend.rest.dtos.MovieConversor.toMovieDto;
import static es.udc.paproject.backend.rest.dtos.SessionConversor.toSessionDtos;
import static es.udc.paproject.backend.rest.dtos.SessionConversor.toSessionDto;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/movies/{id}")
    public MovieDto findMovieById(@PathVariable Long id) throws InstanceNotFoundException {
        return toMovieDto(catalogService.findMovieById(id));
    }

    @GetMapping("/billboard")
    public List<SessionDto> getBillboard(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusDays(6))) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid date");
        }
        return toSessionDtos(catalogService.findSessionsByDate(date));
    }

    @GetMapping("/sessions/{id}")
    public SessionDto findSessionById(@PathVariable Long id) throws InstanceNotFoundException, SessionAlreadyStartedException {
        Session session = catalogService.findSessionById(id);
        if (!session.getDate().isAfter(LocalDateTime.now())) {
            throw new SessionAlreadyStartedException(session.getId(), session.getDate());
        }
        return toSessionDto(session);
    }

}
