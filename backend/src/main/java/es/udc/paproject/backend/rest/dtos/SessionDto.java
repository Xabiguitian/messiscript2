package es.udc.paproject.backend.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionDto {

    private Long id;
    private Long movieId;
    private String movieTitle;
    private String roomName;
    private LocalDateTime date;
    private BigDecimal price;
    private int emptySeats;

    public SessionDto() {}

    public SessionDto(Long id, Long movieId, String movieTitle, String roomName, LocalDateTime date, BigDecimal price,
                      int emptySeats) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.roomName = roomName;
        this.date = date;
        this.price = price;
        this.emptySeats = emptySeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public void setEmptySeats(int emptySeats) {
        this.emptySeats = emptySeats;
    }
}
