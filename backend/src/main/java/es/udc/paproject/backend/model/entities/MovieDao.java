package es.udc.paproject.backend.model.entities;

import org.springframework.data.repository.CrudRepository;

public interface MovieDao extends CrudRepository<Movie, Long> {
    //Futuros metodos de consulta
}