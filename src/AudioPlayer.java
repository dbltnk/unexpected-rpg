import java.io.File;
import java.io.IOException;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.GainControl;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.PlugInManager;
import javax.media.RealizeCompleteEvent;
import javax.media.format.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * plays a WAVE or MP3 audio file
 * (detects the type automatically
 *  from the file extension)
 */
public class AudioPlayer extends Thread implements ControllerListener {
	
	private String type;
	
	//private Clip waveClip;
	
	private Player player;
	
	//private FloatControl waveGainControl;
	private GainControl gainControl;

	private float startVolume;
	
	/*
	private float waveStartVolumeBias;
	private float mp3StartVolume;
	*/
	
	private int fadeOutTime;
	
	public AudioPlayer(String valFilename) {
    	createPlayer(valFilename,0.8f);
	}
	
    public AudioPlayer(String valFilename, float startVolume) {
    	createPlayer(valFilename,startVolume);
    }

    /** creates the player object and calls the play method */
    public void createPlayer(String valFilename, float startVolume) {
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
    		this.type = "wav";
    		//this.waveStartVolumeBias = (startVolume - 0.5f) * 10;
    		//playWave(file);
    		this.startVolume = startVolume;
    		play(file);
    	} else if (extension.equals("wav") ||extension.equals("mp3")) {
    		this.type = "mp3";
    		this.startVolume = startVolume;
    		play(file);
    	} else {
    		System.out.println("Unknown file type: " + valFilename);
    	}
    }
    
    /** returns the type of the audio file */
    public String getType() {
    	return this.type;
    }
        
    /*
    public void playWave(File file) {
    	AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(file);	
			this.waveClip = AudioSystem.getClip();
			this.waveClip.open(audioIn);
			this.waveGainControl = (FloatControl) this.waveClip.getControl(FloatControl.Type.MASTER_GAIN);
			//volume.setValue(this.waveStartVolumeBias);
			adjustWaveVolume(this.waveStartVolumeBias);
			this.waveClip.start();
			//this.waveGainControl = (FloatControl) this.waveClip.getControl(FloatControl.Type.MASTER_GAIN);
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
    */
    
    /** plays the audio file */
    public void play(File file) {
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
			this.player = Manager.createPlayer(new MediaLocator(file.toURI().toURL()));
			this.player.addControllerListener(this);
			this.player.realize();
			this.player.start();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
    
    /** stops the audio immediately */
    public void stopAudio() {
    	if (this.player != null) {
    		this.player.stop();
    		this.player = null;
    	}
    	/*if (this.type.equals("mp3") && this.mp3Player != null) {
    		this.mp3Player.stop();
    	} else if (this.waveClip != null) {
    		this.waveClip.stop();
    	}*/
    }
    
    /*/** the volume of wave files can only be adjusted
    public void adjustWaveVolume(float value) {
    	if (getType().equals("wav") && this.waveGainControl != null) {
    		this.waveGainControl.setValue(value);
    	} else {
    		System.out.println("ERROR: No gain control available!");
    	}
    }*/
    
    /*/** the volume of mp3 files must be set to a value between 0 and 1.0
    public void setMP3Volume(float value) {
    	if (getType().equals("mp3") && this.mp3GainControl != null) {
    		this.mp3GainControl.setLevel(value);
    	} else {
    		System.out.println("ERROR: No gain control available!");
    	}
    }*/
    
    /** determines the volume of the audio file */
    public void setVolume(float value) throws IllegalArgumentException {
    	if (this.gainControl != null) {
    		this.gainControl.setLevel(value);
    	} else {
    		System.out.println("ERROR: No gain control available!");
    	}
    }
    
    /** fades out the audio file by starting the thread */
    public void fadeOut(int milliseconds) {
    	this.fadeOutTime = milliseconds;
    	this.start();
    }

    /** run method of the thread: fades out the audio file, then destroys the player object */
    public void run() {
    	/*if (this.type.equals("wav") && this.waveClip != null) {
    		System.out.println("Fade out wave...");
    		for (int i=0; i<50; i++) {
        		try {
        			float adjustValue = (float) -Math.pow(1.2,i);
        			if (adjustValue < -80.0f) {
        				adjustValue = -80.0f;
        			}
        			adjustWaveVolume(adjustValue);
        			//System.out.println(i);
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	}
    		this.waveClip.stop();
    		this.waveClip = null;
    	}
    	if (this.type.equals("mp3") && this.mp3Player != null) {*/
    	if (this.player != null) {
    		System.out.println("Fade out...");
    		float startVolumeLevel = this.gainControl.getLevel();
    		//System.out.println("Start volume level: " + startVolumeLevel);
    		float minValue = -0.5f;
    		float maxValue = 0.8f;	
    		if (this.fadeOutTime <= 1000) {
    			this.fadeOutTime = 1000;
    		}
    		int stepCount = 50;
    		int sleepTime = this.fadeOutTime / 50;
    		float step = (startVolumeLevel - minValue) / stepCount;
    		for (int i=0; i<stepCount; i++) {
        		try {
        			//float volumeLevel = startVolumeLevel - ((float) Math.pow(1.1,i) * 0.05f);
        			float volumeLevel = startVolumeLevel - step*i;
        			//System.out.println(i +" / " + volumeLevel);
        			if (volumeLevel < minValue) {
        				volumeLevel = minValue;
        			} else if (volumeLevel > maxValue) {
        				volumeLevel = maxValue;
        			}
        			//System.out.println(i +" / " + volumeLevel);
        			setVolume(volumeLevel);
    				Thread.sleep(sleepTime);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	}
    		this.player.stop();
    		this.player = null;
    	}
    	//System.out.println("Finish");
    }
    
    /** when the player is realized, the gainControl object can be obtained */
    @Override
	public void controllerUpdate(ControllerEvent event) {
		if (event instanceof RealizeCompleteEvent) {
			this.gainControl = this.player.getGainControl();
			try {
				setVolume(this.startVolume);
			} catch (IllegalArgumentException e) {
				System.out.println("Illegal volume value: " + this.startVolume);
			}
		    /*FloatControl gainControl = (FloatControl) mp3Player.getControl(FloatControl.Type.MASTER_GAIN);
		    //int gainLevel= (int) ((int)gainControl.getMinimum()+((gainControl.getMaximum()-gainControl.getMinimum())/100*gainPercent));
		    //gainControl.setValue(gainLevel);
		    System.out.println(gainControl.getMinimum() + " - " + gainControl.getMaximum());
			System.exit(0);*/
		}
	}
    
    /** main method for testing */
    public static void main(String[] args) {
    	AudioPlayer audioPlayer = new AudioPlayer("music/critical.mp3",0.8f);
    	AudioPlayer audioPlayer2 = new AudioPlayer("sound/talk_inside.mp3",0.8f);
		try {
			Thread.sleep(2000);
			audioPlayer.fadeOut(5000);
			audioPlayer2.fadeOut(5000);
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	
}
