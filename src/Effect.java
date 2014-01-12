import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class Effect {

	public static final String ACTION_CHANGE_SCENE = "next_scene";
	public static final String ACTION_SET_TEXT = "set_text";
	public static final String ACTION_ADD_TEXT = "add_text";
	public static final String ACTION_ADD_ITEM = "add_item";
	public static final String ACTION_REMOVE_ITEM = "remove_item";
	public static final String ACTION_SET_CONDITION = "set_condition";
	public static final String ACTION_UNSET_CONDITION = "unset_condition";
	public static final String ACTION_SET_INT = "set_int";
	public static final String ACTION_ADD_INT = "add_int";
	public static final String ACTION_SET_STRING = "set_string";
	public static final String ACTION_IMPORT_IMAGE = "import_image";
	public static final String ACTION_SET_BACKGROUND = "set_background";
	public static final String ACTION_ADD_FOREGROUND_IMAGE = "add_image";
	public static final String ACTION_RESET_IMAGES = "reset_images";
	public static final String ACTION_RESET_ALL = "reset_all";
	public static final String ACTION_MUSIC = "music";
	public static final String ACTION_SOUND = "sound";
	
	private JSONObject json;
	
	public static List<Effect> getEffects(JSONObject json)
	{
		List<Effect> resultList = new ArrayList<Effect>();
		if (json.has("effects"))
		{
			JSONArray effectsArray = json.getJSONArray("effects");
			for (int i=0; i < effectsArray.length(); i++)
			{
				Effect effect = new Effect(effectsArray.getJSONObject(i));
				resultList.add(effect);
			}
		}
		return resultList;
	}
	
	public Effect(JSONObject objJson)
	{
		this.json = objJson;
	}
	
	public boolean conditionsTrue()
	{
		if(json.has("conditions"))
		{
			JSONArray conditions = json.getJSONArray("conditions");
			for (int i=0; i < conditions.length(); i++)
			{
				String condition = conditions.getString(i);
				if(!Condition.ConditionTrue(condition))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public JSONObject getJson()
	{
		return this.json;
	}
	
}
