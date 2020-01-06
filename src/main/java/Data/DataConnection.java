package Data;

import java.util.UUID;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {"id", "a", "b", "label", "note", "directed"})
public class DataConnection implements ConnectionInterface {

    private String id = "";
    private Card A = new Card();
    private Card B = new Card();
    private String note = "";
    private String label = "";
    private boolean directed = false;

    public DataConnection(Card a, Card b, String label, String note, boolean dir) {
        this.id = UUID.randomUUID().toString();
        this.A = a;
        this.B = b;
        this.label = label;
        this.note = note;
        this.directed = dir;
    }

    public DataConnection() {
    }

    @XmlElement
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Card getA() {
        return A;
    }

    @XmlElement
    @Override
    public void setA(Card A) {
        this.A = A;
    }

    @Override
    public Card getB() {
        return B;
    }

    @XmlElement
    @Override
    public void setB(Card B) {
        this.B = B;
    }

    @Override
    public String getNote() {
        return note;
    }

    @XmlElement
    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @XmlElement
    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @XmlElement
    @Override
    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            System.out.println("comparing null connection");
            return false;
        }
        DataConnection other = (DataConnection) obj;
        if (getA().getId().equals(other.getA().getId()) && getB().getId().equals(other.getB().getId())) {
            return true;
        }
        if (getA().getId().equals(other.getB().getId()) && getB().getId().equals(other.getA().getId())) {
            return true;
        }
        return false;
    }
}
