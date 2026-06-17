package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Session;

import java.util.List;
import java.util.stream.Collectors;

public class SessionConversor {

    private SessionConversor() {}

    public static SessionDto toSessionDto(Session session) {
        return new SessionDto(session.getId(), session.getMovie().getId(), session.getMovie().getTitle(),
                session.getRoom().getName(), session.getDate(), session.getPrice(), session.getEmptySeats());
    }

    public static List<SessionDto> toSessionDtos(List<Session> sessions) {
        return sessions.stream().map(SessionConversor::toSessionDto).collect(Collectors.toList());
    }

}
