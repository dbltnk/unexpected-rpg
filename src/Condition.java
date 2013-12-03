import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class Condition {
	
	private static List<String> conditionsSet = new ArrayList<String>();
	
	static boolean ConditionTrue(String condition)
	{
		String[] splitted = condition.split(":");
		switch(splitted[0])
		{
		case "ITEM":
		{
			return Inventory.hasItem(splitted[1]);
		}
		case "NO_ITEM":
		{
			return !Inventory.hasItem(splitted[1]);
		}
		case "SET":
		{
			//System.out.println(condition+" "+conditionsSet.contains(splitted[1]));
			return conditionsSet.contains(splitted[1]);
		}
		case "UNSET":
		{
			//System.out.println(condition+" "+!conditionsSet.contains(splitted[1]));
			return !conditionsSet.contains(splitted[1]);
		}
		default:
		{
			System.out.println("condition "+condition+" not parseable or not known.");
		}
		}
		return true;
	}
	
	static boolean ConditionsTrue(JSONArray conditions)
	{
		for(int i=0; i<conditions.length(); i++)
		{
			if(!Condition.ConditionTrue(conditions.getString(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	static void setCondition(String name)
	{
		conditionsSet.add(name);
	}
	
	static void unsetCondition(String name)
	{
		conditionsSet.remove(name);
	}
	
	static void printAllConditions()
	{
		System.out.println("conditions set:");
		for(int i=0; i<conditionsSet.size(); i++)
		{
			System.out.print(conditionsSet.get(i));
			System.out.print(",");
		}
		System.out.println();
	}
}
