package de.planqk.s1.artifacttemplates.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.planqk.s1.artifacttemplates.OpenToscaIASpringApplication;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = OpenToscaIASpringApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlanqkServiceEndpointTest {

    @LocalServerPort
    private int serverPort;

    @Test
    void getWsdlUsingQueryParameter() throws IOException {
        HttpGet request = new HttpGet(getBasePath() + "?wsdl");
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
        }
    }

    private String getBasePath() {
        return "http://localhost:" + serverPort + "/" + PlanqkServiceConstants.PORT_TYPE_NAME;
    }
}
