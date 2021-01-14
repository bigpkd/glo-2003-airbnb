package ca.ulaval.glo2003.transactions.services;

import static ca.ulaval.glo2003.time.domain.helpers.TimestampBuilder.aTimestamp;
import static ca.ulaval.glo2003.transactions.domain.helpers.TransactionBuilder.aTransaction;
import static ca.ulaval.glo2003.transactions.domain.helpers.TransactionObjectMother.*;
import static ca.ulaval.glo2003.transactions.services.TransactionService.AIRBNB;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.ulaval.glo2003.admin.domain.Configuration;
import ca.ulaval.glo2003.time.domain.Timestamp;
import ca.ulaval.glo2003.transactions.converters.ServiceFeeConverter;
import ca.ulaval.glo2003.transactions.converters.TransactionConverter;
import ca.ulaval.glo2003.transactions.domain.*;
import ca.ulaval.glo2003.transactions.rest.ServiceFeeRequest;
import ca.ulaval.glo2003.transactions.rest.TransactionResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TransactionServiceTest {

  private static TransactionService transactionService;
  private static TransactionFactory transactionFactory = mock(TransactionFactory.class);
  private static TransactionRepository transactionRepository = mock(TransactionRepository.class);
  private static TransactionConverter transactionConverter = mock(TransactionConverter.class);
  private static ServiceFeeConverter serviceFeeConverter = mock(ServiceFeeConverter.class);

  private static Transaction transaction = aTransaction().build();
  private static Transaction otherTransaction = aTransaction().build();
  private static Transaction anotherTransaction = aTransaction().build();
  private static TransactionResponse transactionResponse = mock(TransactionResponse.class);
  private static TransactionResponse otherTransactionResponse = mock(TransactionResponse.class);
  private static ServiceFeeRequest serviceFeeRequest = mock(ServiceFeeRequest.class);
  private static ServiceFee updatedServiceFee = mock(ServiceFee.class);
  private static String tenant = createFrom();
  private static String owner = createTo();
  private static Price tenantRefund = createTotal();
  private static Price ownerRefund = createTotal();
  private static Price total = createTotal();
  private static Timestamp departureDate = aTimestamp().build();

  @BeforeAll
  public static void setUpService() {
    transactionService =
        new TransactionService(
            transactionFactory, transactionRepository, transactionConverter, serviceFeeConverter);
  }

  private void resetMocks() {
    reset(transactionFactory, transactionRepository, transactionConverter);
  }

  private void setUpMocksForGetAll() {
    when(transactionRepository.getAll()).thenReturn(Arrays.asList(transaction, otherTransaction));
    when(transactionConverter.toResponse(transaction)).thenReturn(transactionResponse);
    when(transactionConverter.toResponse(otherTransaction)).thenReturn(otherTransactionResponse);
  }

  private void setUpMocksForAddStayBooked() {
    resetMocks();
    when(transactionFactory.createStayBooked(tenant, AIRBNB, total)).thenReturn(transaction);
  }

  private void setUpMocksForAddStayCompleted() {
    resetMocks();
    when(transactionFactory.createStayCompleted(AIRBNB, owner, total, departureDate))
        .thenReturn(transaction);
  }

  private void setUpMocksForAddStayCanceledHalfRefund() {
    resetMocks();
    when(transactionFactory.createStayCanceled(AIRBNB, tenant, tenantRefund))
        .thenReturn(transaction);
    when(transactionFactory.createStayCanceled(AIRBNB, owner, ownerRefund))
        .thenReturn(otherTransaction);
    when(transactionFactory.createStayRefunded(owner, AIRBNB, total, departureDate))
        .thenReturn(anotherTransaction);
  }

  private void setUpMocksForAddStayCanceledFullRefund() {
    resetMocks();
    when(transactionFactory.createStayCanceled(AIRBNB, tenant, total)).thenReturn(transaction);
    when(transactionFactory.createStayRefunded(owner, AIRBNB, total, departureDate))
        .thenReturn(otherTransaction);
  }

  @Test
  public void getAll_shouldGetAllTransactions() {
    setUpMocksForGetAll();

    List<Transaction> transactions = transactionService.getAll();

    assertTrue(transactions.contains(transaction));
    assertTrue(transactions.contains(otherTransaction));
  }

  @Test
  public void getAllResponses_shouldGetAllTransactionsResponses() {
    setUpMocksForGetAll();

    List<TransactionResponse> transactionResponses = transactionService.getAllResponses();

    assertTrue(transactionResponses.contains(transactionResponse));
    assertTrue(transactionResponses.contains(otherTransactionResponse));
  }

  @Test
  public void addStayBooked_shouldAddTransactionToRepository() {
    setUpMocksForAddStayBooked();

    transactionService.addStayBooked(tenant, total);

    verify(transactionRepository).add(eq(transaction));
  }

  @Test
  public void addStayCompleted_shouldAddTransactionToRepository() {
    setUpMocksForAddStayCompleted();

    transactionService.addStayCompleted(owner, total, departureDate);

    verify(transactionRepository).add(eq(transaction));
  }

  @Test
  public void addStayCanceledHalfRefund_shouldAddTransactionCancelTenantToRepository() {
    setUpMocksForAddStayCanceledHalfRefund();

    transactionService.addStayCanceledHalfRefund(
        tenant, owner, tenantRefund, ownerRefund, total, departureDate);

    verify(transactionRepository).add(eq(transaction));
  }

  @Test
  public void addStayCanceledHalfRefund_shouldAddTransactionCancelOwnerToRepository() {
    setUpMocksForAddStayCanceledHalfRefund();

    transactionService.addStayCanceledHalfRefund(
        tenant, owner, tenantRefund, ownerRefund, total, departureDate);

    verify(transactionRepository).add(eq(otherTransaction));
  }

  @Test
  public void addStayCanceledHalfRefund_shouldAddTransactionRefundToRepository() {
    setUpMocksForAddStayCanceledHalfRefund();

    transactionService.addStayCanceledHalfRefund(
        tenant, owner, tenantRefund, ownerRefund, total, departureDate);

    verify(transactionRepository).add(eq(anotherTransaction));
  }

  @Test
  public void addStayCanceledFullRefund_shouldAddTransactionCancelToRepository() {
    setUpMocksForAddStayCanceledFullRefund();

    transactionService.addStayCanceledFullRefund(tenant, owner, total, departureDate);

    verify(transactionRepository).add(eq(transaction));
  }

  @Test
  public void addStayCanceledFullRefund_shouldAddTransactionRefundToRepository() {
    setUpMocksForAddStayCanceledFullRefund();

    transactionService.addStayCanceledFullRefund(tenant, owner, total, departureDate);

    verify(transactionRepository).add(eq(otherTransaction));
  }

  @Test
  public void deleteAll_shouldDeleteAllTransactions() {
    transactionService.deleteAll();

    verify(transactionRepository).deleteAll();
  }

  @Test
  public void updateServiceFee_shouldUpdateServiceFeeOfConfiguration() {
    when(serviceFeeConverter.fromRequest(serviceFeeRequest)).thenReturn(updatedServiceFee);

    transactionService.updateServiceFee(serviceFeeRequest);
    ServiceFee serviceFee = Configuration.instance().getServiceFee();

    assertSame(serviceFee, updatedServiceFee);
  }
}
