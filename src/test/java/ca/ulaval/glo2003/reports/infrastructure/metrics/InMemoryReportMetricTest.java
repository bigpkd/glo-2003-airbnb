package ca.ulaval.glo2003.reports.infrastructure.metrics;

import static ca.ulaval.glo2003.reports.domain.helpers.ReportPeriodDataBuilder.aReportPeriodData;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.ulaval.glo2003.reports.domain.ReportPeriodData;
import ca.ulaval.glo2003.reports.domain.metrics.ReportMetric;
import ca.ulaval.glo2003.reports.domain.metrics.ReportMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class InMemoryReportMetricTest {

  protected static ReportMetric metric;
  protected static ReportPeriodData data;

  protected abstract ReportMetrics metricName();

  @BeforeEach
  public void setUpData() {
    data = aReportPeriodData().build();
  }

  @Test
  public void calculate_shouldCalculateWithMetricName() {
    metric.calculate(data);

    assertEquals(1, data.getMetrics().size());
    assertEquals(metricName(), data.getMetrics().get(0).getName());
  }
}
