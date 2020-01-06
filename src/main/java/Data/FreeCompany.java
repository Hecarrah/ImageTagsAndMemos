//package Data;
//
//import java.util.ArrayList;
//import java.util.Objects;
//
//public class FreeCompany implements CardInterface {
//
//    private String name;
//    private ArrayList<Card> members = new ArrayList<>();
//
//    public FreeCompany(String name, ArrayList<Card> members) {
//        this.name = name;
//        this.members = members;
//    }
//
//    public FreeCompany(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public boolean containsMember(Card p) {
//        return members.contains(p);
//    }
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public ArrayList<Card> getMembers() {
//        return members;
//    }
//
//    @Override
//    public void setMembers(ArrayList<Card> members) {
//        this.members = members;
//    }
//    @Override
//    public void addMember(Card p) {
//        this.members.add(p);
//    }
//    @Override
//    public void removeMember(Card p) {
//        this.members.remove(p);
//    }
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
//        final FreeCompany other = (FreeCompany) obj;
//        if (!Objects.equals(this.name, other.name)) {
//            return false;
//        }
//        return true;
//    }
//
//
//}
