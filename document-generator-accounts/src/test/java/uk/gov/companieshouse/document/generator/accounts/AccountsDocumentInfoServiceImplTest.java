package uk.gov.companieshouse.document.generator.accounts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uk.gov.companieshouse.document.generator.accounts.data.transaction.Resources;
import uk.gov.companieshouse.document.generator.accounts.data.transaction.Transaction;
import uk.gov.companieshouse.document.generator.accounts.exception.HandlerException;
import uk.gov.companieshouse.document.generator.accounts.exception.ServiceException;
import uk.gov.companieshouse.document.generator.accounts.handler.accounts.AccountsHandler;
import uk.gov.companieshouse.document.generator.accounts.service.TransactionService;
import uk.gov.companieshouse.document.generator.interfaces.exception.DocumentInfoException;
import uk.gov.companieshouse.document.generator.interfaces.model.DocumentInfoRequest;
import uk.gov.companieshouse.document.generator.interfaces.model.DocumentInfoResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AccountsDocumentInfoServiceImplTest {

    @InjectMocks
    private AccountsDocumentInfoServiceImpl accountsDocumentInfoService;

    @Mock
    private AccountsHandler accountsHandlerMock;

    @Mock
    private TransactionService transactionService;

    @Mock
    private Transaction transaction;

    private static final String RESOURCE_ID = "/transactions/091174-913515-326060";
    private static final String RESOURCE_URI = "/transactions/091174-913515-326060/accounts/xU-6Vebn7F8AgLwa2QHBUL2yRpk=";

    @Test
    @DisplayName("Test DocumentInfoException thrown when error returned from transaction retrieval")
    void testErrorThrownWhenFailedTransactionRetrieval() throws ServiceException {
        when(transactionService.getTransaction(anyString())).thenThrow(new ServiceException("error"));

        assertThrows(DocumentInfoException.class, () ->
                accountsDocumentInfoService.getDocumentInfo(createDocumentInfoRequest()));
    }

    @Test
    @DisplayName("Tests the unsuccessful retrieval of an document data due to an error in transaction retrieval")
    void testUnsuccessfulGetDocumentInfoFailedTransactionRetrieval() throws ServiceException {
        when(transactionService.getTransaction(anyString())).thenThrow(new ServiceException("error"));

        assertThrows(ServiceException.class, () -> transactionService.getTransaction(""));
    }

    @Test
    @DisplayName("Tests the unsuccessful retrieval of document data due to no accounts resource in transaction")
    void testUnsuccessfulGetDocumentInfoNoAccountsResourceInTransaction() throws ServiceException, DocumentInfoException {
        Transaction transaction = createTransaction();
        transaction.getResources().remove(RESOURCE_ID);
        transaction.getResources().put("error", createResource());
        when(transactionService.getTransaction(anyString())).thenReturn(transaction);

        assertNull(accountsDocumentInfoService.getDocumentInfo(createDocumentInfoRequest()));
    }

    @Test
    @DisplayName("Test DocumentInfoException thrown when error returned from accounts handler")
    void testErrorThrownWhenFailedAccountsHandler() throws HandlerException, ServiceException {

        when(transactionService.getTransaction(anyString())).thenReturn(createTransaction());
        when(accountsHandlerMock.getAbridgedAccountsData(any(Transaction.class),  anyString())).
                thenThrow(new HandlerException("error"));

        assertThrows(DocumentInfoException.class, () ->
                accountsDocumentInfoService.getDocumentInfo(createDocumentInfoRequest()));
    }

    @Test
    @DisplayName("Tests the unsuccessful retrieval of document data due to error in Accounts handler")
    void testUnsuccessfulGetDocumentInfoExceptionFromAccountsHandler()
            throws HandlerException {
        when(accountsHandlerMock.getAbridgedAccountsData(any(Transaction.class),  anyString())).thenThrow(new HandlerException("error"));

        assertThrows(HandlerException.class, () -> accountsHandlerMock.getAbridgedAccountsData(transaction, ""));
    }

    @Test
    @DisplayName("Tests the successful retrieval of document data")
    void testSuccessfulGetDocumentInfo() throws HandlerException, ServiceException, DocumentInfoException {
        when(transactionService.getTransaction(anyString())).thenReturn(createTransaction());
        when(accountsHandlerMock.getAbridgedAccountsData(any(Transaction.class), anyString())).thenReturn(new DocumentInfoResponse());

        assertNotNull(accountsDocumentInfoService.getDocumentInfo(createDocumentInfoRequest()));
    }

    private DocumentInfoRequest createDocumentInfoRequest() {
        DocumentInfoRequest documentInfoRequest = new DocumentInfoRequest();
        documentInfoRequest.setResourceId(RESOURCE_ID);
        documentInfoRequest.setResourceUri(RESOURCE_URI);
        return documentInfoRequest;
    }

    private Transaction createTransaction() {
        Map<String, Resources> resources = new HashMap<>();
        resources.put(RESOURCE_URI, createResource());

        Transaction transaction = new Transaction();
        transaction.setResources(resources);

        return transaction;
    }

    private Resources createResource() {
        Resources resource = new Resources();
        resource.setKind("kind");
        Map<String, String> links = new HashMap<>();
        links.put("resource", RESOURCE_URI);
        resource.setLinks(links);
        return resource;
    }
}
