package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.Purchase;
import es.udc.paproject.backend.model.exceptions.*;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.ShoppingService;
import es.udc.paproject.backend.rest.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static es.udc.paproject.backend.rest.dtos.PurchaseConversor.toPurchaseDto;
import static es.udc.paproject.backend.rest.dtos.PurchaseConversor.toPurchaseDtos;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    @PostMapping("/purchases")
    public PurchaseDto buyTickets(@RequestAttribute Long userId, @Validated @RequestBody BuyTicketsDto params)
            throws InstanceNotFoundException, NotEnoughSeatsException, SessionAlreadyStartedException, MaxTicketsExceededException {
        
        Purchase purchase = shoppingService.buyTickets(userId, params.getSessionId(), params.getQuantity(),
                params.getCreditCard());
        
        return toPurchaseDto(purchase);
    }

    @GetMapping("/purchases")
    public BlockDto<PurchaseDto> findPurchases(@RequestAttribute Long userId, @RequestParam(defaultValue = "0") int page) {
        
        Block<Purchase> purchaseBlock = shoppingService.findPurchases(userId, page, 2);
        
        return new BlockDto<>(toPurchaseDtos(purchaseBlock.getItems()), purchaseBlock.getExistMoreItems());
    }

    @PostMapping("/purchases/{purchaseId}/deliver")
    public PurchaseDto deliverTickets(@PathVariable Long purchaseId, @Validated @RequestBody DeliverTicketsDto params)
            throws InstanceNotFoundException, IncorrectPurchaseCreditCardException, TicketsAlreadyDeliveredException,
            SessionAlreadyStartedException {
        
        Purchase purchase = shoppingService.deliverTickets(purchaseId, params.getCreditCardNumber());
        
        return toPurchaseDto(purchase);
    }

}
