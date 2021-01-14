package ca.ulaval.glo2003.bookings.rest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.bookings.services.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

class BookingResourceTest {

  private static BookingResource bookingResource;
  private static BookingService bookingService = mock(BookingService.class);
  private static BookingMapper bookingMapper = mock(BookingMapper.class);

  private Request request = mock(Request.class);
  private Response response = mock(Response.class);
  private BookingRequest bookingRequest = mock(BookingRequest.class);
  private BookingResponse bookingResponse = mock(BookingResponse.class);
  private CancelationResponse cancelationResponse = mock(CancelationResponse.class);
  private String bedNumber = "bedNumber";
  private String bookingNumber = "bookingNumber";

  @BeforeAll
  public static void setUpResource() {
    bookingResource = new BookingResource(bookingService, bookingMapper);
  }

  @BeforeEach
  public void setUpMocks() throws JsonProcessingException {
    reset(response);
    String requestBody = "requestBody";
    when(request.body()).thenReturn(requestBody);
    when(request.params(bedNumber)).thenReturn(bedNumber);
    when(request.params(bookingNumber)).thenReturn(bookingNumber);
    when(bookingService.add(bedNumber, bookingRequest)).thenReturn(bookingNumber);
    when(bookingService.getResponse(bedNumber, bookingNumber)).thenReturn(bookingResponse);
    when(bookingService.cancel(bedNumber, bookingNumber)).thenReturn(cancelationResponse);
    when(bookingMapper.readValue(requestBody, BookingRequest.class)).thenReturn(bookingRequest);
  }

  @Test
  public void add_shouldReturnCorrectHeaderLocation() throws JsonProcessingException {
    String headerLocation = "/beds/" + bedNumber + "/bookings/" + bookingNumber;

    bookingResource.add(request, response);

    verify(response).header(HttpHeader.LOCATION.asString(), headerLocation);
  }

  @Test
  public void add_shouldSetCreatedAsHttpStatus() throws JsonProcessingException {
    bookingResource.add(request, response);

    verify(response).status(HttpStatus.CREATED_201);
  }

  @Test
  public void getByNumber_shouldReturnBooking() {
    BookingResponse actualResponse =
        (BookingResponse) bookingResource.getByNumber(request, response);

    assertSame(bookingResponse, actualResponse);
  }

  @Test
  public void getByNumber_shouldSetOKAsHttpStatus() {
    bookingResource.getByNumber(request, response);

    verify(response).status(HttpStatus.OK_200);
  }

  @Test
  public void cancel_shouldReturnCancelResponse() {
    CancelationResponse actualCancelationResponse =
        (CancelationResponse) bookingResource.cancel(request, response);

    assertSame(cancelationResponse, actualCancelationResponse);
  }

  @Test
  public void cancel_shouldSetOkAsHttpStatus() {
    bookingResource.cancel(request, response);

    verify(response).status(HttpStatus.OK_200);
  }
}
