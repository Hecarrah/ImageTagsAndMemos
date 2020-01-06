/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *
 * @author Peter
 */
public class Utils {

    public static Object[] initFilterArray() {
        Object[] array = new Object[5];
        array[0] = "";
        array[1] = 0;
        array[2] = 999;
        array[3] = new ArrayList<String>();
        array[4] = new ArrayList<String>();
        return array;
    }

    public static String thumbRename(String oldName) {
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        String substring = oldName.substring(0, oldName.lastIndexOf("."));
        String newName = substring + suffix + "Thumb.png";
        return newName;
    }

    public static String thumbRenameBackwards(String oldName) {
        String newName = oldName.replace("Thumb.png", "");
        return newName;
    }
}
