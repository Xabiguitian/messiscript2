package es.udc.paproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseDto {

    private Long id;
    private Long sessionId;
    private Long userId;
    private LocalDateTime date;
    private LocalDateTime sessionDate;
    private int tickets;
    private BigDecimal totalPrice;
    private String creditCard;
    private boolean delivered;
    private String movieTitle;
    private String roomName;

    public PurchaseDto() {}

    public PurchaseDto(Long id, Long sessionId, Long userId, LocalDateTime date, LocalDateTime sessionDate,
                       int tickets, BigDecimal totalPrice, String creditCard, boolean delivered,
                       String movieTitle, String roomName) {
        this.id = id;
        this.sessionId = sessionId;
        this.userId = userId;
        this.date = date;
        this.sessionDate = sessionDate;
        this.tickets = tickets;
        this.totalPrice = totalPrice;
        this.creditCard = creditCard;
        this.delivered = delivered;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
