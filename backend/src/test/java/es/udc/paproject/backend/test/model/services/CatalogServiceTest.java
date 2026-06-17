package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Room;
import es.udc.paproject.backend.model.entities.RoomDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.services.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CatalogServiceTest {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private SessionDao sessionDao;

    private Movie createMovie(String title, String summary, int duration) {
        return new Movie(title, summary, duration);
    }

    private Room createRoom(String name, int capacity) {
        return new Room(name, capacity);
    }

    private Session createSession(Movie movie, Room room, LocalDateTime date, BigDecimal price) {
        return new Session(movie, room, date, price);
    }

    @Test
    public void testFindSessionsByDate() {
        Movie movie1 = createMovie("B Movie", "Summary B", 120);
        Movie movie2 = createMovie("A Movie", "Summary A", 90);
        Movie movie3 = createMovie("C Movie", "Summary C", 110);
        movieDao.saveAll(Arrays.asList(movie1, movie2, movie3));

        Room room1 = createRoom("Room 1", 100);
        roomDao.save(room1);

        LocalDate targetDate = LocalDate.now().plusDays(1);
        LocalDateTime date1 = targetDate.atTime(18, 0);
        LocalDateTime date2 = targetDate.atTime(20, 0);
        LocalDateTime date3 = targetDate.atTime(22, 0);
        LocalDateTime dateOtherDay = targetDate.plusDays(1).atTime(18, 0);

        Session session1 = createSession(movie1, room1, date2, new BigDecimal("8.00"));
        Session session2 = createSession(movie1, room1, date1, new BigDecimal("8.00"));
        Session session3 = createSession(movie2, room1, date3, new BigDecimal("8.00"));
        Session session4 = createSession(movie3, room1, dateOtherDay, new BigDecimal("8.00"));

        sessionDao.saveAll(Arrays.asList(session1, session2, session3, session4));

        List<Session> sessions = catalogService.findSessionsByDate(targetDate);

        assertEquals(3, sessions.size());
        assertEquals(session3.getId(), sessions.get(0).getId());
        assertEquals(session2.getId(), sessions.get(1).getId());
        assertEquals(session1.getId(), sessions.get(2).getId());
    }

    @Test
    public void testFindSessionsByDateDoesNotReturnStartedSessions() {
        Movie movie1 = createMovie("A Movie", "Summary A", 90);
        Movie movie2 = createMovie("B Movie", "Summary B", 120);
        movieDao.saveAll(Arrays.asList(movie1, movie2));

        Room room1 = createRoom("Room 1", 100);
        roomDao.save(room1);

        LocalDate targetDate = LocalDate.now();
        LocalDateTime alreadyStarted = LocalDateTime.now().minusMinutes(5);
        LocalDateTime upcoming = LocalDateTime.now().plusMinutes(10);

        Session startedSession = createSession(movie1, room1, alreadyStarted, new BigDecimal("8.00"));
        Session upcomingSession = createSession(movie2, room1, upcoming, new BigDecimal("8.00"));
        sessionDao.saveAll(Arrays.asList(startedSession, upcomingSession));

        List<Session> sessions = catalogService.findSessionsByDate(targetDate);

        assertEquals(1, sessions.size());
        assertEquals(upcomingSession.getId(), sessions.get(0).getId());
    }

    @Test
    public void testFindMovieById() throws InstanceNotFoundException {
        Movie movie = createMovie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Movie foundMovie = catalogService.findMovieById(movie.getId());
        assertEquals(movie.getId(), foundMovie.getId());
        assertEquals(movie.getTitle(), foundMovie.getTitle());
    }

    @Test
    public void testFindMovieByIdNotFound() {
        assertThrows(InstanceNotFoundException.class, () -> catalogService.findMovieById(-1L));
    }

    @Test
    public void testFindSessionById() throws InstanceNotFoundException {
        Movie movie = createMovie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Room room = createRoom("Room 1", 100);
        roomDao.save(room);

        LocalDateTime date = LocalDateTime.now();
        BigDecimal price = new BigDecimal("8.00");
        Session session = createSession(movie, room, date, price);
        sessionDao.save(session);

        Session foundSession = catalogService.findSessionById(session.getId());

        assertEquals(session.getId(), foundSession.getId());
        assertEquals(movie.getId(), foundSession.getMovie().getId());
        assertEquals(movie.getTitle(), foundSession.getMovie().getTitle());
        assertEquals(movie.getSummary(), foundSession.getMovie().getSummary());
        assertEquals(movie.getDuration(), foundSession.getMovie().getDuration());
        assertEquals(room.getId(), foundSession.getRoom().getId());
        assertEquals(room.getName(), foundSession.getRoom().getName());
        assertEquals(room.getCapacity(), foundSession.getRoom().getCapacity());
        assertEquals(date, foundSession.getDate());
        assertEquals(price.compareTo(foundSession.getPrice()), 0);
        assertEquals(room.getCapacity(), foundSession.getEmptySeats());
    }

    @Test
    public void testFindSessionByIdNotFound() {
        assertThrows(InstanceNotFoundException.class, () -> catalogService.findSessionById(-1L));
    }
}
