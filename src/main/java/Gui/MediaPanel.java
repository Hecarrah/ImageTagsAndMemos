package Gui;

import Main.Main;
import com.sun.jna.NativeLibrary;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class MediaPanel extends JPanel {

    //Declares our media player component
    private EmbeddedMediaPlayerComponent mediaplayer;
    //This string holds the media URL path
    private String mediapath = "";
    //This string holds the vlc URL path
    private final String vlcpath = "C:\\Program Files\\VideoLAN\\VLC";
    private JPanel panel, panel2;
    private JButton play_btn, stop_btn, foward_btn, rewind_btn, enlarge_btn;
    private JSlider timeline;

    public MediaPanel() {
        if (Main.getImageViewer() != null) {
            this.mediapath = Main.getImageViewer().imageFile.getPath();
        }
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcpath);
        mediaplayer = new EmbeddedMediaPlayerComponent();

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(mediaplayer, BorderLayout.CENTER);

        play_btn = new JButton("play");
        stop_btn = new JButton("stop");
        foward_btn = new JButton("ff");
        rewind_btn = new JButton("rew");
        enlarge_btn = new JButton("enlarge");

        timeline = new JSlider(0, 100, 0);
        timeline.setMajorTickSpacing(10);
        timeline.setMajorTickSpacing(5);
        timeline.setPaintTicks(true);

        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel2.add(play_btn);
        panel2.add(stop_btn);
        panel2.add(foward_btn);
        panel2.add(rewind_btn);
        panel2.add(enlarge_btn);
        panel2.add(timeline);
        panel.add(panel2, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        if (!mediapath.equals("")) {
            play();
        }
    }

    public void poke() {
        if (Main.getImageViewer() != null) {
            this.mediapath = Main.getImageViewer().imageFile.getPath();
            //mediaplayer.getMediaPlayer().attachVideoSurface();
            mediaplayer.getMediaPlayer().playMedia(mediapath);
            mediaplayer.getMediaPlayer().mute(false);
            mediaplayer.getMediaPlayer().setRepeat(true);
            mediaplayer.getMediaPlayer().enableOverlay(true);
            //mediaplayer.getMediaPlayer().playMedia(mediapath);
        }
    }

    public void stop() {
        mediaplayer.getMediaPlayer().mute(true);
        mediaplayer.getMediaPlayer().stop();
        mediaplayer.getMediaPlayer().enableOverlay(false);
    }

    public void play() {
        mediaplayer.getMediaPlayer().playMedia(mediapath);
        mediaplayer.getMediaPlayer().mute(false);
    }
}
