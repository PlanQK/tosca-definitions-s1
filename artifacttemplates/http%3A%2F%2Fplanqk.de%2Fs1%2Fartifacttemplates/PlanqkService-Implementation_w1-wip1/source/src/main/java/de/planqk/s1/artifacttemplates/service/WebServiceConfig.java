package de.planqk.s1.artifacttemplates.service;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/*");
    }

    @Bean(name = PlanqkServiceConstants.PORT_TYPE_NAME)
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema dockerInterfaceSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(PlanqkServiceConstants.PORT_TYPE_NAME);
        wsdl11Definition.setLocationUri("/");
        wsdl11Definition.setTargetNamespace(PlanqkServiceConstants.NAMESPACE_URI);
        wsdl11Definition.setSchema(dockerInterfaceSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema xsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource(PlanqkServiceConstants.XSD_NAME));
    }
}
