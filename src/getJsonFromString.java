import java.util.ArrayList;
import java.util.HashMap;


public class getJsonFromString {

	public static void main (String[] srgs) {
		String json = "{\"key1\":\"value1\",\"key2\":{\"key3\":0,\"key4\":true},\"key5\":[1,2]}";
		HashMap<String, Object> map = getJsonFromString(json);
		System.out.println("json :" + map.size());
		System.out.println("json :" + map.toString());
	}
	
	
	public static HashMap<String, Object> getJsonFromString(String jsonString) {
		HashMap<String, Object> result = new HashMap<>();
		int i = 0;
		while(i < jsonString.length() - 1) {
			int nextKeyIndex = i;
			i = jsonString.indexOf(":", i);
			if (i == -1) {
				return result;
			}
			String key = getKey(jsonString.substring(nextKeyIndex, i));
			if ("".equals(key)) {
				return result;
			}
			String valueString = getValueString(jsonString.substring(i + 1));
			Object value = getValue(valueString);
			if (value == null) {
				return result;
			}
			result.put(key, value);
			i = i + valueString.length() + 2;
		}
		return result;
	}
	
	private static String getKey(String keyString) {
		String key = "";
		if (keyString.endsWith("\"")) {
			key = keyString.substring(keyString.indexOf("\"") + 1, keyString.length()-1);
		}
		if(key.contains("\"")){
			key = "";
		}
		System.out.println("key = " + key);
		return key;
	}
	
	private static String getValueString(String jsonValueString){
		String result = "";
		if (jsonValueString.startsWith("{")) {
			int count = 0;
			for (int i = 0; i < jsonValueString.length(); i++) {
				if ("{".equals(String.valueOf(jsonValueString.charAt(i)))) {
					count++;
				} else if ("}".equals(String.valueOf(jsonValueString.charAt(i)))) {
					if (count == 1) {
						result = jsonValueString.substring(0, i + 1);
					}
					count--;
				}
			}
		} else if (jsonValueString.startsWith("[")){
			int count = 0;
			for (int i = 0; i < jsonValueString.length(); i++) {
				if ("[".equals(String.valueOf(jsonValueString.charAt(i)))) {
					count++;
				} else if ("]".equals(String.valueOf(jsonValueString.charAt(i)))) {
					if (count == 1) {
						result = jsonValueString.substring(0, i + 1);
					}
					count--;
				}
			}
		} else {
			int valueEndIndex = jsonValueString.indexOf(",");
			if (valueEndIndex == -1) {
				if (jsonValueString.endsWith("}")) {
					result = jsonValueString.substring(0, jsonValueString.length() - 1);
				} else {
					result = jsonValueString.substring(0, jsonValueString.length());
				}	
			} else {
				result = jsonValueString.substring(0, valueEndIndex);
			}
		}
		System.out.println("ValueString = " + result);
		return result;
	}
	
	private static Object getValue (String jsonValueString) {
		Object result = null;
		if (jsonValueString.startsWith("{")) {
			result = getJsonFromString(jsonValueString);
		} else if (jsonValueString.startsWith("[")){
			ArrayList<Object> list = new ArrayList<Object>();
			String tmp = jsonValueString.substring(1, jsonValueString.length() -1);
			while(tmp.length() > 0) {
				String valueString = getValueString(tmp);
				Object value = getValue(valueString);
				list.add(value);
				if (tmp.length() == valueString.length()) {
					tmp = "";
				} else {
					tmp = tmp.substring(valueString.length() + 1, tmp.length());
				}
			}
			result = list;
		} else if (jsonValueString.startsWith("\"")) {
			result = jsonValueString.substring(1, jsonValueString.length()-1);
		} else {
			try {
				result = Integer.parseInt(jsonValueString);
			} catch(NumberFormatException e) {
				if ("true".equals(jsonValueString.toLowerCase())
						|| "false".equals(jsonValueString.toLowerCase())) {
					result = Boolean.parseBoolean(jsonValueString);
				}
				result = jsonValueString;
			}
		}
		System.out.println("value = " + result.toString());
		return result;
	}
}
