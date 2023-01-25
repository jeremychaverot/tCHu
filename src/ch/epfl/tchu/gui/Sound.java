package ch.epfl.tchu.gui;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Cristian Safta (324694)
 */
public class Sound {
    private Clip soundClip;
    
    
    public Sound(String soundFileName) {
        
        
        try {
            File file = new File(soundFileName);
            AudioInputStream soundStream = AudioSystem.getAudioInputStream(file);
            soundClip = AudioSystem.getClip();
            soundClip.open(soundStream);
            
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void play() {
       
        if(!soundClip.isRunning()) {
            soundClip.setFramePosition(0);
            soundClip.start();
        }
        
    }

    public void stop() {
        soundClip.stop();
    }

    public void playOnLoop() {
        
        if(!soundClip.isRunning()) {
            soundClip.setFramePosition(0);
            soundClip.loop(Clip.LOOP_CONTINUOUSLY);
            soundClip.start();
        }
        
    }

}
