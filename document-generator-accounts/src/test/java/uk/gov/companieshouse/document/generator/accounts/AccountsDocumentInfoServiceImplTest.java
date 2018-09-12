package uk.gov.companieshouse.document.generator.accounts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.document.generator.accounts.service.TransactionService;
import uk.gov.companieshouse.document.generator.interfaces.model.DocumentInfo;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AccountsDocumentInfoServiceImplTest {

    @InjectMocks
    private AccountsDocumentInfoServiceImpl accountsDocumentInfoService;

    @Mock
    private TransactionService transactionService;

    @Test
    @DisplayName("Tests the unsuccessful retrieval of an accounts document data due to no accounts resource in transaction")
    void testUnsuccessfulGetDocumentInfoNoAccountsResourceInTransaction() {
        Map<String, Resource> resources = new HashMap<>();
        resources.put("error", createResource());

        Transaction transaction = new Transaction();
        transaction.setResources(resources);
        when(transactionService.getTransaction(anyString())).thenReturn(transaction);

        assertNull(accountsDocumentInfoService.getDocumentInfo());
    }

    @Test
    @DisplayName("Tests the unsuccessful retrieval of an accounts document data due to error in transaction retrieval")
    void testUnsuccessfulGetDocumentInfoFailedTransactionRetrieval() {
        when(transactionService.getTransaction(anyString())).thenReturn(null);
        assertNull(accountsDocumentInfoService.getDocumentInfo());
    }

    @Test
    @DisplayName("Tests the successful retrieval of an abridged accounts document data")
    void testSuccessfulGetDocumentInfo() {
        Map<String, Resource> resources = new HashMap<>();
        resources.put("", createResource());

        Transaction transaction = new Transaction();
        transaction.setResources(resources);
        when(transactionService.getTransaction(anyString())).thenReturn(transaction);

        assertEquals(DocumentInfo.class, accountsDocumentInfoService.getDocumentInfo().getClass());
    }

    private Resource createResource() {
        Resource resource = new Resource();
        resource.setKind("kind");
        Map<String, String> links = new HashMap<>();
        links.put("resource", "/transactions/175725-236115-324362/accounts/wQSM2bWmQR3zrIw3x7apJOBjzWY=");
        resource.setLinks(links);
        return resource;
    }

}