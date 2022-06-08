package de.planqk.s1.artifacttemplates.service;

public abstract class PlanqkServiceConstants {

    // namespace under which the SOAP service operates
    public static final String NAMESPACE_URI = "http://planqk.de/s1/artifacttemplates";

    // port type to use for the SOAP service
    public static final String PORT_TYPE_NAME = "de_planqk_s1_artifacttemplates_PlanqkService_InterfacePort";

    // name of the XML Schema file to use as basis for the WSDL generation
    public static final String XSD_NAME = "planqkServiceInterface.xsd";
}
