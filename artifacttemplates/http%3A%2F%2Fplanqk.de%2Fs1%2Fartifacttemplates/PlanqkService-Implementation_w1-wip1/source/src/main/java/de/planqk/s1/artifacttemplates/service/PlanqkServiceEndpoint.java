package de.planqk.s1.artifacttemplates.service;

import de.planqk.s1.artifacttemplates.OpenToscaHeaders;
import de.planqk.s1.artifacttemplates.SoapUtil;
import java.util.Objects;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

@Endpoint
public class PlanqkServiceEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(PlanqkServiceEndpoint.class);

    @PayloadRoot(namespace = PlanqkServiceConstants.NAMESPACE_URI, localPart = "createServiceRequest")
    public void createService(@RequestPayload CreateServiceRequest request, MessageContext messageContext) {
        LOG.info("Received startContainer request!");

        OpenToscaHeaders openToscaHeaders = SoapUtil.parseHeaders(messageContext);

        InvokeResponse invokeResponse = new InvokeResponse();
        invokeResponse.setMessageID(openToscaHeaders.messageId());

        // create connection to the docker engine
        if (Objects.isNull(request.getBaseUrl())) {
            LOG.error("Base URL not defined in SOAP request!");
            invokeResponse.setError("Base URL must be defined to start a container!");

            SoapUtil.sendSoapResponse(invokeResponse, InvokeResponse.class, openToscaHeaders.replyTo());
            return;
        }

        var deploymentArtifact = openToscaHeaders.deploymentArtifacts()
            .entrySet()
            .stream()
            .flatMap(e -> e.getValue().entrySet().stream())
            .findFirst()
            .orElseThrow();

        var url = deploymentArtifact.getValue();
        var file = FileHandler.downloadFile(url);

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            String authUrlTemplate = "%s/auth/realms/planqk/protocol/openid-connect/token?grant_type=password&username=%s&password=%s";
            String authUrl = authUrlTemplate.formatted(request.getBaseUrl(), request.getUsername(), request.getPassword());
            var response = client.execute(new HttpGet(authUrl));

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            // TODO: move to separate method

        } catch (Exception e) {
            LOG.error("Error requesting PlanQK Platform access token");
        }

        SoapUtil.sendSoapResponse(invokeResponse, InvokeResponse.class, openToscaHeaders.replyTo());
    }
}
