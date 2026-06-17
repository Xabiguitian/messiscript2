package es.udc.paproject.backend.model.exceptions;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class SessionAlreadyStartedException extends Exception {

    private Long sessionId;
    private LocalDateTime sessionDate;

    public SessionAlreadyStartedException(Long sessionId, LocalDateTime sessionDate) {
        super("Session with id " + sessionId + " has already started at " + sessionDate);
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }
}
