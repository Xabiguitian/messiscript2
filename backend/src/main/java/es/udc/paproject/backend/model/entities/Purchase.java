package es.udc.paproject.backend.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Purchase {

    private Long id;
    private User user;
    private Session session;
    private int tickets;
    private String creditCard;
    private LocalDateTime date;
    private boolean delivered;

    public Purchase() {}

    public Purchase(User user, Session session, int tickets, String creditCard, LocalDateTime date) {
        this.user = user;
        this.session = session;
        this.tickets = tickets;
        this.creditCard = creditCard;
        this.date = date;
        this.delivered = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sessionId")
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    public int getTickets() { return tickets; }
    public void setTickets(int tickets) { this.tickets = tickets; }

    public String getCreditCard() { return creditCard; }
    public void setCreditCard(String creditCard) { this.creditCard = creditCard; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }
}