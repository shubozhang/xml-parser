package jaxb.xmlwriter;


import jaxb.entity.Person;
import jaxb.entity.PersonList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class XMLwriter {

    public static void main(String[] args) {
        Person p1 = new Person("A1", "A1");
        Person p2 = new Person("A2", "A2");

        PersonList personList = new PersonList();
        personList.addPerson(p1);
        personList.addPerson(p2);

        try {

            File file = new File("files/100.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(personList, file);
            jaxbMarshaller.marshal(personList, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }



}
