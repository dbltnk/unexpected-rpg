import java.io.File;
import java.io.IOException;

import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.format.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * plays a WAVE or MP3 audio file
 * (detects the type automatically
 *  from the file extension)
 */
public class AudioPlayer extends Thread {
	
    public AudioPlayer(String valFilename) {
    	String extension = null;
    	int i = valFilename.lastIndexOf('.');
    	if (i > 0) {
    	    extension = valFilename.substring(i+1);
    	}
    	
    	if (extension == null) {
    		System.out.println("Unknown file type: " + valFilename);
    	} else if (extension.equals("wav") || extension.equals("wave")) {
    		playWave(valFilename);
    	} else if (extension.equals("mp3")) {
    		playMP3(valFilename);
    	} else {
    		System.out.println("Unknown file type: " + valFilename);
    	}
    }
        
    public void playWave(String filename) {
    	AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(new File(filename));
			Clip clip = AudioSystem.getClip();
	    	clip.open(audioIn);
	    	clip.start();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void playMP3(String filename) {
		Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		Format input2 = new AudioFormat(AudioFormat.MPEG);
		Format output = new AudioFormat(AudioFormat.LINEAR);
		PlugInManager.addPlugIn(
			"com.sun.media.codec.audio.mp3.JavaDecoder",
			new Format[]{input1, input2},
			new Format[]{output},
			PlugInManager.CODEC
		);
		try{
			Player player = Manager.createPlayer(new MediaLocator(new File(filename).toURI().toURL()));
			player.start();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
    
    public static void main(String[] args) {
    	new AudioPlayer("sound/birds.mp3");
    	new AudioPlayer("sound/talk_inside.wav");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
