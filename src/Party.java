import java.util.HashMap;

import org.json.JSONObject;


public class Party {
	
	private static HashMap<String,Integer> ints = new HashMap<String,Integer>();
	private static HashMap<String,String> strings = new HashMap<String,String>();
	
	public static void clear()
	{
		ints.clear();
		strings.clear();
	}

	public static void setInt(String name, int value)
	{
		ints.put(name, value);
	}
	
	public static void addInt(String name, int value)
	{
		if(ints.containsKey(name))
		{
			ints.put(name, ints.get(name)+value);
		}
		else
		{
			ints.put(name, value);
		}
	}
	
	public static int getInt(String name)
	{
		if(ints.containsKey(name))
		{
			return ints.get(name);
		}
		else
		{
			return 0;
		}
	}
	
	public static void setString(String name, String value)
	{
		strings.put(name, value);
	}
	
	public static String getString(String name)
	{
		if(strings.containsKey(name))
		{
			return strings.get(name);
		}
		else
		{
			return "";
		}
	}
}
