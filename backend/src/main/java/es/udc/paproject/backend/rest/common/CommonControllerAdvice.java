package es.udc.paproject.backend.rest.common;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.paproject.backend.model.exceptions.*;

@ControllerAdvice
public class CommonControllerAdvice {
	
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private final static String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";
	private final static String PERMISSION_EXCEPTION_CODE = "project.exceptions.PermissionException";
	private final static String NOT_ENOUGH_SEATS_EXCEPTION_CODE = "project.exceptions.NotEnoughSeatsException";
	private final static String SESSION_ALREADY_STARTED_EXCEPTION_CODE = "project.exceptions.SessionAlreadyStartedException";
	private final static String INCORRECT_PURCHASE_CREDIT_CARD_EXCEPTION_CODE = "project.exceptions.IncorrectPurchaseCreditCardException";
	private final static String TICKETS_ALREADY_DELIVERED_EXCEPTION_CODE = "project.exceptions.TicketsAlreadyDeliveredException";
	private final static String MAX_TICKETS_EXCEEDED_EXCEPTION_CODE = "project.exceptions.MaxTicketsExceededException";
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
				
		List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
			.map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage())).collect(Collectors.toList());
		
		return new ErrorsDto(fieldErrors);
	    
	}
	
	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {
		
		String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE, 
				new Object[] {nameMessage, exception.getKey().toString()}, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}
	
	@ExceptionHandler(DuplicateInstanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {
		
		String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
		String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE, 
				new Object[] {nameMessage, exception.getKey().toString()}, DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}
	
	@ExceptionHandler(PermissionException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handlePermissionException(PermissionException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(PERMISSION_EXCEPTION_CODE, null, PERMISSION_EXCEPTION_CODE,
			locale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(NotEnoughSeatsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleNotEnoughSeatsException(NotEnoughSeatsException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(NOT_ENOUGH_SEATS_EXCEPTION_CODE, null, NOT_ENOUGH_SEATS_EXCEPTION_CODE,
			locale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(SessionAlreadyStartedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleSessionAlreadyStartedException(SessionAlreadyStartedException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(SESSION_ALREADY_STARTED_EXCEPTION_CODE, null, SESSION_ALREADY_STARTED_EXCEPTION_CODE,
			locale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(IncorrectPurchaseCreditCardException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleIncorrectPurchaseCreditCardException(IncorrectPurchaseCreditCardException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(INCORRECT_PURCHASE_CREDIT_CARD_EXCEPTION_CODE, null, INCORRECT_PURCHASE_CREDIT_CARD_EXCEPTION_CODE,
			locale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(TicketsAlreadyDeliveredException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleTicketsAlreadyDeliveredException(TicketsAlreadyDeliveredException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(TICKETS_ALREADY_DELIVERED_EXCEPTION_CODE, null, TICKETS_ALREADY_DELIVERED_EXCEPTION_CODE,
			locale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(MaxTicketsExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMaxTicketsExceededException(MaxTicketsExceededException exception, Locale locale) {
		
		String errorMessage = messageSource.getMessage(MAX_TICKETS_EXCEEDED_EXCEPTION_CODE, 
			new Object[] {exception.getMaxTickets()}, MAX_TICKETS_EXCEEDED_EXCEPTION_CODE, locale);

		return new ErrorsDto(errorMessage);
		
	}

}
