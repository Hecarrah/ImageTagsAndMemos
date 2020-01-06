/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Enum.*;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Peter
 */
@XmlRootElement 
@XmlType (propOrder={"lodestone","server","name", "race", "subrace", "sex", "mainJob", "fc",
    "attractivity","authority","assertiveness","submissiviness","lewdness","drunkLewdness","sexualMorale",
    "generalBodyType","height","chest","butt",
    "oral","vaginal","anal","hft","tits","special","tease",
    "specialSkillNote","traitNote","otherNote","sexualHistory",
    "notes","id"})
public class Person {

    private String id;
 
    private String lodestone;
    private Server server;
    private String name;
    private Race race;
    private Subrace subrace;
    private Sex sex;
    private Job mainJob;
    private String fc;

    private int attractivity = 0;
    private int authority = 0;
    private int assertiveness = 0;
    private int submissiviness = 0;
    private int lewdness = 0;
    private int drunkLewdness = 0;
    private int sexualMorale = 0;

    private String generalBodyType = "Athletic";
    private String height = "Tiny";
    private String chest = "Flat";
    private String butt = "Flat";

    private int oral = 0;
    private int vaginal = 0;
    private int anal = 0;
    private int hft = 0;
    private int tits = 0;
    private int special = 0;
    private int tease = 0;

    private String specialSkillNote = "";
    private String traitNote = "";
    private String otherNote = "";
    private String sexualHistory = "";

    private ArrayList<String> notes = new ArrayList<>();

    public Person(String lodestone, Server server, String name, Race race, Subrace subrace, Sex sex, Job mainJob, String fc) {
        this.lodestone = lodestone;
        this.server = server;
        this.name = name;
        this.race = race;
        this.subrace = subrace;
        this.sex = sex;
        this.mainJob = mainJob;
        this.fc = fc;
        this.generateId();
        notes.add("");
        notes.add("");
        notes.add("");
    }

    public Person() {
    }

    public void generateId() {
        if (this.lodestone.equals("")) {
            this.setId("-1");
        } else {
            this.setId(Integer.parseInt((this.lodestone).replaceAll("[\\D]", "")) + "");
        }
    }
    
