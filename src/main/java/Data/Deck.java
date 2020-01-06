package Data;

import Enum.CardType;
import Enum.Server;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable {

    private static ArrayList<Card> personCardList = new ArrayList<>();
    private static ArrayList<DataConnection> connectionList = new ArrayList<>();
    private static ArrayList<DataConnection> FCconnectionList = new ArrayList<>();
    private static ArrayList<Card> freeCompanyList = new ArrayList<>();
    private static ArrayList<Card> groupList = new ArrayList<>();
    private static ArrayList<Server> serverList = new ArrayList<>();

    public static void addGroup(Card g) {
        if (groupList.contains(g)) {
            groupList.remove(g);
        }
        groupList.add(g);
    }

    public static void removeGroup(Card g) {
        groupList.remove(g);
    }

    public static ArrayList<Card> returnGroups() {
        return groupList;
    }

    public static void addConnection(Card a, Card b, String label, String note, boolean directed) {
        DataConnection c = new DataConnection(a, b, label, note, directed);
        if (connectionList.contains(c)) {
            connectionList.remove(c);
        }

        connectionList.add(c);
    }

    public static void addConnection(DataConnection c) {
         if (connectionList.contains(c)) {
            connectionList.remove(c);
        }

        connectionList.add(c);
    }

    public static void removeConnection(DataConnection c) {
            connectionList.remove(c);
    }

    public static ArrayList<DataConnection> returnConnections() {
        return connectionList;
    }

    public static ArrayList<DataConnection> returnFCConnections() {
        return FCconnectionList;
    }

    public static void addPersonCard(Card c) {
        if (personCardList.contains(c)) {
            personCardList.remove(c);
        }
        personCardList.add(c);
    }

    public static Card personFromId(String id) {
        for (Card p : personCardList) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public static Card personFromName(String name) {
        for (Card p : personCardList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    public static Card fcFromName(String name) {
        for (Card p : freeCompanyList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }
    public static Card groupFromName(String name) {
        for (Card p : groupList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public static void removePersonCard(Card c) {
        File file = new File("Data/Xml/Characters" + c.getName() + ".xml");
        file.delete();
        personCardList.remove(c);
        for (ConnectionInterface pc : connectionList) {
            if (pc.getA().equals(c) || pc.getB().equals(c)) {
                File cFile = new File("Data/Xml/Connections" + pc.getId() + ".xml");
                cFile.delete();
                connectionList.remove(pc);
            }
        }
    }

    public static ArrayList<Card> returnPersonCards() {
        return personCardList;
    }

    public static void handleFCs() {
        ArrayList<String> fcarray = new ArrayList<String>();
        for (Card p : personCardList) {
            if (!p.getFc().equals("")) {
                if (!fcarray.contains(p.getFc())) {
                    fcarray.add(p.getFc());
                    freeCompanyList.add(new Card(p.getFc(), CardType.FreeCompany, "FC"+p.getFc()));
                }
            }
        }
        for (Card fc : freeCompanyList) {
            for (Card p : personCardList) {
                if (p.getFc().equals(fc.getName())) {
                    if (!fc.containsMember(p) && !p.getId().equals(fc.getId())) {
                        fc.addMember(p);
                        FCconnectionList.add(new DataConnection(p, fc, "", "",false));
                    }
                }
            }
        }
    }

    public static ArrayList<Card> returnFreeCompanies() {
        return freeCompanyList;
    }

    public static void handleServers() {
        ArrayList<String> fcarray = new ArrayList<String>();
        for (Card p : freeCompanyList) {
            if (!p.getMembers().get(0).getServer().equals("")) {
                if (!fcarray.contains(p.getMembers().get(0).getServer())) {
                    fcarray.add(p.getMembers().get(0).getServer().toString());
                    serverList.add((p.getMembers().get(0).getServer()));
                }
            }
        }
    }

    public static ArrayList<Server> returnServers() {
        return serverList;
    }
}
