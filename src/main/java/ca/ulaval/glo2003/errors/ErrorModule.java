package ca.ulaval.glo2003.errors;

import ca.ulaval.glo2003.errors.rest.ErrorMapper;
import ca.ulaval.glo2003.errors.rest.factories.ErrorFactory;
import ca.ulaval.glo2003.errors.rest.factories.JsonParsingErrorFactory;
import ca.ulaval.glo2003.errors.rest.handlers.CatchallExceptionHandler;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

public class ErrorModule extends AbstractModule {

  @Override
  protected void configure() {
    configureErrorFactories();

    bind(CatchallExceptionHandler.class);
    bind(ErrorMapper.class);
  }

  private void configureErrorFactories() {
    Multibinder<ErrorFactory<Exception>> multibinder =
        Multibinder.newSetBinder(binder(), new TypeLiteral<ErrorFactory<Exception>>() {});
    multibinder.addBinding().to(JsonParsingErrorFactory.class);
  }
}
