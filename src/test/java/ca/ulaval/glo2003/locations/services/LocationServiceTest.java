package ca.ulaval.glo2003.locations.services;

import static ca.ulaval.glo2003.locations.domain.helpers.LocationBuilder.aLocation;
import static ca.ulaval.glo2003.locations.domain.helpers.LocationObjectMother.createZipCode;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.ulaval.glo2003.locations.converters.ZipCodeConverter;
import ca.ulaval.glo2003.locations.domain.Location;
import ca.ulaval.glo2003.locations.domain.LocationClient;
import ca.ulaval.glo2003.locations.domain.ZipCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationServiceTest {

  private static LocationService locationService;
  private static LocationClient locationClient = mock(LocationClient.class);
  private static ZipCodeConverter zipCodeConverter = mock(ZipCodeConverter.class);

  private static final ZipCode zipCode = createZipCode();
  private static final Location location = aLocation().withZipCode(zipCode).build();

  @BeforeAll
  public static void setUpService() {
    locationService = new LocationService(locationClient, zipCodeConverter);
  }

  @BeforeEach
  public void setUpMock() {
    when(zipCodeConverter.fromString(zipCode.toString())).thenReturn(zipCode);
    when(locationClient.getLocation(zipCode)).thenReturn(location);
    locationService = new LocationService(locationClient, zipCodeConverter);
  }

  @Test
  public void getLocation_shouldGetLocationFromClient() {
    Location actualLocation = locationService.getLocation(zipCode.toString());

    assertSame(location, actualLocation);
  }
}
