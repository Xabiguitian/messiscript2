package es.udc.paproject.backend.model.exceptions;

@SuppressWarnings("serial")
public class NotEnoughSeatsException extends Exception {

    private int requestedTickets;
    private int availableSeats;

    public NotEnoughSeatsException(int requestedTickets, int availableSeats) {
        super("Not enough seats available. Requested: " + requestedTickets + ", Available: " + availableSeats);
        this.requestedTickets = requestedTickets;
        this.availableSeats = availableSeats;
    }

    public int getRequestedTickets() {
        return requestedTickets;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }
}
