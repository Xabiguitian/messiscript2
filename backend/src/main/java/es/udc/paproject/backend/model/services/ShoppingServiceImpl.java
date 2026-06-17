package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Purchase;
import es.udc.paproject.backend.model.entities.PurchaseDao;
import es.udc.paproject.backend.model.entities.Session;
import es.udc.paproject.backend.model.entities.SessionDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.IncorrectPurchaseCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.MaxTicketsExceededException;
import es.udc.paproject.backend.model.exceptions.NotEnoughSeatsException;
import es.udc.paproject.backend.model.exceptions.SessionAlreadyStartedException;
import es.udc.paproject.backend.model.exceptions.TicketsAlreadyDeliveredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ShoppingServiceImpl implements ShoppingService {

    @Autowired
    private PermissionChecker permissionChecker;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private PurchaseDao purchaseDao;

    @Override
    public Purchase buyTickets(Long userId, Long sessionId, int tickets, String creditCard)
            throws InstanceNotFoundException, NotEnoughSeatsException, SessionAlreadyStartedException, MaxTicketsExceededException {

        User user = permissionChecker.checkUser(userId);

        if (tickets > 10) {
            throw new MaxTicketsExceededException(tickets, 10);
        }

        Session session = findSession(sessionId);

        if (!session.getDate().isAfter(LocalDateTime.now())) {
            throw new SessionAlreadyStartedException(sessionId, session.getDate());
        }

        if (session.getEmptySeats() < tickets) {
            throw new NotEnoughSeatsException(tickets, session.getEmptySeats());
        }

        session.setEmptySeats(session.getEmptySeats() - tickets);

        Purchase purchase = new Purchase(user, session, tickets, creditCard, LocalDateTime.now());

        return purchaseDao.save(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Block<Purchase> findPurchases(Long userId, int page, int size) {

        Slice<Purchase> slice = purchaseDao.findByUserIdOrderByDateDesc(userId,
                PageRequest.of(page, size));

        return new Block<>(slice.getContent(), slice.hasNext());
    }

    @Override
    public Purchase deliverTickets(Long purchaseId, String creditCard)
            throws InstanceNotFoundException, IncorrectPurchaseCreditCardException, TicketsAlreadyDeliveredException,
            SessionAlreadyStartedException {

        Purchase purchase = findPurchase(purchaseId);
        Session session = purchase.getSession();

        if (!session.getDate().isAfter(LocalDateTime.now())) {
            throw new SessionAlreadyStartedException(session.getId(), session.getDate());
        }

        if (!purchase.getCreditCard().equals(creditCard)) {
            throw new IncorrectPurchaseCreditCardException(purchaseId, creditCard);
        }

        if (purchase.isDelivered()) {
            throw new TicketsAlreadyDeliveredException(purchaseId);
        }

        purchase.setDelivered(true);

        return purchase;
    }

    private Session findSession(Long sessionId) throws InstanceNotFoundException {

        Optional<Session> session = sessionDao.findById(sessionId);

        if (!session.isPresent()) {
            throw new InstanceNotFoundException("project.entities.session", sessionId);
        }

        return session.get();
    }

    private Purchase findPurchase(Long purchaseId) throws InstanceNotFoundException {

        Optional<Purchase> purchase = purchaseDao.findById(purchaseId);

        if (!purchase.isPresent()) {
            throw new InstanceNotFoundException("project.entities.purchase", purchaseId);
        }

        return purchase.get();
    }
}

