import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SceneLoader {

	private HashMap<String,JSONObject> sceneMap;
	
	private String nextScene;
	
	private View view;
	
	private static char pressedKey = '0';
	private static boolean isKeyPressed = false;
	
	private static boolean exitMenu = false;
	
	private static boolean fullscreenSwitch = false;
	
	private HashMap<String,AudioPlayer> musicMap = new HashMap<String,AudioPlayer>();
	
	private HashMap<String,AudioPlayer> soundMap = new HashMap<String,AudioPlayer>();
	
	public static final int STD_FADEOUT_TIME = 1000;
	
	public void setNextScene(String nextScene)
	{
		this.nextScene = nextScene;
	}
	
	public String getNextScene()
	{
		return this.nextScene;
	}

	/** reads in a complete file and returns the input as a string */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	public void outputScene(String sceneName) {
		try {
			JSONObject scene = this.sceneMap.get(sceneName);
			System.out.println(scene.toString());
		} catch (NullPointerException e) {
			System.out.println("ERROR: Scene with the name '" + sceneName + "' does not exist.");
			System.out.println(e);
		}
	}
	
	/** returns true if there was a next scene to load */
	public boolean loadNextScene()
	{
		if(this.nextScene != "")
		{
			return loadScene(this.nextScene);
		}
		else
		{
			return false;
		}
	}
		
	/** returns true if the scene could be loaded */
	public boolean loadScene(String sceneName)
	{
		setNextScene("");
		char[] choiceKeys = {'a','b','x','y'};
		
		
		JSONObject scene = new JSONObject();
	    
	    // get scene information
		try {
			scene = this.sceneMap.get(sceneName);
		} catch (NullPointerException e) {
			System.out.println("ERROR: Scene with the name '" + sceneName + "' does not exist.");
			System.out.println(e);
			return false;
		}
		
		if(sceneName == "Init")
		{
			List<Effect> effects = new ArrayList<Effect>();
			effects.addAll(Effect.getEffects(scene));
			execute(effects);
			return true;
		}
		
		//show situation
		JSONObject situation = scene.getJSONObject("situation");
		String situationText = situation.getString("text");
		if(situation.has("int_insert"))
		{
			JSONObject iv = situation.getJSONObject("int_insert");
			situationText = situationText.replace(iv.getString("replace"), ""+Party.getInt(iv.getString("insert")));
		}
		if(situation.has("string_insert"))
		{
			JSONObject iv = situation.getJSONObject("string_insert");
			situationText = situationText.replace(iv.getString("replace"), ""+Party.getString(iv.getString("insert")));
		}
		//System.out.println(situationText);
		//view.setMessage(situationText);
		if(situation.has("background"))
		{
			view.changeBackground(situation.getString("background"));
		}
		if(situation.has("reset_images"))
		{
			if(situation.getBoolean("reset_images"))
			{
				view.resetImagePositionings();
			}
		}
		if(situation.has("images"))
		{
			JSONArray images = situation.getJSONArray("images");
			for(int i=0; i<images.length(); i++)
			{
				JSONObject image = images.getJSONObject(i);
				if(image.has("conditions"))
				{
					JSONArray conditions = image.getJSONArray("conditions");
					if(!Condition.ConditionsTrue(conditions))
					{
						continue;
					}
				}
				String label = image.getString("image");
				int x = image.getInt("x");
				int y = image.getInt("y");
				view.addImagePositioning(label, x, y);
			}
		}
		if(situation.has("stop_music"))
		{
			if(situation.getBoolean("stop_music"))
			{
				stopAllMusic(STD_FADEOUT_TIME);
			}
		}
		if(situation.has("stop_sound"))
		{
			if(situation.getBoolean("stop_sound"))
			{
				stopAllSounds(STD_FADEOUT_TIME);
			}
		}
		if(situation.has("music"))
		{
			String musicFile = situation.getString("music");
			if(situation.has("music_volume"))
			{
				float volume = (float)situation.getDouble("music_volume");
				playMusic(musicFile, volume);
			}
			else
			{
				playMusic(musicFile);
			}
		}
		if(situation.has("sound"))
		{
			String soundFile = situation.getString("sound");
			if(situation.has("sound_volume"))
			{
				float volume = (float)situation.getDouble("sound_volume");
				playSound(soundFile, volume);
			}
			else
			{
				playSound(soundFile);
			}
		}
		
		// create list of effects
		List<Effect> effects = new ArrayList<Effect>();

		List<Choice> choices = Choice.getChoices(scene);
		if (choices.size() > 0) {
			int count = 0;
			List<String> optionStrings = new ArrayList<String>();
			for (Choice choice : choices) {
				System.out.println("[" + choiceKeys[count] + "] " + choice.getTag());
				optionStrings.add(choice.getTag());
				count++;
			}
			
			String[] textParts = situationText.split("\n");
			for(int i=0; i<textParts.length; i++)
			{
				System.out.println(textParts[i]);
				view.setMessage(textParts[i]);
				if(i != textParts.length-1)
					getKeyboardInput();
				else
					view.displayOptions(optionStrings);
			}
			
			int selection = 0;
			outerloop:
		    while (true) {
		        System.out.print("> ");
		        //String input = getInputLine();
		        char input = getKeyboardInput();
		        
				selection = 0;
				for (char c : choiceKeys) {
					//if (input.equals(""+c) && selection < choices.size()) {
					if (input == c && selection < choices.size()) {
						break outerloop;
					}
					selection++;
				}
				System.out.println("Invalid choice.");
			}
			
			//System.out.println("Selection: " + selection);
							
			// handle user input
			Choice choice = choices.get(selection);
			effects.addAll(choice.getEffects());
		}
		else
		{
			view.hideOptions();
			String[] textParts = situationText.split("\n");
			for(int i=0; i<textParts.length; i++)
			{
				System.out.println(textParts[i]);
				view.setMessage(textParts[i]);
				getKeyboardInput();
			}
		}
		
		effects.addAll(Effect.getEffects(scene));
		execute(effects);
		
		return true;
	}
	
	public void execute(List<Effect> effects)
	{
		for (Effect effect : effects) {
			if(!effect.conditionsTrue())
			{
				continue;
			}
			JSONObject json = effect.getJson();
			switch(json.getString("action"))
			{
			case Effect.ACTION_CHANGE_SCENE:
			{
				String newScene = json.getString("scene");
				//System.out.println("Debug: set next scene to " + newScene);
				setNextScene(newScene);
				break;
			}
			case Effect.ACTION_SET_TEXT:
			{
				//System.out.println("Debug: set text");
				String text = json.getString("text");
				if(json.has("int_insert"))
				{
					JSONObject iv = json.getJSONObject("int_insert");
					text = text.replace(iv.getString("replace"), ""+Party.getInt(iv.getString("insert")));
				}
				if(json.has("string_insert"))
				{
					JSONObject iv = json.getJSONObject("string_insert");
					text = text.replace(iv.getString("replace"), Party.getString(iv.getString("insert")));
				}
				view.hideOptions();
				String[] textParts = text.split("\n");
				for(int i=0; i<textParts.length; i++)
				{
					System.out.println(textParts[i]);
					view.setMessage(textParts[i]);
					getKeyboardInput();
				}
				break;
			}
			case Effect.ACTION_ADD_TEXT:
			{
				//System.out.println("Debug: add text");
				String text = json.getString("text");
				if(json.has("int_insert"))
				{
					JSONObject iv = json.getJSONObject("int_insert");
					text = text.replace(iv.getString("replace"), ""+Party.getInt(iv.getString("insert")));
				}
				if(json.has("string_insert"))
				{
					JSONObject iv = json.getJSONObject("string_insert");
					text = text.replace(iv.getString("replace"), Party.getString(iv.getString("insert")));
				}
				System.out.println(text);
				view.addMessage(text);
				break;
			}
			case Effect.ACTION_ADD_ITEM:
			{
				String item = json.getString("item");
				System.out.println("Got item: ["+item+"]");
				Inventory.addItem(item);
				if(json.has("image"))
				{
					view.addItem(json.getString("image"));
				}
				break;
			}
			case Effect.ACTION_REMOVE_ITEM:
			{
				String item = json.getString("item");
				System.out.println("Removed item: ["+item+"]");
				Inventory.removeItem(item);
				if(json.has("image"))
				{
					view.removeItem(json.getString("image"));
				}
				break;
			}
			case Effect.ACTION_SET_CONDITION:
			{
				Condition.setCondition(json.getString("condition"));
				break;
			}
			case Effect.ACTION_UNSET_CONDITION:
			{
				Condition.unsetCondition(json.getString("condition"));
				break;
			}
			case Effect.ACTION_SET_INT:
			{
				String name = json.getString("name");
				int value = json.getInt("value");
				Party.setInt(name, value);
				view.setGold(""+Party.getInt("gold"));
				view.setBattlepoints(""+Party.getInt("battle points"));
				break;
			}
			case Effect.ACTION_ADD_INT:
			{
				String name = json.getString("name");
				int value = json.getInt("value");
				Party.addInt(name, value);
				view.setGold(""+Party.getInt("gold"));
				view.setBattlepoints(""+Party.getInt("battle points"));
				break;
			}
			case Effect.ACTION_SET_STRING:
			{
				String name = json.getString("name");
				String value = json.getString("value");
				Party.setString(name, value);
				break;
			}
			case Effect.ACTION_IMPORT_IMAGE:
			{
				String filename = json.getString("filename");
				String label = json.getString("label");
				view.addImage(filename, label);
				break;
			}
			case Effect.ACTION_SET_BACKGROUND:
			{
				String file = json.getString("image");
				view.changeBackground(file);
				break;
			}
			case Effect.ACTION_ADD_FOREGROUND_IMAGE:
			{
				String label = json.getString("image");
				int x = json.getInt("x");
				int y = json.getInt("y");
				view.addImagePositioning(label, x, y);
				break;
			}
			case Effect.ACTION_RESET_IMAGES:
			{
				view.resetImagePositionings();
				break;
			}
			case Effect.ACTION_RESET_ALL:
			{
				stopAllMusic(STD_FADEOUT_TIME);
				stopAllSounds(STD_FADEOUT_TIME);
				Party.clear();
				Inventory.clear();
				Condition.clear();
				view.resetImagePositionings();
				view.clearItems();
				view.setGold(""+Party.getInt("gold"));
				view.setBattlepoints(""+Party.getInt("battle points"));
				break;
			}
			case Effect.ACTION_MUSIC:
			{
				if(json.has("stop_music") && json.getBoolean("stop_music"))
				{
					stopAllMusic(STD_FADEOUT_TIME);
				}
				if(json.has("stop_sound") && json.getBoolean("stop_sound"))
				{
					stopAllSounds(STD_FADEOUT_TIME);
				}
				if(json.has("music"))
				{
					String musicFile = json.getString("music");
					if(json.has("music_volume"))
					{
						float volume = (float)json.getDouble("music_volume");
						playMusic(musicFile, volume);
					}
					else
					{
						playMusic(musicFile);
					}
				}
				break;
			}
			case Effect.ACTION_SOUND:
			{
				if(json.has("stop_music") && json.getBoolean("stop_music"))
				{
					stopAllMusic(STD_FADEOUT_TIME);
				}
				if(json.has("stop_sound") && json.getBoolean("stop_sound"))
				{
					stopAllSounds(STD_FADEOUT_TIME);
				}
				if(json.has("sound"))
				{
					String soundFile = json.getString("sound");
					if(json.has("sound_volume"))
					{
						float volume = (float)json.getDouble("sound_volume");
						playSound(soundFile, volume);
					}
					else
					{
						playSound(soundFile);
					}
				}
				break;
			}
			default:
				System.out.println("Debug: effect " + json.getString("action") + " unknown.");	
			}
		}
	}
	
	public SceneLoader(String inputFile) {
		try {
			// read in file
			String jsonString = SceneLoader.readFile(inputFile,Charset.defaultCharset());
			/*System.out.println("-------------------------");
			System.out.println("Load " + inputFile);
			System.out.println("-------------------------");
			System.out.println(jsonString);*/
			System.out.println("-------------------------");
			
			// create the HashMap
			this.sceneMap = new HashMap<String,JSONObject>();
			
			// convert string to JSON object
			JSONObject scenes = new JSONObject(jsonString);
			
			// get all scene names
			String[] sceneNames = JSONObject.getNames(scenes);
			
			// iterate over all scenes
			for (String name : sceneNames) {
				this.sceneMap.put(name,scenes.getJSONObject(name));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// add gamepad support and the corresponding listener
		ControllerSupport cs = new ControllerSupport();
		cs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamepadKeyPressed(e);
			}
        });
		cs.start();
		
		// create the view
		view = new View();
	}
	
	/*public static String getInputLine()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
		try {
			input = br.readLine();
		} catch (IOException e) {
			System.out.println(e);
		}
		return input;
	}*/
	
	public void playSound(String soundFile) {
		playSound(soundFile, 0.8f);
	}
	
	public void playSound(String soundFile, float volume) {
		//System.out.println(soundFile);
		AudioPlayer newSound = new AudioPlayer(soundFile, volume);
		if (soundMap.containsKey(soundFile)) {
			soundMap.get(soundFile).interrupt();
			soundMap.remove(soundFile);
		}
		this.soundMap.put(soundFile,newSound);
	}
	
	public void playMusic(String musicFile) {
		playMusic(musicFile, 0.8f);
	}
	
	public void playMusic(String musicFile, float volume) {
		//System.out.println(musicFile);
		AudioPlayer newMusic = new AudioPlayer(musicFile, volume);
		if (musicMap.containsKey(musicFile)) {
			musicMap.get(musicFile).interrupt();
			musicMap.remove(musicFile);
		}
		this.musicMap.put(musicFile,newMusic);
	}
	
	public synchronized void stopAllSounds(int fadeOutTime) {
		for (String key : this.soundMap.keySet()) {
			if (fadeOutTime > 0) {
				this.soundMap.get(key).fadeOut(fadeOutTime);
			} else {
				this.soundMap.get(key).stopAudio();
			}
			this.soundMap.remove(key);
		}
	}
	
	public synchronized void stopAllMusic(int fadeOutTime) {
		for (String key : this.musicMap.keySet()) {
			if (fadeOutTime > 0) {
				this.musicMap.get(key).fadeOut(fadeOutTime);
			} else {
				this.musicMap.get(key).stopAudio();
			}
			this.musicMap.remove(key);
		}
	}
	
	public synchronized static void keyPressed(KeyEvent e)
	{
		pressedKey = e.getKeyChar();
		isKeyPressed = true;
	}
	
	public synchronized static void gamepadKeyPressed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("a") || (command.equals("b")) || (command.equals("x")) || (command.equals("y"))) {
			pressedKey = command.toCharArray()[0];
			System.out.println("CHAR:" + command.toCharArray()[0]);
			isKeyPressed = true;
		}
	}
	
	private char getKeyboardInput()
	{
		isKeyPressed = false;
		while(!isKeyPressed || exitMenu)
		{
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(isKeyPressed && exitMenu)
			{
				if(pressedKey == 'y')
				{
					System.exit(0);
				}
				else if(pressedKey == 'n')
				{
					exitMenu = false;
					view.setEscapeDialog(false);
					isKeyPressed = false;
				}
			}
			else if (isKeyPressed)
			{
				if(pressedKey == KeyEvent.VK_ESCAPE)
				{
					exitMenu = true;
					view.setEscapeDialog(true);
					isKeyPressed = false;
					// just for testing the audio player component_
					//new AudioPlayer("sound/birds.mp3");
				} else if (pressedKey == KeyEvent.VK_ENTER) {
					view.toggleFullscreen();
				}
			}
		}
		return pressedKey;
	}
	
	public static void main(String[] args) {
		//System.out.println("Working Directory = " + System.getProperty("user.dir"));
		SceneLoader sceneLoader = new SceneLoader("./Story.json");
		sceneLoader.setNextScene("Init");
		
		boolean end = false;
		while (!end)
		{
			end = !sceneLoader.loadNextScene();
			//Condition.printAllConditions();
		}
		System.out.println("Debug: no next scene");
	}
}
