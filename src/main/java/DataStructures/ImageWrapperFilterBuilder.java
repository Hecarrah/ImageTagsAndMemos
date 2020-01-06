package DataStructures;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class ImageWrapperFilterBuilder {

    private String suffix = "";
    private ArrayList<Tag> tags = new ArrayList<>();
    private boolean tagged;
    private String tagCount;
    

    public ImageWrapperFilterBuilder() {
    }

    public ImageWrapperFilterBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public ImageWrapperFilterBuilder addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public ImageWrapperFilterBuilder addTags(ArrayList<Tag> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public ImageWrapperFilterBuilder removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }
    public ImageWrapperFilterBuilder setTagged(Boolean tag) {
        this.tagged = tag;
        return this;
    }
    /**
     * 
     * @param count {@literal in form (<,>,=),(<Number>)}
     * @return 
     */
    public ImageWrapperFilterBuilder setTagCount(String count) {
        this.tagCount = count;
        return this;
    }

    public ImageWrapperFilter createImageWrapperFilter() {
        return new ImageWrapperFilter(suffix, tags, tagged, tagCount);
    }

}
