package jaxb.xmlreader;


import jaxb.entity.Person;
import jaxb.entity.PersonList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileReader;

public class XMLreader {

    public static void main(String[] args) {
        jaxb();

        jaxbSax();
    }

    private static void jaxb() {

        try {
            File file = new File("files/100.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            PersonList personList = (PersonList) unmarshaller.unmarshal(file);

            for (Person person: personList.getList()) {
                System.out.println(person.getId() + " : " + person.getName());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    private static void jaxbSax() {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            InputSource inputSource = new InputSource(new FileReader("files/100.xml"));
            SAXSource source = new SAXSource(xmlReader, inputSource);

            JAXBContext jaxbContext = JAXBContext.newInstance(PersonList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            PersonList personList = (PersonList) unmarshaller.unmarshal(source);

            for (Person person: personList.getList()) {
                System.out.println(person.getId() + " : " + person.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
