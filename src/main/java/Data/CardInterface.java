/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Enum.CardType;
import Enum.Job;
import Enum.Race;
import Enum.Server;
import Enum.Sex;
import Enum.Subrace;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Peter
 */
public interface CardInterface extends Serializable {

    boolean equals(Object o);

    String toString();

    String getId();

    void setId(String id);

    void generateId();

    @XmlElement
    void setType(CardType ntype);

    CardType getType();

    int getAnal();

    int getAssertiveness();

    int getAttractivity();

    int getAuthority();

    String getButt();

    String getChest();

    int getDrunkLewdness();

    String getFc();

    String getGeneralBodyType();

    String getHeight();

    int getHft();

    int getLewdness();

    String getLodestone();

    Job getMainJob();

    String getName();

    ArrayList<String> getNotes();

    int getOral();

    String getOtherNote();

    Race getRace();

    Server getServer();

    Sex getSex();

    String getSexualHistory();

    int getSexualMorale();

    int getSpecial();

    String getSpecialSkillNote();

    int getSubmissiviness();

    Subrace getSubrace();

    int getTease();

    int getTits();

    String getTraitNote();

    int getVaginal();

    @XmlElement
    void setAnal(int anal);

    @XmlElement
    void setAssertiveness(int assertiveness);

    @XmlElement
    void setAttractivity(int attractivity);

    @XmlElement
    void setAuthority(int authority);

    @XmlElement
    void setButt(String butt);

    @XmlElement
    void setChest(String chest);

    @XmlElement
    void setDrunkLewdness(int drunkLewdness);

    @XmlElement
    void setFc(String fc);

    @XmlElement
    void setGeneralBodyType(String generalBodyType);

    @XmlElement
    void setHeight(String height);

    @XmlElement
    void setHft(int hft);

    @XmlElement
    void setLewdness(int lewdness);

    void setLewds(int attractivity, int authority, int assertiveness, int submissiviness, int lewdness, int drunkLewdness, int sexualMorale, String generalBodyType, String height, String chest, String butt, int oral, int vaginal, int anal, int hft, int tits, int special, int tease, String specialSkillNote, String traitNote, String otherNote, String sexualHistory);

    @XmlElement
    void setLodestone(String lodestone);

    @XmlElement
    void setMainJob(Job mainJob);

    @XmlElement
    void setName(String name);

    @XmlElement
    void setNotes(ArrayList<String> newnotes);

    @XmlElement
    void setOral(int oral);

    @XmlElement
    void setOtherNote(String otherNote);

    @XmlElement
    void setRace(Race race);

    @XmlElement
    void setServer(Server server);

    @XmlElement
    void setSex(Sex sex);

    @XmlElement
    void setSexualHistory(String sexualHistory);

    @XmlElement
    void setSexualMorale(int sexualMorale);

    @XmlElement
    void setSpecial(int special);

    @XmlElement
    void setSpecialSkillNote(String specialSkillNote);

    @XmlElement
    void setSubmissiviness(int submissiviness);

    @XmlElement
    void setSubrace(Subrace subrace);

    @XmlElement
    void setTease(int tease);

    @XmlElement
    void setTits(int tits);

    @XmlElement
    void setTraitNote(String traitNote);

    @XmlElement
    void setVaginal(int vaginal);

    //fc
    void addConnection(DataConnection p);

    boolean containsConnection(DataConnection p);

    ArrayList<DataConnection> getConnections();

    String getLink();

    void removeConnection(DataConnection p);

    void setConnections(ArrayList<DataConnection> members);

    void setLink(String link);

    //Group
    void addMember(Card p);

    boolean containsMember(Card p);

    ArrayList<Card> getMembers();

    void removeMember(Card p);

    void setMembers(ArrayList<Card> members);

}
