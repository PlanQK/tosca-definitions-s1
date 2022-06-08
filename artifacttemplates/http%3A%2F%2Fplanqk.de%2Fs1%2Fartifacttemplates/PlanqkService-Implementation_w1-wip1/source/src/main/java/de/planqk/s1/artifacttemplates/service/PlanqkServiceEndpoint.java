package de.planqk.s1.artifacttemplates.service;

import de.planqk.s1.artifacttemplates.OpenToscaHeaders;
import de.planqk.s1.artifacttemplates.SoapUtil;
import java.util.Objects;
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

        SoapUtil.sendSoapResponse(invokeResponse, InvokeResponse.class, openToscaHeaders.replyTo());
    }
}
