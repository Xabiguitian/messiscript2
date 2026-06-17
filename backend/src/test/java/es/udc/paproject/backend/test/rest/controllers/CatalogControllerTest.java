package es.udc.paproject.backend.test.rest.controllers;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Room;
import es.udc.paproject.backend.model.entities.RoomDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private SessionDao sessionDao;

    @Test
    public void testFindMovieById() throws Exception {
        Movie movie = new Movie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        mockMvc.perform(get("/catalog/movies/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movie.getId()))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.summary").value(movie.getSummary()))
                .andExpect(jsonPath("$.duration").value(movie.getDuration()));
    }

    @Test
    public void testFindMovieByIdNotFound() throws Exception {
        mockMvc.perform(get("/catalog/movies/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBillboard() throws Exception {
        Movie movie = new Movie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Room room = new Room("Room 1", 100);
        roomDao.save(room);

        LocalDate date = LocalDate.now().plusDays(1);
        Session session = new Session(movie, room, date.atTime(18, 0), new BigDecimal("8.00"));
        sessionDao.save(session);

        mockMvc.perform(get("/catalog/billboard?date=" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(session.getId()))
                .andExpect(jsonPath("$[0].movieTitle").value(movie.getTitle()))
                .andExpect(jsonPath("$[0].roomName").value(room.getName()));
    }

}
