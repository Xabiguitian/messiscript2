import * as actionTypes from './actionTypes';
import backend from '../../backend';

export const buyTicketsCompleted = purchase => ({
    type: actionTypes.BUY_TICKETS_COMPLETED,
    purchase
});

export const buyTickets = (sessionId, quantity, creditCard, onSuccess, onErrors) => dispatch =>
    backend.shoppingService.buyTickets(sessionId, quantity, creditCard).then(response => {
        if (response.ok) {
            dispatch(buyTicketsCompleted(response.payload));
            onSuccess();
        } else {
            onErrors(response.payload);
        }
    });

export const clearPurchase = () => ({
    type: actionTypes.CLEAR_PURCHASE
});

export const findPurchasesCompleted = purchaseSearch => ({
    type: actionTypes.FIND_PURCHASES_COMPLETED,
    purchaseSearch
});

export const findPurchases = criteria => dispatch => {

    dispatch(clearPurchaseSearch());
    backend.shoppingService.findPurchases(criteria).then(response => {
        if (response.ok) {
            dispatch(findPurchasesCompleted({criteria, result: response.payload}));
        }
    });

}

export const clearPurchaseSearch = () => ({
    type: actionTypes.CLEAR_PURCHASE_SEARCH
});

export const deliverTicketsCompleted = delivery => ({
    type: actionTypes.DELIVER_TICKETS_COMPLETED,
    delivery
});

export const deliverTickets = (purchaseId, creditCardNumber, onSuccess, onErrors) => dispatch =>
    backend.shoppingService.deliverTickets(purchaseId, creditCardNumber).then(response => {
        if (response.ok) {
            dispatch(deliverTicketsCompleted(response.payload));
            onSuccess();
        } else {
            onErrors(response.payload);
        }
    });

export const clearDelivery = () => ({
    type: actionTypes.CLEAR_DELIVERY
});
