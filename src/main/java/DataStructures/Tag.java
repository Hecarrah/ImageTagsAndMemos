package DataStructures;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Tag implements Comparable {

    @XmlElement
    private String name;
    
    @XmlTransient
    private LinkedHashMap<Tag, Double> relation = new LinkedHashMap<>();
    private LinkedHashMap<Tag, Double> weight = new LinkedHashMap<>();
    public double id;
    ArrayList<Tag> ordered = new ArrayList<>();

    public Tag() {
        
    }

    public void adjustRelation(Tag tag, Double i) {
        if (!tag.equals(this)) {
            relation.put(tag, relation.getOrDefault(tag, 0.0) + 1);
        }
    }

    public Double getRelation(Tag tag) {
        try {
            return relation.get(tag);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Set<Tag> getAllRelations() {
//        LinkedHashMap<Tag, Double> relationReturn = new LinkedHashMap<>();
//        Iterator<Map.Entry<Tag, Double>> iterator = relation.entrySet().iterator();
//        while(iterator.hasNext()){
//            Map.Entry<Tag, Double> next = iterator.next();
//            relationReturn.put(next.getKey(), next.getValue());
//        }
//        for(Tag t : relationReturn.keySet()){
//            relationReturn.put(t, relationReturn.getOrDefault(t, 0.0) / relationReturn.size());
//        }
        return relation.keySet();
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj
    ) {
        if (obj == null) {
            return false;
        }
        Tag strObj = (Tag) obj;
        if (strObj.getName().equals(this.getName())) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Object obj) {
        Tag strObj = (Tag) obj;
        return this.getName().compareTo(strObj.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }
    

    @Override
    public String toString() {
        return this.getName();
    }
}
