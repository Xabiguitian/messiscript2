package es.udc.paproject.backend.test.rest.controllers;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.rest.common.JwtGenerator;
import es.udc.paproject.backend.rest.common.JwtInfo;
import es.udc.paproject.backend.rest.dtos.BuyTicketsDto;
import es.udc.paproject.backend.rest.dtos.DeliverTicketsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ShoppingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private PurchaseDao purchaseDao;

    private User signUpViewer(String userName) {
        User user = new User(userName, "password", "firstName", "lastName", userName + "@test.com");
        user.setRole(User.RoleType.VIEWER);
        return userDao.save(user);
    }

    private User signUpTicketSeller(String userName) {
        User user = new User(userName, "password", "firstName", "lastName", userName + "@test.com");
        user.setRole(User.RoleType.TICKET_SELLER);
        return userDao.save(user);
    }

    private String generateToken(User user) {
        return jwtGenerator.generate(new JwtInfo(user.getId(), user.getRole().toString()));
    }

    @Test
    public void testBuyTickets() throws Exception {
        User user = signUpViewer("user");
        String token = generateToken(user);

        Movie movie = new Movie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Room room = new Room("Room 1", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        BuyTicketsDto params = new BuyTicketsDto();
        params.setSessionId(session.getId());
        params.setQuantity(2);
        params.setCreditCard("1234567812345678");

        mockMvc.perform(post("/shopping/purchases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.tickets").value(2))
                .andExpect(jsonPath("$.totalPrice").value(16.0));
    }

    @Test
    public void testFindPurchases() throws Exception {
        User user = signUpViewer("user");
        String token = generateToken(user);

        Movie movie = new Movie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Room room = new Room("Room 1", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = new Purchase(user, session, 2, "1234567812345678", LocalDateTime.now());
        purchaseDao.save(purchase);

        mockMvc.perform(get("/shopping/purchases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(purchase.getId()));
    }

    @Test
    public void testDeliverTickets() throws Exception {
        User viewer = signUpViewer("viewer");
        User seller = signUpTicketSeller("seller");
        String token = generateToken(seller);

        Movie movie = new Movie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Room room = new Room("Room 1", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusHours(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        String creditCard = "1234567812345678";
        Purchase purchase = new Purchase(viewer, session, 2, creditCard, LocalDateTime.now());
        purchaseDao.save(purchase);

        DeliverTicketsDto params = new DeliverTicketsDto();
        params.setCreditCardNumber(creditCard);

        mockMvc.perform(post("/shopping/purchases/" + purchase.getId() + "/deliver")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(purchase.getId()))
                .andExpect(jsonPath("$.delivered").value(true));
    }

    @Test
    public void testDeliverTicketsInvalidCreditCard() throws Exception {
        User viewer = signUpViewer("viewer");
        User seller = signUpTicketSeller("seller");
        String token = generateToken(seller);

        Movie movie = new Movie("Movie 1", "Summary 1", 120);
        movieDao.save(movie);

        Room room = new Room("Room 1", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusHours(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = new Purchase(viewer, session, 2, "1234567812345678", LocalDateTime.now());
        purchaseDao.save(purchase);

        DeliverTicketsDto params = new DeliverTicketsDto();
        params.setCreditCardNumber("8765432187654321");

        mockMvc.perform(post("/shopping/purchases/" + purchase.getId() + "/deliver")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest());
    }
}
