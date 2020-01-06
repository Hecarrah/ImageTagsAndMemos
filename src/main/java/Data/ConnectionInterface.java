/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Peter
 */
public interface ConnectionInterface extends Serializable {

    boolean equals(Object obj);

    Card getA();

    Card getB();

    String getId();

    String getLabel();

    String getNote();

    public boolean isDirected();

    @XmlElement
    void setA(Card A);

    @XmlElement
    void setB(Card B);

    @XmlElement
    void setId(String id);

    @XmlElement
    void setLabel(String label);

    @XmlElement
    void setNote(String note);

    @XmlElement
    public void setDirected(boolean directed);

}
