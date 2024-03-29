package com.volvo.gcc3.interiorroom.request.response;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;

public class XmlUnmarshaller {

    private static JAXBContext jaxbContext;
    private static String removeString = " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"";

    public XmlUnmarshaller() {

    }

    public static InteriorResponse UnmarshalXml(String xmlContent) {
        InteriorResponse interiorResponse = null;
        try {
        	System.out.println("Before:- "+xmlContent);
        	xmlContent = removeSoapHeadTail(xmlContent);
        	System.out.println("After: "+xmlContent);
            StringReader sr = new StringReader(xmlContent);
            // XMLEventReader reader = XMLInputFactory.newFactory().createXMLEventReader(sr);
            // XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(reader);
            jaxbContext = JAXBContext.newInstance(InteriorResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            interiorResponse = (InteriorResponse) unmarshaller.unmarshal(sr);
            printData(interiorResponse);
        } catch (JAXBException | FactoryConfigurationError e) {
            e.printStackTrace();
        }
        return interiorResponse;
    }
    
    private static String removeSoapHeadTail(String soapContent) {
    	String soapHead = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body>";
    	String soapTail = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
    	soapContent = soapContent.replace(soapHead, "");
    	soapContent = soapContent.replace(soapTail, "");
    	return soapContent;

    	 	
    }

    public void printSize(InteriorResponse interiorResponse) {
        System.out.println("StartWeek: " + interiorResponse.getStartWeek());
        System.out.println("EndWeek: " + interiorResponse.getEndWeek());
        System.out.println("Pno12: " + interiorResponse.getPno12());

        System.out.println("CommonFeatureList size: " + interiorResponse.getCommonFeatureList());

        System.out.println("CommonOptionList size: " + interiorResponse.getCommonOptionList());

        System.out.println("InteriorRoomList size: " + interiorResponse.getInteriorRoomList());

        List<InteriorRoom> interiorRoomList = interiorResponse.getInteriorRoomList();
        for (InteriorRoom interiorRoom : interiorRoomList) {
            System.out.println("Color: " + interiorRoom.getColor());
            System.out.println("Upholstery: " + interiorRoom.getUpholstery());

            System.out.println("Color: " + interiorRoom.getColor() + " FeatureList size: " + interiorRoom.getFeatureList().size());
            System.out.println("Color: " + interiorRoom.getColor() + " OptionList size: " + interiorRoom.getOptionList().size());

        }

    }

    public static void printData(InteriorResponse interiorResponse) {
        System.out.println("StartWeek: " + interiorResponse.getStartWeek());
        System.out.println("EndWeek: " + interiorResponse.getEndWeek());
        System.out.println("Pno12: " + interiorResponse.getPno12());

        List<String> commonFeatureList = interiorResponse.getCommonFeatureList();
        for (String feature : commonFeatureList) {
            System.out.println("Common feature code: " + feature);
        }

        List<String> commonOptionList = interiorResponse.getCommonOptionList();
        for (String option : commonOptionList) {
            System.out.println("Common Option: " + option);
        }

        List<InteriorRoom> interiorRoomList = interiorResponse.getInteriorRoomList();

        for (InteriorRoom interiorRoom : interiorRoomList) {
            System.out.println("Color: " + interiorRoom.getColor());
            System.out.println("Upholstery: " + interiorRoom.getUpholstery());
            System.out.println("Color " + interiorRoom.getColor() + " features:- ");
            for (String feature : interiorRoom.getFeatureList()) {
                System.out.println("Color " + interiorRoom.getColor() + " Feature code: " + feature);
            }
            for (String option : interiorRoom.getOptionList()) {
                System.out.println("Color " + interiorRoom.getColor() + " Optoine code: " + option);
            }
        }
    }

    public static InteriorResponse getInteriorResponse(String xmlContent) {
        InteriorResponse interiorResponse = UnmarshalXml(xmlContent);
        return interiorResponse;
    }
}
