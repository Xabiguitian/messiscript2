import {appFetch} from './appFetch';

export const buyTickets = (sessionId, quantity, creditCard) =>
    appFetch('POST', '/shopping/purchases', {
        sessionId: Number(sessionId),
        quantity: Number(quantity),
        creditCard
    });

export const findPurchases = ({page}) =>
    appFetch('GET', `/shopping/purchases?page=${page}`);

export const deliverTickets = (purchaseId, creditCardNumber) =>
    appFetch('POST', `/shopping/purchases/${purchaseId}/deliver`, {
        creditCardNumber
    });

