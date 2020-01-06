package Algorithms;

import IO.ImageSQLHandler;
import DataStructures.ImageWrapper;
import DataStructures.Tag;
import Gui.FXMain;
import Main.Main;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Peter
 */
public class TagAlgorithms {

    static LinkedHashMap<Tag, Integer> countMap = new LinkedHashMap<>();
    static LinkedHashMap<Tag, Integer> countMap2 = new LinkedHashMap<>();
    static public double similarityBreakoffPoint = 0.50;
    static public double reverseSimilarityBreakoffPoint = 0.75;

    /*
    public static void tagRelationsForImage(ImageWrapper img) {
        for (Tag t : img.returnTags()) {
            for (Tag x : img.returnTags()) {
                Tag tag1 = Data.getTagList().getTagByName(t.getName());
                Tag tag2 = Data.getTagList().getTagByName(x.getName());
                tag1.adjustRelation(tag2, +1.0);
                //System.out.println(tag1.getName() + "->" + tag2.getName() + ", " + tag1.getRelation(tag2));
            }
        }
    }
/*
   public static void tagRelationAllImages() {
        for (ImageWrapper img : Data.getImageList().getAllImages()) {
            tagRelationsForImage(img);
        }
    }
     */
    public double imageContainsTagsFromAnotherImagePercent(ImageWrapper from, ImageWrapper to) {
        double returnValue = 0.0;
        for (String t : from.returnTags()) {
            if (to.returnTags().contains(t)) {
                returnValue++;
            }
        }
        returnValue = returnValue / from.returnTags().size();
        return returnValue;
    }

    public Set<Map.Entry<String, Double>> computeSuggestions(String given) {
        long currentTimeMillis = System.currentTimeMillis();
        HashMap<String, Double> counts = new HashMap<>();
        ImageWrapper wrapper = new ImageWrapper(given, null);
        HashMap<String, Double> countsSimilar = new HashMap<>();
        ArrayList<String> imageTags = ImageSQLHandler.getImageTags(given);
        ArrayList[] tagWeights = ImageSQLHandler.getTagWeights(imageTags); //name, weight, wpc
        ArrayList<String> similarImagesBasedOnTags = ImageSQLHandler.getSimilarImagesBasedOnTags(imageTags);
        double treshold = FXMain.getController().SuggestionSlider.getValue() / 100;

        if (imageTags.size() < 1) {
            try {
                ResultSet allImages = ImageSQLHandler.getHistogrammticallySimilarImagesToNewResultSet(wrapper, treshold);
                while (allImages.next()) {
                    ImageWrapper tempWrapper = new ImageWrapper(allImages.getString(("Path")), null);
                    ArrayList<String> imageTags1 = ImageSQLHandler.getImageTags(tempWrapper.getPath());
                    if (imageTags1.size() < 1) {
                        continue;
                    }
                    double compareHistograms = Main.getImageProcessing().compareHistograms(wrapper, tempWrapper);
                    if (compareHistograms == 99.9) {
                        ImageSQLHandler.addHistogramDatatoImage(wrapper);
                        compareHistograms = Main.getImageProcessing().compareHistograms(wrapper, tempWrapper);
                    }
                    if (compareHistograms < treshold && !tempWrapper.getPath().equals(wrapper.getPath())) {
                        for (String t : tempWrapper.returnTags()) {
                            countsSimilar.put(t, countsSimilar.getOrDefault(t, 1.0) * (10 * compareHistograms));
                        }
                    }
                }
                //allImages.close();
            } catch (IOException ex) {
                Logger.getLogger(TagAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TagAlgorithms.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (String path : similarImagesBasedOnTags) {
                ImageWrapper tempWrapper = new ImageWrapper(path, null);
                double compareHistograms = Main.getImageProcessing().compareHistograms(wrapper, tempWrapper);
                if (compareHistograms < 0.10 && !tempWrapper.getPath().equals(wrapper.getPath())) {
                    for (String t : tempWrapper.returnTags()) {
                        countsSimilar.put(t, countsSimilar.getOrDefault(t, 1.0) * (1 - compareHistograms));
                    }
                }
                if (compareHistograms < 0.05 && !tempWrapper.getPath().equals(wrapper.getPath())) {
                    for (String t : tempWrapper.returnTags()) {
                        countsSimilar.put(t, countsSimilar.getOrDefault(t, 1.0) * (10 - compareHistograms));
                    }
                }
                tempWrapper = null;
            }
        }

        if (imageTags.size() >= 1) {
            for (String path : similarImagesBasedOnTags) {
                ImageWrapper tempWrapper = new ImageWrapper(path, null);
                double similarity = imageContainsTagsFromAnotherImagePercent(wrapper, tempWrapper);
                double reverseSimilarity = imageContainsTagsFromAnotherImagePercent(tempWrapper, wrapper);

                if (similarity >= 0.10) {
                    for (String t : tempWrapper.returnTags()) {
                        countsSimilar.put(t, countsSimilar.getOrDefault(t, 1.0) * (0.5 + similarityBreakoffPoint));
                    }
                }
                if (reverseSimilarity >= 0.10) {
                    for (String t : tempWrapper.returnTags()) {
                        countsSimilar.put(t, countsSimilar.getOrDefault(t, 1.0) * (0.5 + similarityBreakoffPoint));
                    }
                }
                tempWrapper = null;
            }

        }
        for (String similar : countsSimilar.keySet()) {
            counts.put(similar, counts.getOrDefault(similar, countsSimilar.get(similar)) + 1);
        }
        if (tagWeights != null) {
            for (int i = 0; i < tagWeights[0].size(); i++) {
                String string = ((String) tagWeights[0].get(i));
                counts.put(string, counts.getOrDefault(string, 1.0) * ((int)tagWeights[1].get(i)) / (1 + (Double) tagWeights[2].get(i)));
            }
        }

        HashMap<String, Double> sortByValue = sortByValue(counts);

        if (Main.debugMode) {
            System.out.println("TagAlgorithms took: " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        }
        wrapper = null;
        return sortByValue.entrySet();
    }

    public Set<Map.Entry<String, Double>> computeSuggestionsForTagList(HashMap<String, Integer> map, int amount) {
        HashMap<String, Double> counts = new HashMap<>();
        ArrayList<String> imageTags = new ArrayList<>(map.keySet());
        ArrayList[] tagWeights = ImageSQLHandler.getTagWeights(imageTags);
        if (tagWeights != null) {
            for (String s : imageTags) {
                if (map.get(s) < amount) {
                    counts.put(s, (map.get(s) / (amount - map.get(s))) * 2.0);
                }
            }
            for (int i = 0; i < tagWeights[0].size(); i++) {
                if ((Double) tagWeights[2].get(i) > 0.9) {
                    String string = ((String) tagWeights[0].get(i));
                    counts.put(string, (counts.getOrDefault(string, 10.0) + 10.0) * map.getOrDefault(string, 1));
                } else {
                    String string = ((String) tagWeights[0].get(i));
                    counts.put(string, (counts.getOrDefault(string, 0.0) + ((Double) tagWeights[2].get(i))) * map.getOrDefault(string, 1));
                }
                //System.out.println(tagWeights[0].get(i));
            }
        }
        HashMap<String, Double> sortByValue = sortByValue(counts);
        return sortByValue.entrySet();
    }

    public <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
