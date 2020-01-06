/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Enum.*;
import java.util.ArrayList;
import java.util.UUID;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Peter
 */
@XmlRootElement
@XmlType(propOrder = {"id", "lodestone", "server", "name", "race", "subrace", "sex", "mainJob", "fc",
    "attractivity", "authority", "assertiveness", "submissiviness", "lewdness", "drunkLewdness", "sexualMorale",
    "generalBodyType", "height", "chest", "butt",
    "oral", "vaginal", "anal", "hft", "tits", "special", "tease",
    "specialSkillNote", "traitNote", "otherNote", "sexualHistory",
    "notes", "members", "link", "connections", "type"})
public class Card implements CardInterface {

    private String id;
    private CardType type;

//<editor-fold defaultstate="collapsed" desc="CharacterStuff">
    private String lodestone;
    private Server server;
    private String name = "";
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
//</editor-fold>

    private ArrayList<Card> members = new ArrayList<>();

    private ArrayList<DataConnection> personConnection = new ArrayList<>();
    private ArrayList<DataConnection> FCConnection = new ArrayList<>();
    private String link;

    public Card(String lodestone, Server server, String name, Race race, Subrace subrace, Sex sex, Job mainJob, String fc, String id) {
        this.lodestone = lodestone;
        this.server = server;
        this.name = name;
        this.race = race;
        this.subrace = subrace;
        this.sex = sex;
        this.mainJob = mainJob;
        this.fc = fc;
        notes.add("");
        notes.add("");
        notes.add("");
        this.type = CardType.Character;
        this.id = id;
        this.generateId();
    }

    //fc
    public Card(String n, ArrayList<Card> members, String id) {
        this.name = n;
        this.members = members;
        this.type = CardType.FreeCompany;
        this.id = id;
        this.generateId();
    }

    public Card(String n, CardType newType, String id) {
        this.name = n;
        this.type = newType;
        this.id = id;
        this.generateId();
    }

    //group
    public Card(String n, String l, ArrayList<DataConnection> members, String id) {
        this.name = n;
        this.link = l;
        this.personConnection = members;
        this.type = CardType.Group;
        this.id = id;
        this.generateId();
    }

    public Card() {
    }

    @Override
    public void generateId() {
//        try {
//            if (this.lodestone.equals("")) {
//                this.setId("-1");
//            } else {
//                this.setId(this.type + "" + Integer.parseInt((this.lodestone).replaceAll("[\\D]", "")) + "");
//            }
//        } catch (NullPointerException ex) {
//            this.setId(this.getName());
//        }
        if (this.id == null) {
            this.setId(UUID.randomUUID().toString());
        }else{
            System.out.println("Card already has ID, not generating new one.");
        }
    }

    @Override
    public void setId(String newid) {
        this.id = newid;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        Card p = (Card) o;
//        if (this.type == CardType.Group) {
//            if (this.getName().equals(p.getName())) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        if (this.type == CardType.Character) {
//            if (this == null || o == null) {
//                return false;
//            }
//            if (this.getId().equals(p.getId())) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        if (o == null) {
//            return false;
//        }
//        if ((Card) o != this) {
//            return false;
//        }
        return (this.getId().equals(p.getId()));
    }

    @Override
    public String toString() {
        if (this.type == CardType.Character) {
            return this.name + "_" + this.fc;
        }
        if (this.type == CardType.FreeCompany) {
            return this.name;
        }
        if (this.type == CardType.Group) {
            return this.name;
        }
        return this.name + "_" + this.fc;
    }

    @XmlElement
    @Override
    public void setType(CardType ntype) {
        this.type = ntype;
    }

    @Override
    public CardType getType() {
        return this.type;
    }
//<editor-fold defaultstate="collapsed" desc="Character stuff">

    @Override
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

    @Override
    public String getName() {
        return name;
    }

    @XmlElement
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFc() {
        return fc;
    }

    @XmlElement
    @Override
    public void setFc(String fc) {
        this.fc = fc;
    }

    @Override
    public String getLodestone() {
        return lodestone;
    }

