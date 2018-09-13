package uk.gov.companieshouse.document.generator.core.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.companieshouse.document.generator.core.models.DocumentResponse;
import uk.gov.companieshouse.document.generator.core.service.response.ResponseObject;
import uk.gov.companieshouse.document.generator.core.service.response.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiResponseMapperTest {

    @InjectMocks
    private ApiResponseMapper apiResponseMapper;

    private static final String DESCRIPTION = "description";

    private static final String DESCRIPTION_IDENTIFIER = "descriptionIdentifier";

    private static final String SIZE = "size";

    private static final String LOCATION = "location";

    private String DATE = "date";

    private String DATE_VALUE = "01/01/1980";

    @Test
    @DisplayName("tests if Created returned")
    public void testCreatedReturned() {

        DocumentResponse documentResponse = new DocumentResponse();

        documentResponse.setDescriptionValues(setDescriptionValues());

        documentResponse.setDescription(DESCRIPTION);
        documentResponse.setSize(SIZE);
        documentResponse.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);
        documentResponse.setLinks(LOCATION);

        ResponseObject responseObject = new ResponseObject(ResponseStatus.CREATED, documentResponse);

        ResponseEntity responseEntity = apiResponseMapper.map(responseObject);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("tests if Bad request returned")
    public void testBadRequestReturned() {

        ResponseObject responseObject = new ResponseObject(ResponseStatus.NO_DATA_RETRIEVED);

        ResponseEntity responseEntity = apiResponseMapper.map(responseObject);

        assertNotNull(responseEntity);
        assertNull(responseEntity.getBody());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("tests if Internal Server Error returned")
    public void testInternalServerErrorReturned() {

        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setDescriptionValues(setDescriptionValues());

        documentResponse.setDescription(DESCRIPTION);
        documentResponse.setDescriptionIdentifier(DESCRIPTION_IDENTIFIER);

        ResponseObject responseObject = new ResponseObject(ResponseStatus.DOCUMENT_NOT_RENDERED, documentResponse);

        ResponseEntity responseEntity = apiResponseMapper.map(responseObject);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    /**
     * Set the descriptionValues
     *
     * @return Map of descriptionValues
     */
    private Map<String,String> setDescriptionValues() {

        Map<String, String> descriptionValues = new HashMap<>();
        descriptionValues.put(DATE, DATE_VALUE);

        return descriptionValues;
    }
}
