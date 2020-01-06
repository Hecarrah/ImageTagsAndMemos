package Gui.Utils.Xiv;

import Enum.Job;
import Enum.Race;
import Enum.Sex;
import Enum.Subrace;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class xivUtils {

    public static void openWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException ex) {
            System.out.println(ex);
        }
    }

    public static String[] parseCharacterFromLodestone(String url, String name, String server) {
        Element character;
        Elements elementName = null, elementServer = null, elementRace = null, elementFC = null, elementCharLink = null;
        if (url != null) {
            try {
                Document doc = Jsoup.connect(url).get();
                character = doc.getElementById("character");
                elementName = character.getElementsByClass("frame__chara__name");
                elementServer = character.getElementsByClass("frame__chara__world");
                elementRace = character.getElementsByClass("character-block__name");
                elementFC = character.getElementsByClass("character__freecompany__name");

                return parseLodestoneInfo(elementRace, elementName, elementServer, elementFC, url);

            } catch (Exception ex) {
                System.out.println("Malformed lodestone URL");
                return null;
            }
        } else {
            try {
                String nameFormat = name.split(" ")[0] + "+" + name.split(" ")[1];
                Document doc = Jsoup.connect("https://eu.finalfantasyxiv.com/lodestone/character/?q=" + nameFormat + "&worldname=" + server).get();
                elementCharLink = doc.getElementsByClass("entry__link");
                Element get = elementCharLink.get(0);
                String attr = get.attr("abs:href");

                return parseCharacterFromLodestone(attr, name, server);

            } catch (Exception ex) {
                System.out.println(ex);
                return null;
            }
        }
    }

    public static String[] parseLodestoneInfo(Elements elementRace, Elements elementName, Elements elementServer, Elements elementFC, String url) {
        String raceGender = (elementRace.get(0).text());
        raceGender = raceGender.replace(" /", "");

        Race race = Race.HYUR;
        Subrace subrace = Subrace.HYUR_MID;
        Sex sex = Sex.MALE;
        for (Race r : Race.values()) {
            if (raceGender.contains(r.toString())) {
                race = r;
                break;
            }
        }
        for (Subrace r : Subrace.values()) {
            if (raceGender.contains(r.toString())) {
                subrace = r;
                break;
            }
        }
        if (raceGender.contains("♀")) {
            sex = (Sex.FEMALE);
        } else {
            sex = (Sex.MALE);
        }

        System.out.println(elementName.text());
        System.out.println(elementServer.text());
        System.out.println(race.toString());
        System.out.println(subrace.toString());
        System.out.println(sex.toString());
        System.out.println(elementFC.text().replaceAll("Free Company ", ""));
        String[] ret = {elementServer.text(), elementName.text(), race.toString(), subrace.toString(), sex.toString(), elementFC.text().replaceAll("Free Company ", ""), url};
        return ret;
    }
}
