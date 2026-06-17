package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Purchase;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.NotEnoughSeatsException;
import es.udc.paproject.backend.model.exceptions.SessionAlreadyStartedException;
import es.udc.paproject.backend.model.exceptions.IncorrectPurchaseCreditCardException;
import es.udc.paproject.backend.model.exceptions.TicketsAlreadyDeliveredException;
import es.udc.paproject.backend.model.exceptions.MaxTicketsExceededException;

public interface ShoppingService {

    Purchase buyTickets(Long userId, Long sessionId, int tickets, String creditCard)
            throws InstanceNotFoundException, NotEnoughSeatsException, SessionAlreadyStartedException, MaxTicketsExceededException;

    Block<Purchase> findPurchases(Long userId, int page, int size);

    Purchase deliverTickets(Long purchaseId, String creditCard)
            throws InstanceNotFoundException, IncorrectPurchaseCreditCardException, TicketsAlreadyDeliveredException, SessionAlreadyStartedException;

}
