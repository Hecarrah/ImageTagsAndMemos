package DataStructures;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Peter
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement
public class ImageList {

    ArrayList<ImageWrapper> allImages = new ArrayList<>();
    ArrayList<ImageWrapper> filtered = null;

    public ImageList() {

    }

    public ArrayList<ImageWrapper> getImages() {
        if (filtered == null) {
            return allImages;
        } else {
            return filtered;
        }
    }

    @XmlElementWrapper(name = "ImageList")
    @XmlElement(name = "Image")
    public ArrayList<ImageWrapper> getAllImages() {
        Collections.sort(allImages);
        return allImages;
    }

//    private boolean containsAnyTag(ImageWrapper img, ImageWrapperFilter f) {
//        for (Tag t : img.tags) {
//            if (img.tags.containsAll(f.getTags())) {
//                return true;
//            }
//        }
//        return false;
//    }
//    private boolean containsTagCount(ImageWrapper img, ImageWrapperFilter f) {
//        String[] split = f.getTagCount().split(",");
//        int type = -1;
//        if (split[0].equals("<")) {
//            type = 0;
//        }
//        if (split[0].equals(">")) {
//            type = 1;
//        }
//        if (split[0].equals("=")) {
//            type = 2;
//        }
//        if (type == -1) {
//            return false;
//        }
//        if (type == 0) {
//            if (img.returnTags().size() < Integer.parseInt(split[1])) {
//                return true;
//            }
//        }
//        if (type == 1) {
//            if (img.returnTags().size() > Integer.parseInt(split[1])) {
//                return true;
//            }
//        }
//        if (type == 2) {
//            if (img.returnTags().size() == Integer.parseInt(split[1])) {
//                return true;
//            }
//        }
//        return false;
//    }
//    public ArrayList<ImageWrapper> getFilteredImages(ImageWrapperFilter f) {
//        filtered = new ArrayList<>();
//        for (ImageWrapper img : allImages) {
//            if (img.path.endsWith(f.getSuffix()) && f.getTags().isEmpty() && f.getTagCount().equals("-,")) { //suffix, no tags, no count.
//                filtered.add(img);
//            } else if (img.path.endsWith(f.getSuffix()) && containsAnyTag(img, f) && f.getTagCount().equals("-,")) { //suffix, any tag, no count.
//                filtered.add(img);
//            } else if (img.path.endsWith(f.getSuffix()) && containsAnyTag(img, f) && containsTagCount(img, f)) { //suffix, any tag, count.
//                filtered.add(img);
//            } else if (f.getSuffix().equals("") && containsAnyTag(img, f) && f.getTagCount().equals("-,")) { //no suffix, any tag, no count.
//                filtered.add(img);
//            } else if (f.getSuffix().equals("") && containsAnyTag(img, f) && containsTagCount(img, f)) { //no suffix, any tag, count.
//                filtered.add(img);
//            } else if (f.getSuffix().equals("") && f.getTags().isEmpty() && containsTagCount(img, f)) { //no suffix, no tag, count.
//                filtered.add(img);
//            }
//        }
//        return filtered;
//    }
    public void resetFilter() {
        filtered = null;
    }

    public void setIwl(ArrayList<ImageWrapper> iwl) {
        allImages = iwl;
    }

    public void add(ImageWrapper iw) {
        if (!allImages.contains(iw)) {
            allImages.add(iw);
        }
    }

    public void remove(ImageWrapper iw) {
        if (allImages.contains(iw)) {
            allImages.remove(iw);
        }
    }

    public boolean contains(Path f) {
        if (allImages.contains(new ImageWrapper(f.toString(), null))) {
            return true;
        }
        return false;
    }
}