    @XmlElement
    @Override
    public void setLodestone(String lodestone) {
        this.lodestone = lodestone;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @XmlElement
    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Race getRace() {
        return race;
    }

    @XmlElement
    @Override
    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public Subrace getSubrace() {
        return subrace;
    }

    @XmlElement
    @Override
    public void setSubrace(Subrace subrace) {
        this.subrace = subrace;
    }

    @Override
    public Sex getSex() {
        return sex;
    }

    @XmlElement
    @Override
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public Job getMainJob() {
        return mainJob;
    }

    @XmlElement
    @Override
    public void setMainJob(Job mainJob) {
        this.mainJob = mainJob;
    }

    @Override
    public ArrayList<String> getNotes() {
        return notes;
    }

    @XmlElement
    @Override
    public void setNotes(ArrayList<String> newnotes) {
        // this.notes.clear();
        this.notes = newnotes;
    }

    @Override
    public int getAttractivity() {
        return attractivity;
    }

    @XmlElement
    @Override
    public void setAttractivity(int attractivity) {
        this.attractivity = attractivity;
    }

    @Override
    public int getAuthority() {
        return authority;
    }

    @XmlElement
    @Override
    public void setAuthority(int authority) {
        this.authority = authority;
    }

    @Override
    public int getAssertiveness() {
        return assertiveness;
    }

    @XmlElement
    @Override
    public void setAssertiveness(int assertiveness) {
        this.assertiveness = assertiveness;
    }

    @Override
    public int getSubmissiviness() {
        return submissiviness;
    }

    @XmlElement
    @Override
    public void setSubmissiviness(int submissiviness) {
        this.submissiviness = submissiviness;
    }

    @Override
    public int getLewdness() {
        return lewdness;
    }

    @XmlElement
    @Override
    public void setLewdness(int lewdness) {
        this.lewdness = lewdness;
    }

    @Override
    public int getDrunkLewdness() {
        return drunkLewdness;
    }

    @XmlElement
    @Override
    public void setDrunkLewdness(int drunkLewdness) {
        this.drunkLewdness = drunkLewdness;
    }

    @Override
    public int getSexualMorale() {
        return sexualMorale;
    }

    @XmlElement
    @Override
    public void setSexualMorale(int sexualMorale) {
        this.sexualMorale = sexualMorale;
    }

    @Override
    public String getGeneralBodyType() {
        return generalBodyType;
    }

    @XmlElement
    @Override
    public void setGeneralBodyType(String generalBodyType) {
        this.generalBodyType = generalBodyType;
    }

    @Override
    public String getHeight() {
        return height;
    }

    @XmlElement
    @Override
    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public String getChest() {
        return chest;
    }

    @XmlElement
    @Override
    public void setChest(String chest) {
        this.chest = chest;
    }

    @Override
    public String getButt() {
        return butt;
    }

    @XmlElement
    @Override
    public void setButt(String butt) {
        this.butt = butt;
    }

    @Override
    public int getOral() {
        return oral;
    }

    @XmlElement
    @Override
    public void setOral(int oral) {
        this.oral = oral;
    }

    @Override
    public int getVaginal() {
        return vaginal;
    }

    @XmlElement
    @Override
    public void setVaginal(int vaginal) {
        this.vaginal = vaginal;
    }

    @Override
    public int getAnal() {
        return anal;
    }

    @XmlElement
    @Override
    public void setAnal(int anal) {
        this.anal = anal;
    }

    @Override
    public int getHft() {
        return hft;
    }

    @XmlElement
    @Override
    public void setHft(int hft) {
        this.hft = hft;
    }

    @Override
    public int getTits() {
        return tits;
    }

    @XmlElement
    @Override
    public void setTits(int tits) {
        this.tits = tits;
    }

    @Override
    public int getSpecial() {
        return special;
    }

    @XmlElement
    @Override
    public void setSpecial(int special) {
        this.special = special;
    }

    @Override
    public int getTease() {
        return tease;
    }

    @XmlElement
    @Override
    public void setTease(int tease) {
        this.tease = tease;
    }

    @Override
    public String getSpecialSkillNote() {
        return specialSkillNote;
    }

    @XmlElement
    @Override
    public void setSpecialSkillNote(String specialSkillNote) {
        this.specialSkillNote = specialSkillNote;
    }

    @Override
    public String getTraitNote() {
        return traitNote;
    }

    @XmlElement
    @Override
    public void setTraitNote(String traitNote) {
        this.traitNote = traitNote;
    }

    @Override
    public String getOtherNote() {
        return otherNote;
    }

    @XmlElement
    @Override
    public void setOtherNote(String otherNote) {
        this.otherNote = otherNote;
    }

    @Override
    public String getSexualHistory() {
        return sexualHistory;
    }

    @XmlElement
    @Override
    public void setSexualHistory(String sexualHistory) {
        this.sexualHistory = sexualHistory;
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="FreeCompanyStuff">

    @Override
    public boolean containsMember(Card p) {
        return members.contains(p);
    }

    @Override
    public ArrayList<Card> getMembers() {
        return members;
    }

    @Override
    public void setMembers(ArrayList<Card> members) {
        this.members = members;
    }

    @Override
    public void addMember(Card p) {
        this.members.add(p);
    }

    @Override
    public void removeMember(Card p) {
        this.members.remove(p);
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="GroupStuff">

    @Override
    public String getLink() {
        return link;
    }

    @XmlElement
    @Override
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean containsConnection(DataConnection p) {
        return personConnection.contains(p);
    }

    @XmlElement
    @Override
    public void setConnections(ArrayList<DataConnection> members) {
        this.personConnection = members;
    }

    @Override
    public void addConnection(DataConnection p) {
        this.personConnection.add(p);
    }

    @Override
    public void removeConnection(DataConnection p) {
        this.personConnection.remove(p);
    }

    @Override
    public ArrayList<DataConnection> getConnections() {
        return personConnection;
    }
//</editor-fold>
}
