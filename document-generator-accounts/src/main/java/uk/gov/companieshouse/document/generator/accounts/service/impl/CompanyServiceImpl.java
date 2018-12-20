package uk.gov.companieshouse.document.generator.accounts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.document.generator.accounts.exception.ServiceException;
import uk.gov.companieshouse.document.generator.accounts.service.ApiClientService;
import uk.gov.companieshouse.document.generator.accounts.service.CompanyService;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

import static uk.gov.companieshouse.document.generator.accounts.AccountsDocumentInfoServiceImpl.MODULE_NAME_SPACE;


@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private ApiClientService apiClientService;

    private static final UriTemplate GET_COMPANY_URI =
            new UriTemplate("/company/{companyNumber}");

    private static final Logger LOG = LoggerFactory.getLogger(MODULE_NAME_SPACE);

    @Override
    public CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException {

        CompanyProfileApi companyProfileApi;

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = GET_COMPANY_URI.expand(companyNumber).toString();

        try {
            LOG.info("SHAUN TEST: start execute company profile api");
            companyProfileApi = apiClient.company().get(uri).execute();
            LOG.info("SHAUN TEST: end execute company profile api");
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retrieving company profile", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for company resource", e);
        }

        return companyProfileApi;
    }
}
