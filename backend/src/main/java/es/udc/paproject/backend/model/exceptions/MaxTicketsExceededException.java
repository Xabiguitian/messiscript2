package es.udc.paproject.backend.model.exceptions;

@SuppressWarnings("serial")
public class MaxTicketsExceededException extends Exception {

    private int requestedTickets;
    private int maxTickets;

    public MaxTicketsExceededException(int requestedTickets, int maxTickets) {
        super("Max tickets exceeded. Requested: " + requestedTickets + ", Max: " + maxTickets);
        this.requestedTickets = requestedTickets;
        this.maxTickets = maxTickets;
    }

    public int getRequestedTickets() {
        return requestedTickets;
    }

    public int getMaxTickets() {
        return maxTickets;
    }
}
