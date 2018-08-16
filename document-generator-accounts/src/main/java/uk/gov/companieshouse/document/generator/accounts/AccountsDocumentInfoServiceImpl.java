package uk.gov.companieshouse.document.generator.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.document.generator.accounts.service.TransactionService;
import uk.gov.companieshouse.document.generator.interfaces.DocumentInfoService;
import uk.gov.companieshouse.document.generator.interfaces.model.DocumentInfo;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AccountsDocumentInfoServiceImpl implements DocumentInfoService {

    @Autowired
    private TransactionService transactionService;

    public static final String MODULE_NAME_SPACE = "document-generator-accounts";

    private static final Logger LOG = LoggerFactory.getLogger(MODULE_NAME_SPACE);

    @Override
    public DocumentInfo getDocumentInfo() {
        LOG.info("Started getting document");

        String resource = "";

        Transaction transaction = transactionService.getTransaction(resource);
        if (transaction == null) {
            return null;
        }

        return new DocumentInfo();

    }
}