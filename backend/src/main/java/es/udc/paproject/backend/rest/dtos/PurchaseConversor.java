package es.udc.paproject.backend.rest.dtos;

import es.udc.paproject.backend.model.entities.Purchase;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseConversor {

    private PurchaseConversor() {}

    public static PurchaseDto toPurchaseDto(Purchase purchase) {
        BigDecimal totalPrice = purchase.getSession().getPrice().multiply(new BigDecimal(purchase.getTickets()));
        
        return new PurchaseDto(purchase.getId(), purchase.getSession().getId(), purchase.getUser().getId(),
                purchase.getDate(), purchase.getSession().getDate(), purchase.getTickets(), totalPrice,
                purchase.getCreditCard(), purchase.isDelivered(), 
                purchase.getSession().getMovie().getTitle(),
                purchase.getSession().getRoom().getName());
    }

    public static List<PurchaseDto> toPurchaseDtos(List<Purchase> purchases) {
        return purchases.stream().map(PurchaseConversor::toPurchaseDto).collect(Collectors.toList());
    }

}
