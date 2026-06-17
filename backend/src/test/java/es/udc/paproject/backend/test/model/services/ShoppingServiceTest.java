package es.udc.paproject.backend.test.model.services;

import es.udc.paproject.backend.model.entities.Movie;
import es.udc.paproject.backend.model.entities.MovieDao;
import es.udc.paproject.backend.model.entities.Purchase;
import es.udc.paproject.backend.model.entities.PurchaseDao;
import es.udc.paproject.backend.model.entities.Room;
import es.udc.paproject.backend.model.entities.RoomDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;
import es.udc.paproject.backend.model.exceptions.IncorrectPurchaseCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.NotEnoughSeatsException;
import es.udc.paproject.backend.model.exceptions.SessionAlreadyStartedException;
import es.udc.paproject.backend.model.exceptions.TicketsAlreadyDeliveredException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.ShoppingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ShoppingServiceTest {

    @Autowired
    private ShoppingService shoppingService;

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

    private User createUser(String userName) {
        User user = new User(userName, "secret", "First", "Last", userName + "@test.com");
        user.setRole(User.RoleType.VIEWER);
        return user;
    }

    @Test
    public void testBuyTicketsOk() throws Exception {

        User user = createUser("buyer");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 10);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = shoppingService.buyTickets(user.getId(), session.getId(), 2, "1111222233334444");

        assertNotNull(purchase.getId());
        assertEquals(user.getId(), purchase.getUser().getId());
        assertEquals(session.getId(), purchase.getSession().getId());
        assertEquals(2, purchase.getTickets());
        assertFalse(purchase.isDelivered());

        Session updatedSession = sessionDao.findById(session.getId()).orElseThrow();
        assertEquals(8, updatedSession.getEmptySeats());

        Purchase persistedPurchase = purchaseDao.findById(purchase.getId()).orElseThrow();
        assertEquals(purchase.getId(), persistedPurchase.getId());
    }

    @Test
    public void testBuyTicketsUserNotFound() {
        assertThrows(InstanceNotFoundException.class,
                () -> shoppingService.buyTickets(-1L, -1L, 2, "1111"));
    }

    @Test
    public void testBuyTicketsSessionNotFound() {

        User user = createUser("buyer2");
        userDao.save(user);

        assertThrows(InstanceNotFoundException.class,
                () -> shoppingService.buyTickets(user.getId(), -1L, 2, "1111"));
    }

    @Test
    public void testBuyTicketsNotEnoughSeats() throws Exception {

        User user = createUser("buyer3");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 5);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        assertThrows(NotEnoughSeatsException.class,
                () -> shoppingService.buyTickets(user.getId(), session.getId(), 6, "1111"));
    }

    @Test
    public void testBuyTicketsExactlyAvailableSeats() throws Exception {

        User user = createUser("buyer5");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 3);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = shoppingService.buyTickets(user.getId(), session.getId(), 3, "1111222233334444");

        assertEquals(3, purchase.getTickets());
        Session updatedSession = sessionDao.findById(session.getId()).orElseThrow();
        assertEquals(0, updatedSession.getEmptySeats());
    }

    @Test
    public void testBuyTicketsSessionAlreadyStarted() throws Exception {

        User user = createUser("buyer4");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 5);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().minusMinutes(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        assertThrows(SessionAlreadyStartedException.class,
                () -> shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111"));
    }

    // ---- FUNC-5: findPurchases tests ----

    @Test
    public void testFindPurchasesEmpty() {

        User user = createUser("viewer1");
        userDao.save(user);

        Block<Purchase> block = shoppingService.findPurchases(user.getId(), 0, 10);

        assertEquals(0, block.getItems().size());
        assertFalse(block.getExistMoreItems());
    }

    @Test
    public void testFindPurchasesOrderedByDateDesc() throws Exception {

        User user = createUser("viewer2");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase1 = shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");
        Purchase purchase2 = shoppingService.buyTickets(user.getId(), session.getId(), 2, "1111222233334444");

        Block<Purchase> block = shoppingService.findPurchases(user.getId(), 0, 10);

        assertEquals(2, block.getItems().size());
        assertEquals(purchase2.getId(), block.getItems().get(0).getId());
        assertEquals(purchase1.getId(), block.getItems().get(1).getId());
        assertFalse(block.getExistMoreItems());
    }

    @Test
    public void testFindPurchasesPagination() throws Exception {

        User user = createUser("viewer3");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");
        shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");
        shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");

        Block<Purchase> firstPage = shoppingService.findPurchases(user.getId(), 0, 2);
        assertEquals(2, firstPage.getItems().size());
        assertTrue(firstPage.getExistMoreItems());

        Block<Purchase> secondPage = shoppingService.findPurchases(user.getId(), 1, 2);
        assertEquals(1, secondPage.getItems().size());
        assertFalse(secondPage.getExistMoreItems());
    }

    @Test
    public void testFindPurchasesOnlyReturnsUserPurchases() throws Exception {

        User user1 = createUser("viewer4");
        userDao.save(user1);

        User user2 = createUser("viewer5");
        userDao.save(user2);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        shoppingService.buyTickets(user1.getId(), session.getId(), 1, "1111222233334444");
        shoppingService.buyTickets(user2.getId(), session.getId(), 2, "5555666677778888");

        Block<Purchase> user1Purchases = shoppingService.findPurchases(user1.getId(), 0, 10);
        assertEquals(1, user1Purchases.getItems().size());
        assertEquals(1, user1Purchases.getItems().get(0).getTickets());

        Block<Purchase> user2Purchases = shoppingService.findPurchases(user2.getId(), 0, 10);
        assertEquals(1, user2Purchases.getItems().size());
        assertEquals(2, user2Purchases.getItems().get(0).getTickets());
    }

    @Test
    public void testFindPurchasesOutOfRangePage() throws Exception {

        User user = createUser("viewer6");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");

        Block<Purchase> block = shoppingService.findPurchases(user.getId(), 5, 10);

        assertEquals(0, block.getItems().size());
        assertFalse(block.getExistMoreItems());
    }

    // ---- FUNC-6: deliverTickets tests ----

    @Test
    public void testDeliverTicketsOk() throws Exception {

        User user = createUser("deliver1");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = shoppingService.buyTickets(user.getId(), session.getId(), 2, "1111222233334444");

        Purchase delivered = shoppingService.deliverTickets(purchase.getId(), "1111222233334444");

        assertTrue(delivered.isDelivered());

        Purchase persisted = purchaseDao.findById(purchase.getId()).orElseThrow();
        assertTrue(persisted.isDelivered());
    }

    @Test
    public void testDeliverTicketsPurchaseNotFound() {
        assertThrows(InstanceNotFoundException.class,
                () -> shoppingService.deliverTickets(-1L, "1111"));
    }

    @Test
    public void testDeliverTicketsIncorrectCreditCard() throws Exception {

        User user = createUser("deliver2");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");

        assertThrows(IncorrectPurchaseCreditCardException.class,
                () -> shoppingService.deliverTickets(purchase.getId(), "0000111122223333"));

        Purchase persisted = purchaseDao.findById(purchase.getId()).orElseThrow();
        assertFalse(persisted.isDelivered());
    }

    @Test
    public void testDeliverTicketsAlreadyDelivered() throws Exception {

        User user = createUser("deliver3");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().plusDays(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = shoppingService.buyTickets(user.getId(), session.getId(), 1, "1111222233334444");

        shoppingService.deliverTickets(purchase.getId(), "1111222233334444");

        assertThrows(TicketsAlreadyDeliveredException.class,
                () -> shoppingService.deliverTickets(purchase.getId(), "1111222233334444"));
    }

    @Test
    public void testDeliverTicketsSessionAlreadyStarted() throws Exception {

        User user = createUser("deliver4");
        userDao.save(user);

        Movie movie = new Movie("Movie", "Summary", 120);
        movieDao.save(movie);

        Room room = new Room("Room", 100);
        roomDao.save(room);

        Session session = new Session(movie, room, LocalDateTime.now().minusMinutes(1), new BigDecimal("8.00"));
        sessionDao.save(session);

        Purchase purchase = new Purchase(user, session, 1, "1111222233334444", LocalDateTime.now().minusMinutes(2));
        purchaseDao.save(purchase);

        assertThrows(SessionAlreadyStartedException.class,
                () -> shoppingService.deliverTickets(purchase.getId(), "1111222233334444"));
    }
}
