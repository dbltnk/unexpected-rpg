import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class Choice {

	private String tag;
	
	private List<Effect> effects;
	
	public static List<Choice> getChoices(JSONObject json)
	{
		List<Choice> resultList = new ArrayList<Choice>();
		if (json.has("choice")) {
			JSONArray choicesArray = json.getJSONArray("choice");
			for (int i=0; i < choicesArray.length(); i++) {
				JSONObject choice = choicesArray.getJSONObject(i);
				if(choice.has("conditions"))
				{
					JSONArray conditions = choice.getJSONArray("conditions");
					if(!Condition.ConditionsTrue(conditions))
					{
						continue;
					}
				}
				resultList.add(new Choice(choicesArray.getJSONObject(i)));
			}
		}
		return resultList;
	}
	
	public Choice(JSONObject json)
	{
		this.tag = json.getString("tag");
		this.effects = Effect.getEffects(json);
	}
	
	public String getTag()
	{
		return tag;
	}

	public List<Effect> getEffects()
	{
		return effects;
	}
	
}