    public void setId(String newid) {
        this.id = newid;
    }
    public String getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if((Person)o != this){
            return false;
        }
        Person p = (Person) o;
        return (this.getId().equals(p.getId()));
    }

    
    public String toString() {
        return this.name + "_" + this.fc;
    }

    
    public void setLewds(int attractivity, int authority, int assertiveness, int submissiviness, int lewdness, int drunkLewdness, int sexualMorale, String generalBodyType, String height, String chest, String butt, int oral, int vaginal, int anal, int hft, int tits, int special, int tease, String specialSkillNote, String traitNote, String otherNote, String sexualHistory) {
        this.attractivity = attractivity;
        this.authority = authority;
        this.assertiveness = assertiveness;
        this.submissiviness = submissiviness;
        this.lewdness = lewdness;
        this.drunkLewdness = drunkLewdness;
        this.sexualMorale = sexualMorale;
        this.generalBodyType = generalBodyType;
        this.height = height;
        this.chest = chest;
        this.butt = butt;
        this.oral = oral;
        this.vaginal = vaginal;
        this.anal = anal;
        this.hft = hft;
        this.tits = tits;
        this.special = special;
        this.tease = tease;
        this.specialSkillNote = specialSkillNote;
        this.traitNote = traitNote;
        this.otherNote = otherNote;
        this.sexualHistory = sexualHistory;
    }

    
    public String getName() {
        return name;
    }

    @XmlElement
    
    public void setName(String name) {
        this.name = name;
    }

    
    public String getFc() {
        return fc;
    }

    @XmlElement
    
    public void setFc(String fc) {
        this.fc = fc;
    }

    
    public String getLodestone() {
        return lodestone;
    }

    @XmlElement
    
    public void setLodestone(String lodestone) {
        this.lodestone = lodestone;
    }

    
    public Server getServer() {
        return server;
    }

    @XmlElement
    
    public void setServer(Server server) {
        this.server = server;
    }

    
    public Race getRace() {
        return race;
    }

    @XmlElement
    
    public void setRace(Race race) {
        this.race = race;
    }

    
    public Subrace getSubrace() {
        return subrace;
    }

    @XmlElement
    
    public void setSubrace(Subrace subrace) {
        this.subrace = subrace;
    }

    
    public Sex getSex() {
        return sex;
    }

    @XmlElement
    
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    
    public Job getMainJob() {
        return mainJob;
    }

    @XmlElement
    
    public void setMainJob(Job mainJob) {
        this.mainJob = mainJob;
    }

    
    public ArrayList<String> getNotes() {
        return notes;
    }

    @XmlElement
    
    public void setNotes(ArrayList<String> newnotes) {
       // this.notes.clear();
        this.notes = newnotes;
    }

    
    public int getAttractivity() {
        return attractivity;
    }

    @XmlElement
    
    public void setAttractivity(int attractivity) {
        this.attractivity = attractivity;
    }

    
    public int getAuthority() {
        return authority;
    }

    @XmlElement
    
    public void setAuthority(int authority) {
        this.authority = authority;
    }

    
    public int getAssertiveness() {
        return assertiveness;
    }

    @XmlElement
    
    public void setAssertiveness(int assertiveness) {
        this.assertiveness = assertiveness;
    }

    
    public int getSubmissiviness() {
        return submissiviness;
    }

    @XmlElement
    
    public void setSubmissiviness(int submissiviness) {
        this.submissiviness = submissiviness;
    }

    
    public int getLewdness() {
        return lewdness;
    }

    @XmlElement
    
    public void setLewdness(int lewdness) {
        this.lewdness = lewdness;
    }

    
    public int getDrunkLewdness() {
        return drunkLewdness;
    }

    @XmlElement
    
    public void setDrunkLewdness(int drunkLewdness) {
        this.drunkLewdness = drunkLewdness;
    }

    
    public int getSexualMorale() {
        return sexualMorale;
    }

    @XmlElement
    
    public void setSexualMorale(int sexualMorale) {
        this.sexualMorale = sexualMorale;
    }

    
    public String getGeneralBodyType() {
        return generalBodyType;
    }

    @XmlElement
    
    public void setGeneralBodyType(String generalBodyType) {
        this.generalBodyType = generalBodyType;
    }

    
    public String getHeight() {
        return height;
    }

    @XmlElement
    
    public void setHeight(String height) {
        this.height = height;
    }

    
    public String getChest() {
        return chest;
    }

    @XmlElement
    
    public void setChest(String chest) {
        this.chest = chest;
    }

    
    public String getButt() {
        return butt;
    }

    @XmlElement
    
    public void setButt(String butt) {
        this.butt = butt;
    }

    
    public int getOral() {
        return oral;
    }

    @XmlElement
    
    public void setOral(int oral) {
        this.oral = oral;
    }

    
    public int getVaginal() {
        return vaginal;
    }

    @XmlElement
    
    public void setVaginal(int vaginal) {
        this.vaginal = vaginal;
    }

    
    public int getAnal() {
        return anal;
    }

    @XmlElement
    
    public void setAnal(int anal) {
        this.anal = anal;
    }

    
    public int getHft() {
        return hft;
    }

    @XmlElement
    
    public void setHft(int hft) {
        this.hft = hft;
    }

    
    public int getTits() {
        return tits;
    }

    @XmlElement
    
    public void setTits(int tits) {
        this.tits = tits;
    }

    
    public int getSpecial() {
        return special;
    }

    @XmlElement
    
    public void setSpecial(int special) {
        this.special = special;
    }

    
    public int getTease() {
        return tease;
    }

    @XmlElement
    
    public void setTease(int tease) {
        this.tease = tease;
    }

    
    public String getSpecialSkillNote() {
        return specialSkillNote;
    }

    @XmlElement
    
    public void setSpecialSkillNote(String specialSkillNote) {
        this.specialSkillNote = specialSkillNote;
    }

    
    public String getTraitNote() {
        return traitNote;
    }

    @XmlElement
    
    public void setTraitNote(String traitNote) {
        this.traitNote = traitNote;
    }

    
    public String getOtherNote() {
        return otherNote;
    }

    @XmlElement
    
    public void setOtherNote(String otherNote) {
        this.otherNote = otherNote;
    }

    
    public String getSexualHistory() {
        return sexualHistory;
    }

    @XmlElement
    
    public void setSexualHistory(String sexualHistory) {
        this.sexualHistory = sexualHistory;
    }
}
