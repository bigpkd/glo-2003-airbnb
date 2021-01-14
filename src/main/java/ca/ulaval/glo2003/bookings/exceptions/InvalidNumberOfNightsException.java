package ca.ulaval.glo2003.bookings.exceptions;

public class InvalidNumberOfNightsException extends BookingException {

  public InvalidNumberOfNightsException() {
    super("INVALID_NUMBER_OF_NIGHTS");
  }
}
