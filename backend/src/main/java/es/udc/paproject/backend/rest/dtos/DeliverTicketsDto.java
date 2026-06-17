package es.udc.paproject.backend.rest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DeliverTicketsDto {

    private String creditCardNumber;

    public DeliverTicketsDto() {}

    @NotNull
    @Size(min=16, max=16)
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }
}
