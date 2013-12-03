//import net.sf.json.JSONObject;
//import net.sf.json.JSONSerializer;
//import org.json;
//import org.apache.commons.lang.exception.NestableRuntimeException;
//import org.json.JSONObject;

import org.json.JSONObject;

public class TestClass {
	
	public static void main(String[] args) {
		String str = "{'string':'JSON', 'integer': 1, 'double': 2.0, 'boolean': true}";  
//		JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON( str );  
//		String value = jsonObject.getString( "string" );
//		System.out.println(value);
		
		JSONObject obj = new JSONObject(str);
		
		System.out.println(obj.toString());
		
		String value = obj.getString("string");
		System.out.println(value);
		
	}
	
}
