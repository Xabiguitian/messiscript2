package es.udc.paproject.backend.model.exceptions;

@SuppressWarnings("serial")
public class TicketsAlreadyDeliveredException extends Exception {

    private Long purchaseId;

    public TicketsAlreadyDeliveredException(Long purchaseId) {
        super("Tickets for purchase " + purchaseId + " have already been delivered");
        this.purchaseId = purchaseId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }
}
