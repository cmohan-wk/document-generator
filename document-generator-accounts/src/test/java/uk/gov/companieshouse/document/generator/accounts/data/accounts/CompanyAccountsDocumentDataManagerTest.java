package uk.gov.companieshouse.document.generator.accounts.data.accounts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.CompanyAccounts;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.document.generator.accounts.AccountType;
import uk.gov.companieshouse.document.generator.accounts.data.transaction.Resources;
import uk.gov.companieshouse.document.generator.accounts.data.transaction.Transaction;
import uk.gov.companieshouse.document.generator.accounts.exception.ServiceException;
import uk.gov.companieshouse.document.generator.accounts.mapping.smallfull.model.ixbrl.SmallFullAccountIxbrl;
import uk.gov.companieshouse.document.generator.accounts.mapping.smallfull.model.ixbrl.balancesheet.BalanceSheet;
import uk.gov.companieshouse.document.generator.accounts.mapping.smallfull.model.ixbrl.company.Company;
import uk.gov.companieshouse.document.generator.accounts.mapping.smallfull.model.ixbrl.period.Period;
import uk.gov.companieshouse.document.generator.accounts.service.AccountsService;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyAccountsDocumentDataManagerTest {

    @InjectMocks
    CompanyAccountsDocumentDataManager companyAccountsDocumentDataManager;

    @Mock
    private AccountsService mockAccountsService;

    private static final String COMPANY_ACCOUNTS_RESOURCE_URI = "/transactions/091174-913515-326060/company-accounts/xU-6Vebn7F8AgLwa2QHBUL2yRpk=";

    private static final String SERVICE_EXCEPTION = "Failure in service layer";

    @Test
    @DisplayName("Tests successful return of accounts data for small full")
    void testSuccessfullReturnOfAccountsDataForSmallFull() throws ServiceException {

        when(mockAccountsService.getSmallFullAccounts(anyString(), anyString(), any(Transaction.class)))
                .thenReturn(createCurrentSmallFullAccounts());

        assertNotNull(companyAccountsDocumentDataManager.getCompanyAccountDocumentData(createCompanyAccounts(),
                createAccountType(), createTransaction(COMPANY_ACCOUNTS_RESOURCE_URI), COMPANY_ACCOUNTS_RESOURCE_URI));
    }

    @Test
    @DisplayName("Tests service error thrown when obtaining data for small full")
    void testServiceErrorThrownWhenObtainingSmallFullAccounts() throws ServiceException {

        when(mockAccountsService.getSmallFullAccounts(anyString(), anyString(), any(Transaction.class)))
                .thenThrow(new ServiceException(SERVICE_EXCEPTION));

        assertThrows(ServiceException.class, () -> companyAccountsDocumentDataManager.getCompanyAccountDocumentData(
                createCompanyAccounts(), createAccountType(), createTransaction(COMPANY_ACCOUNTS_RESOURCE_URI),
                COMPANY_ACCOUNTS_RESOURCE_URI));
    }

    private CompanyAccounts createCompanyAccounts() {

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();
        Map<String, String> links = new HashMap<>();
        links.put("small_full_accounts", COMPANY_ACCOUNTS_RESOURCE_URI);
        links.put("transaction", "/transactions/091174-913515-326060");
        companyAccounts.setLinks(links);

        return companyAccounts;
    }

    private Transaction createTransaction(String resourceUri) {
        Map<String, Resources> resources = new HashMap<>();
        resources.put(resourceUri, createResource(resourceUri));

        Transaction transaction = new Transaction();
        transaction.setResources(resources);

        return transaction;
    }

    private Resources createResource(String resourceUri) {
        Resources resource = new Resources();
        resource.setKind("kind");
        Map<String, String> links = new HashMap<>();
        links.put("resource", resourceUri);
        resource.setLinks(links);
        return resource;
    }

    private AccountType createAccountType() {
       return AccountType.getAccountType("small_full_accounts");
    }

    private SmallFullAccountIxbrl createCurrentSmallFullAccounts() {

        SmallFullAccountIxbrl smallFullAccountIxbrl = new SmallFullAccountIxbrl();

        smallFullAccountIxbrl.setBalanceSheet(new BalanceSheet());
        smallFullAccountIxbrl.setPeriod(createPeriod());
        smallFullAccountIxbrl.setCompany(new Company());
        smallFullAccountIxbrl.setApprovalDate("2018-01-01");
        smallFullAccountIxbrl.setApprovalName("name");

        return smallFullAccountIxbrl;
    }

    private Period createPeriod() {

        Period period = new Period();
        period.setCurrentPeriodStartOn("2018-01-01");
        period.setCurrentPeriodEndsOn("2018-12-31");

        return period;
    }
}