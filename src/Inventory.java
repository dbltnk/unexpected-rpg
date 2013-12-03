import java.util.ArrayList;
import java.util.List;


public class Inventory
{
	private static List<String> items = new ArrayList<String>();
	
	public static void addItem(String name)
	{
		items.add(name);
	}
	
	public static void removeItem(String name)
	{
		if(hasItem(name))
		{
			items.remove(name);
		}
		else
		{
			System.out.println("Debug: Item "+name+" could not be removed because it was not in the inventory.");
		}
	}
	
	public static boolean hasItem(String name)
	{
		return items.contains(name);
	}
}
