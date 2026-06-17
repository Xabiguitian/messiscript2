package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private MovieDao movieDao;

    @Override
    public List<Session> findSessionsByDate(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        return sessionDao.findByDateBetweenAndDateAfterOrderByMovieTitleAscDateAsc(
                startDate, endDate, LocalDateTime.now());
    }

    @Override
    public Movie findMovieById(Long id) throws InstanceNotFoundException {
        Optional<Movie> movie = movieDao.findById(id);

        if (!movie.isPresent()) {
            throw new InstanceNotFoundException("project.entities.movie", id);
        }

        return movie.get();
    }

    @Override
    public Session findSessionById(Long id) throws InstanceNotFoundException {
        Optional<Session> session = sessionDao.findById(id);

        if (!session.isPresent()) {
            throw new InstanceNotFoundException("project.entities.session", id);
        }

        return session.get();
    }
}
