//package Data;
//
//import Enum.Job;
//import Enum.Race;
//import Enum.Server;
//import Enum.Sex;
//import Enum.Subrace;
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class Group implements CardInterface {
//
//    private String id;
//    private String name;
//    private ArrayList<ConnectionInterface> personConnection = new ArrayList<>();
//    private ArrayList<ConnectionInterface> personFCConnection = new ArrayList<>();
//    private String link;
//
//    public Group(String n, String l, ArrayList<ConnectionInterface> members) {
//        this.name = name;
//        this.link = l;
//        this.personConnection = members;
//    }
//
//    public Group(String n, String l) {
//        this.name = n;
//        this.link = l;
//    }
//
//    public Group(String n) {
//        this.name = n;
//    }
//
//    @Override
//    public String getId() {
//        return this.id;
//    }
//
//    @Override
//    public void setId(String newid) {
//        this.id = newid;
//    }
//
////<editor-fold defaultstate="collapsed" desc="name and link setters and getters">
//    @Override
//    public String getLink() {
//        return link;
//    }
//
//    @Override
//    public void setLink(String link) {
//        this.link = link;
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }
////</editor-fold>
//
////<editor-fold defaultstate="collapsed" desc="PersonConnections">
//    @Override
//    public boolean containsPersonConnection(ConnectionInterface p) {
//        return personConnection.contains(p);
//    }
//
//    @Override
//    public void setPersonConnections(ArrayList<ConnectionInterface> members) {
//        this.personConnection = members;
//    }
//
//    @Override
//    public void addPersonConnection(ConnectionInterface p) {
//        this.personConnection.add(p);
//    }
//
//    @Override
//    public void removePersonConnection(ConnectionInterface p) {
//        this.personConnection.remove(p);
//    }
//
//    @Override
//    public ArrayList<ConnectionInterface> getPersonConnections() {
//        return personConnection;
//    }
////</editor-fold>
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Group other = (Group) obj;
//        if (!Objects.equals(this.name, other.name)) {
//            return false;
//        }
//        return true;
//    }
//}
