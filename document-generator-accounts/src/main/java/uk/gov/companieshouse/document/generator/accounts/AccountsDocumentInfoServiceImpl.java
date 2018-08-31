package uk.gov.companieshouse.document.generator.accounts;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.transaction.Resource;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.document.generator.accounts.service.TransactionService;
import uk.gov.companieshouse.document.generator.interfaces.DocumentInfoService;
import uk.gov.companieshouse.document.generator.interfaces.model.DocumentInfo;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Service
public class AccountsDocumentInfoServiceImpl implements DocumentInfoService {

    @Autowired
    private TransactionService transactionService;

    public static final String MODULE_NAME_SPACE = "document-generator-accounts";

    private static final Logger LOG = LoggerFactory.getLogger(MODULE_NAME_SPACE);

    @Override
    public DocumentInfo getDocumentInfo() {
        LOG.info("Started getting document");

        String resource = "";
        String resourceId = "";

        Transaction transaction = transactionService.getTransaction(resource);
        if (transaction == null) {
            LOG.error("transaction not found");
            return null;
        }

        String accountLink = Optional.of(transaction)
                .map(Transaction::getResources)
                .map(e -> e.get(resourceId))
                .map(Resource::getLinks)
                .map(e -> e.get("resource"))
                .orElseGet(() -> {
                    LOG.error("Unable to find account resource link in transaction under:" + resourceId);
                    return Optional.empty().toString();
                });

        // when abridged has been migrated to use the company-accounts api, the code for the
        // company accounts should work for abridged, resulting in this abridged specific code
        // qualifying for removal
        if (isAbridged(accountLink)) {
            return new DocumentInfo();
        }

        return null;

    }

    /**
     * determines whether it is an abridged link as /accounts/ only exists within the abridged
     * implementation
     * @param accountLink - account link
     * @return true if abridged, false if not
     */
    private boolean isAbridged(String accountLink) {
        return accountLink.contains("/accounts/");
    }
}