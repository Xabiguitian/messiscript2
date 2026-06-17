package es.udc.paproject.backend.model.entities;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseDao extends CrudRepository<Purchase, Long> {

    Slice<Purchase> findByUserIdOrderByDateDesc(Long userId, Pageable pageable);

}