package ca.ulaval.glo2003.beds.rest.helpers;

import static ca.ulaval.glo2003.beds.domain.helpers.PackageObjectMother.createPackageName;
import static ca.ulaval.glo2003.transactions.domain.helpers.PriceObjectMother.createPrice;

import ca.ulaval.glo2003.beds.rest.PackageRequest;

public class PackageRequestBuilder {

  private PackageRequestBuilder() {}

  private String DEFAULT_NAME = createPackageName().toString();
  private String name = DEFAULT_NAME;

  private double DEFAULT_PRICE_PER_NIGHT = createPrice().toDouble();
  private double pricePerNight = DEFAULT_PRICE_PER_NIGHT;

  public static PackageRequestBuilder aPackageRequest() {
    return new PackageRequestBuilder();
  }

  public PackageRequestBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public PackageRequestBuilder withPricePerNight(double pricePerNight) {
    this.pricePerNight = pricePerNight;
    return this;
  }

  public PackageRequest build() {
    PackageRequest packageRequest = new PackageRequest();
    packageRequest.setName(name);
    packageRequest.setPricePerNight(pricePerNight);
    return packageRequest;
  }
}
