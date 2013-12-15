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
	
	private String type;
	
	private Clip waveClip;
	
	private Player mp3Player;
	
    public AudioPlayer(String valFilename) {
    	String extension = null;
    	int i = valFilename.lastIndexOf('.');
    	if (i > 0) {
    	    extension = valFilename.substring(i+1);
    	}
    	
    	File file = new File(valFilename);
    	if(!file.exists()) {
    		System.out.println("The audio file does not exist: " + valFilename);
    	} else if (extension == null) {
    		System.out.println("Unknown file type: " + valFilename);
    	} else if (extension.equals("wav") || extension.equals("wave")) {
    		playWave(file);
    	} else if (extension.equals("mp3")) {
    		playMP3(file);
    	} else {
    		System.out.println("Unknown file type: " + valFilename);
    	}
    }
        
    public void playWave(File file) {
    	this.type = "wav";
    	AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(file);	
			this.waveClip = AudioSystem.getClip();
			this.waveClip.open(audioIn);
			this.waveClip.start();
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
    
    public void playMP3(File file) {
    	this.type = "mp3";
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
			this.mp3Player = Manager.createPlayer(new MediaLocator(file.toURI().toURL()));
			this.mp3Player.start();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
    
    public void stopAudio() {
    	if (this.type.equals("mp3") && this.mp3Player != null) {
    		this.mp3Player.stop();
    	} else if (this.waveClip != null) {
    		this.waveClip.stop();
    	}
    }
    
    public static void main(String[] args) {
    	new AudioPlayer("sound/birds.mp3");
    	//new AudioPlayer("sound/talk_inside.wav");
    	//new AudioPlayer("music/critical.wav");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
