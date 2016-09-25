package jaxb.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class PersonList {


    private ArrayList<Person> list = new ArrayList<Person>();

    public PersonList() {

    }

    public void addPerson(Person p) {
        this.list.add(p);
    }

    public ArrayList<Person> getList() {
        return list;
    }

    @XmlElement(name = "Person")
    public void setList(ArrayList<Person> list) {
        this.list = list;
    }
}
