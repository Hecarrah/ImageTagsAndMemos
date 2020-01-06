//package IO;
//
//import Algorithms.ImageProcessing;
//import Algorithms.TagAlgorithms;
//import Gui.Gui;
//import com.sun.jna.NativeLibrary;
//import uk.co.caprica.vlcj.binding.LibVlc;
//import uk.co.caprica.vlcj.log.NativeLog;
//import uk.co.caprica.vlcj.runtime.RuntimeUtil;
//
//public class Data {//248 is webm
//
//    private static final String NATIVE_LIBRARY_SEARCH_PATH = "C:\\Program Files\\VideoLAN\\VLC";
//    public static boolean debugMode = false;
//    public static Gui gui;
//    public static TagAlgorithms algo;
//    public static ImageProcessing imageProcessing;
//
//    public static void main(String[] args) {
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
//        NativeLog log = new NativeLog(LibVlc.INSTANCE, LibVlc.INSTANCE.libvlc_new(0, args));
//
//        try {
//            SQLHandler.createConnection();
//            SQLHandler.getImages();
//            SQLHandler.removeNonExisting();
//            SQLHandler.getTags();
//            SQLHandler.tableSize = SQLHandler.imageCount();
//            SQLHandler.removeOrphnaedTags();
//            //IO.loadImageXml();
//            IO.rescanImages();
//            System.out.println("SQL done");
//        } catch (Exception sQLException) {
//            System.out.println("SQL went bonkers");
//            System.out.println(sQLException);
//        }
//        System.out.println("starting!");
//
//        algo = new TagAlgorithms();
//        imageProcessing = new ImageProcessing();
//        gui = new Gui();
//        gui.setVisible(true);
//        //gui.resetImageViewer();
//        gui.update();
//
//    }
//
//    public void run() {
//        String[] a = {""};
//        main(a);
//    }
//
//    public static TagAlgorithms getTagAlgorithms() {
//        return algo;
//    }
//
//    public static ImageProcessing getImageProcessing() {
//        return imageProcessing;
//    }
//
//}
