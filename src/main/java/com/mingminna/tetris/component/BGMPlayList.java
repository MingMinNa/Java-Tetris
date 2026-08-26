package com.mingminna.tetris.component;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;
import java.nio.file.Path;


public class BGMPlayList {

    // Music Resources
    private static final List<String> tracks = List.of(
        "assets/music/tetris-theme-classical.mp3",
        "assets/music/tetris-theme-piano.mp3",
        "assets/music/tetris-theme-lullaby.mp3"
    );

    private static int currentIndex = 0;
    private static boolean started = false;
    private static boolean muted = false;
    private static MediaPlayer player = null;

    public static void start() 
    {
        if (!started) {
            playCurrent();
            started = true;
        }
    }
    
    public static void stop()
    {
        disposeCurrentPlayer();
        started = false;
    }

    public static boolean isMuted()
    {
        return muted;
    }

    public static void setMuted(boolean value)
    {
        muted = value;
        if (player != null) {
            player.setMute(muted);
        }
    }

    public static void toggleMute()
    {
        setMuted(!muted);
    }

    public static String getCurrentTrackName()
    {
        String basename = Path.of(tracks.get(currentIndex)).getFileName().toString();
        String name = basename.substring(0, basename.lastIndexOf('.'));
        return name;
    }

    private static void playCurrent() 
    {
        disposeCurrentPlayer();

        String path = BGMPlayList.class.getResource(
            "/" + tracks.get(currentIndex)).toExternalForm();
            
        Media media = new Media(path);
        player = new MediaPlayer(media);

        player.setMute(muted);
        player.setOnEndOfMedia(BGMPlayList::playNext);
        player.play();
    }

    private static void playNext() 
    {
        currentIndex = (currentIndex + 1) % tracks.size();
        playCurrent();
    }

    private static void disposeCurrentPlayer()
    {
        if (player != null) {
            player.setOnEndOfMedia(null);
            player.stop();
            player.dispose();
            player = null;
        }
    }
}