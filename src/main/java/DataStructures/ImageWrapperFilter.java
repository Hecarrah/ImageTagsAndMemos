package DataStructures;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter
 */
public class ImageWrapperFilter {

    private String suffix;
    private ArrayList<Tag> tags = new ArrayList<>();
    private boolean tagged = false;
    private String tagCount;

    public ImageWrapperFilter(String suffix, ArrayList<Tag> tag, boolean tagged, String tagCount) {
        this.suffix = suffix;
        this.tags = tag;
        this.tagged = tagged;
        this.tagCount = tagCount;
    }

    public String getSuffix() {
        return suffix;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public boolean getTagged() {
        return tagged;
    }
    public String getTagCount() {
        return tagCount;
    }

}
