package Enum;

import javafx.scene.image.ImageView;
public enum xivIcons {

    UKN("xiv.png"),
    PLD("\\icons\\FFXIVIcons\\01_TANK\\Job\\Paladin.png"),
    WAR("\\icons\\FFXIVIcons\\01_TANK\\Job\\Warrior.png"),
    DRK("\\icons\\FFXIVIcons\\01_TANK\\Job\\DarkKnight.png"),
    WHM("\\icons\\FFXIVIcons\\02_HEALER\\Job\\WhiteMage.png"),
    SCH("\\icons\\FFXIVIcons\\02_HEALER\\Job\\Scholar.png"),
    AST("\\icons\\FFXIVIcons\\02_HEALER\\Job\\Astrologian.png"),
    MNK("\\icons\\FFXIVIcons\\03_DPS\\Job\\Monk.png"),
    DRG("\\icons\\FFXIVIcons\\03_DPS\\Job\\Dragoon.png"),
    NIN("\\icons\\FFXIVIcons\\03_DPS\\Job\\Ninja.png"),
    SAM("\\icons\\FFXIVIcons\\03_DPS\\Job\\Samurai.png"),
    BRD("\\icons\\FFXIVIcons\\03_DPS\\Job\\Bard.png"),
    MCH("\\icons\\FFXIVIcons\\03_DPS\\Job\\Machinist.png"),
    BLM("\\icons\\FFXIVIcons\\03_DPS\\Job\\BlackMage.png"),
    SMN("\\icons\\FFXIVIcons\\03_DPS\\Job\\Summoner.png"),
    RDM("\\icons\\FFXIVIcons\\03_DPS\\Job\\RedMage.png");

    private final String img;   // in kilograms

    xivIcons(String img) {
        this.img = img;
    }

    public ImageView getIcon() {
        System.out.println(this.img);
        getClass().getClassLoader().getResource("\\xiv.png").getPath();
        return (new ImageView(getClass().getClassLoader().getResource(img).toExternalForm()));
        
    }
}
