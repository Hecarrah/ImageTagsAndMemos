//package Data;
//
//import java.io.Serializable;
//import java.util.UUID;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlType;
//
//@XmlRootElement
//@XmlType(propOrder = {"id", "a", "FC", "label", "note","b"})
//public class PersonFCConnection implements ConnectionInterface {
//
//    private String id;
//    private Person person;
//    private FreeCompany fc;
//    private String note;
//    private String label;
//
//    public PersonFCConnection(Person a, FreeCompany b, String label, String note) {
//        this.id = UUID.randomUUID().toString();
//        this.person = a;
//        this.fc = b;
//        this.label = label;
//        this.note = note;
//    }
//    public PersonFCConnection() {
//    }
//
//    @XmlElement
//    @Override
//    public void setId(String id) {
//        this.id = id;
//    }
//    @Override
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public Person getA() {
//        return person;
//    }
//
//    @XmlElement
//    @Override
//    public void setA(Person A) {
//        this.person = A;
//    }
//
//    @Override
//    public FreeCompany getFC() {
//        return fc;
//    }
//
//    @XmlElement
//    @Override
//    public void setFC(FreeCompany B) {
//        this.fc = B;
//    }
//
//    @Override
//    public String getNote() {
//        return note;
//    }
//
//    @XmlElement
//    @Override
//    public void setNote(String note) {
//        this.note = note;
//    }
//
//    @Override
//    public String getLabel() {
//        return label;
//    }
//
//    @XmlElement
//    @Override
//    public void setLabel(String label) {
//        this.label = label;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            System.out.println("comparing null connection");
//            return false;
//        }
//        PersonFCConnection other = (PersonFCConnection) obj;
//        if (getA().equals(other.getA()) && getFC().equals(other.getFC())) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public Person getB() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void setB(Person B) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
