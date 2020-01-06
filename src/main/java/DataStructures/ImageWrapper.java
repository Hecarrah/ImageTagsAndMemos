package DataStructures;

import IO.ImageSQLHandler;
import IO.IO;
import Gui.ImageViewer;
import IO.ThumbIO;
import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author Peter
 */
public class ImageWrapper implements Comparable {

//    @XmlElement
    String path;
    String thumbPath;
    @XmlElementWrapper(name = "Tags")
    @XmlElement(name = "tag")
    ArrayList<Tag> legacyTag = new ArrayList<>();
    ArrayList<String> tags = new ArrayList<>();

    public ImageWrapper(String path, ArrayList<String> tags, String thumbPath) {
        // this.path = path;
        this.tags = tags;
    }

    public ImageWrapper(String path, String thumbPath) {
        File thumb;
        if (thumbPath == null) {
            thumb = IO.findThumbnailFor(path);
            if(!thumb.exists()){
                ThumbIO.createThumbFile(path, false);
            }
            this.path = path;
            this.thumbPath = thumb.toString();
        } else {
            this.path = path;
            this.thumbPath = thumbPath;
        }
    }

    public ImageWrapper() {
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    public String getFileNamePath() {
        File f = new File(path);
        return f.getName();
    }

    public void setThumbPath(String path) {
        this.thumbPath = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void addTag(String t) {
        PreparedStatement[] addImageTagAddToBatch = ImageSQLHandler.addImageTagAddToBatch(this, t);
        ImageSQLHandler.addImageTagExecuteBatch(addImageTagAddToBatch);
    }

    public void removeTag(String t) {
        ImageSQLHandler.removeImageTag(this, t);
    }

    public ArrayList<Tag> returnTagsArray() {
        return legacyTag;
    }

    public ArrayList<String> returnTags() {
        return ImageSQLHandler.getImageTags(path);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImageWrapper other = (ImageWrapper) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        ImageWrapper obj = (ImageWrapper) o;
        return this.getFileNamePath().compareTo(obj.getFileNamePath());
    }

    @Override
    public String toString() {
        return this.path;
    }

}
