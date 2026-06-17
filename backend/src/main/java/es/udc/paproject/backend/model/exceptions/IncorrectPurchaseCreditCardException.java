package es.udc.paproject.backend.model.exceptions;

@SuppressWarnings("serial")
public class IncorrectPurchaseCreditCardException extends Exception {

    private Long purchaseId;
    private String creditCard;

    public IncorrectPurchaseCreditCardException(Long purchaseId, String creditCard) {
        super("Incorrect credit card " + creditCard + " for purchase " + purchaseId);
        this.purchaseId = purchaseId;
        this.creditCard = creditCard;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public String getCreditCard() {
        return creditCard;
    }
}
