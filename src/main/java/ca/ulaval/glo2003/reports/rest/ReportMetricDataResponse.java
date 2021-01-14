package ca.ulaval.glo2003.reports.rest;

public abstract class ReportMetricDataResponse<T> {

  private String name;

  protected ReportMetricDataResponse(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract double getValue();
}
