package ca.ulaval.glo2003.bookings.rest.factories;

import ca.ulaval.glo2003.bookings.exceptions.BookingAlreadyCanceledException;
import org.eclipse.jetty.http.HttpStatus;

public class BookingAlreadyCanceledErrorFactory extends BookingErrorFactory {

  protected Class<?> getAssociatedException() {
    return BookingAlreadyCanceledException.class;
  }

  protected String getError() {
    return "BOOKING_ALREADY_CANCELED";
  }

  protected String getDescription() {
    return "booking has already been canceled";
  }

  protected int getStatus() {
    return HttpStatus.BAD_REQUEST_400;
  }
}
