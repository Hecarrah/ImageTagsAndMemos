//package Data.old;
//
//import java.util.UUID;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlType;
//
//@XmlRootElement
//@XmlType(propOrder = {"id", "a", "b", "label", "note"})
//public class PersonConnection implements ConnectionInterface {
//
//    private String id;
//    private Card A;
//    private Card B;
//    private String note;
//    private String label;
//
//    public PersonConnection(Card a, Card b, String label, String note) {
//        this.id = UUID.randomUUID().toString();
//        this.A = a;
//        this.B = b;
//        this.label = label;
//        this.note = note;
//    }
//    public PersonConnection() {
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
//    public Card getA() {
//        return A;
//    }
//
//    @XmlElement
//    @Override
//    public void setA(Card A) {
//        this.A = A;
//    }
//
//    @Override
//    public Card getB() {
//        return B;
//    }
//
//    @XmlElement
//    @Override
//    public void setB(Card B) {
//        this.B = B;
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
//        PersonConnection other = (PersonConnection) obj;
//        if (getA().equals(other.getA()) && getB().equals(other.getB())) {
//            return true;
//        }
//        if (getA().equals(other.getB()) && getB().equals(other.getA())) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean isDirected() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void setDirected(boolean directed) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
