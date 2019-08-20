package com.volvo.gcc3.interiorroom.response;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlUnmarshaller {

    private JAXBContext jaxbContext;
    // private InteriorResponse interiorResponse = null;

    public XmlUnmarshaller() {

    }

    public InteriorResponse UnmarshalXml(String xmlContent, InteriorResponse interiorResponse) {
        try {
            StringReader sr = new StringReader(xmlContent);
            jaxbContext = JAXBContext.newInstance(InteriorResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            interiorResponse = (InteriorResponse) unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        // printData(interiorResponse);
        return interiorResponse;
    }

    public void printData(InteriorResponse interiorResponse) {
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

    public InteriorResponse getInteriorResponse(String xmlContent, InteriorResponse interiorResponse) {
        // if (interiorResponse == null) {
            interiorResponse = UnmarshalXml(xmlContent, interiorResponse);
        // }
        return interiorResponse;
    }
}
