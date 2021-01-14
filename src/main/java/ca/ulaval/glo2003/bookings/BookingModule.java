package ca.ulaval.glo2003.bookings;

import ca.ulaval.glo2003.bookings.converters.BookingConverter;
import ca.ulaval.glo2003.bookings.exceptions.BookingException;
import ca.ulaval.glo2003.bookings.rest.BookingResource;
import ca.ulaval.glo2003.bookings.rest.factories.*;
import ca.ulaval.glo2003.bookings.rest.handlers.BookingExceptionHandler;
import ca.ulaval.glo2003.bookings.rest.serializers.BookingDeserializingModule;
import ca.ulaval.glo2003.bookings.rest.serializers.BookingSerializingModule;
import ca.ulaval.glo2003.bookings.services.BookingService;
import ca.ulaval.glo2003.bookings.services.CancelationService;
import ca.ulaval.glo2003.errors.rest.factories.ErrorFactory;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

public class BookingModule extends AbstractModule {

  @Override
  protected void configure() {
    configureErrorFactories();

    bind(BookingSerializingModule.class);
    bind(BookingDeserializingModule.class);
    bind(BookingConverter.class);
    bind(BookingService.class);
    bind(CancelationService.class);
    bind(BookingResource.class);
    bind(BookingExceptionHandler.class);
  }

  private void configureErrorFactories() {
    Multibinder<ErrorFactory<BookingException>> multibinder =
        Multibinder.newSetBinder(binder(), new TypeLiteral<ErrorFactory<BookingException>>() {});
    multibinder.addBinding().to(ArrivalDateInThePastErrorFactory.class);
    multibinder.addBinding().to(BookingNotFoundErrorFactory.class);
    multibinder.addBinding().to(InvalidArrivalDateErrorFactory.class);
    multibinder.addBinding().to(InvalidColonySizeErrorFactory.class);
    multibinder.addBinding().to(InvalidNumberOfNightsErrorFactory.class);
    multibinder.addBinding().to(BookingAlreadyCanceledErrorFactory.class);
    multibinder.addBinding().to(CancelationNotAllowedErrorFactory.class);
  }
}
