package Algorithms;

import IO.ImageSQLHandler;
import DataStructures.ImageWrapper;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.image.processing.face.feature.ltp.LtpDtFeature;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

public class ImageProcessing {

    public double compareHistograms(ImageWrapper img1, ImageWrapper img2) {
        long currentTimeMillis = System.currentTimeMillis();
        List<MultidimensionalHistogram> histograms = new ArrayList<>();

        histograms.add(ImageSQLHandler.getHistogramDataFromImage(img1));
        histograms.add(ImageSQLHandler.getHistogramDataFromImage(img2));

        if (histograms.get(0) == null) {
            try {
                ImageSQLHandler.addHistogramDatatoImage(img1);
                histograms.clear();
                histograms.add(ImageSQLHandler.getHistogramDataFromImage(img1));
                histograms.add(ImageSQLHandler.getHistogramDataFromImage(img2));
            } catch (IOException ex) {
                Logger.getLogger(ImageProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (histograms.get(1) == null) {
            try {
                ImageSQLHandler.addHistogramDatatoImage(img2);
                histograms.clear();
                histograms.add(ImageSQLHandler.getHistogramDataFromImage(img1));
                histograms.add(ImageSQLHandler.getHistogramDataFromImage(img2));
            } catch (IOException ex) {
                Logger.getLogger(ImageProcessing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (histograms.get(0) != null && histograms.get(1) != null) {
            double distanceScore = histograms.get(0).compare(histograms.get(1), DoubleFVComparison.EUCLIDEAN);
            //System.out.println("Histograms took: " + (System.currentTimeMillis() - currentTimeMillis));
            return distanceScore;
        } else {
            return 99.9;
        }
    }

    public MultidimensionalHistogram getHistogram(ImageWrapper img) {
        try {
            MBFImage image1 = ImageUtilities.readMBF(new File(img.getPath()));
            HistogramModel model = new HistogramModel(4, 4, 4);
            model.estimateModel(image1);
            return model.histogram.clone();
        } catch (IOException ex) {
            System.out.println("exception on: " + img.getPath() + " " + ex);
        }
        return null;
    }

    public void extractFeatures(ImageWrapper img) {
        try {
            MBFImage image1 = ImageUtilities.readMBF(new File(img.getPath()));
//            FloatKMeans cluster = FloatKMeans.createKDTreeEnsemble(2);
//            float[][] imageData = image1.getPixelVectorNative(new float[image1.getWidth() * image1.getHeight()][3]);
//            FloatCentroidsResult result = cluster.cluster(imageData);
//
//            float[][] centroids = result.centroids;
//            for (float[] fs : centroids) {
//                System.out.println(Arrays.toString(fs));
//            }
//
//            HardAssigner<float[], ?, ?> assigner = result.defaultHardAssigner();
//            for (int y = 0; y < image1.getHeight(); y++) {
//                for (int x = 0; x < image1.getWidth(); x++) {
//                    float[] pixel = image1.getPixelNative(x, y);
//                    int centroid = assigner.assign(pixel);
//                    image1.setPixelNative(x, y, centroids[centroid]);
//                }
//            }
            FelzenszwalbHuttenlocherSegmenter fhs = new FelzenszwalbHuttenlocherSegmenter(5, 25, 2);
            List segment = fhs.segment(image1);
            SegmentationUtilities.renderSegments(image1,segment);
            //image1 = ColourSpace.convert(image1, ColourSpace.RGB);
            DisplayUtilities.display(image1);

        } catch (IOException ex) {
            Logger.getLogger(ImageProcessing.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
