package ca.ulaval.glo2003.interfaces.domain.serializers;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public abstract class AbstractSerializer<T> extends StdSerializer<T> {

  protected AbstractSerializer(Class<T> t) {
    super(t);
  }

  public abstract Class<?> getType();
}
