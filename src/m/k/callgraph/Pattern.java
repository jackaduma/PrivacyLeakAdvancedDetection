package m.k.callgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author kun_ma
 * 
 */
public class Pattern {

	/*
	 * 
	 */
	public static Map<String, List<String>> actionPattern = new HashMap<String, List<String>>();
	static {
		String key = "Send SMS";
		List<String> methodsList = new ArrayList<String>();
		methodsList.add("Landroid/telephony/SmsManager;->sendTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V");
		actionPattern.put(key, methodsList);
	}

	/*
	 * 
	 */
	public static Map<String, List<String>> sourcePattern = new HashMap<String, List<String>>();
	static {
		String key = "SMS";
		List<String> methodsList = new ArrayList<String>();
		methodsList.add("");
		sourcePattern.put(key, methodsList);

	}

	public static Map<String, List<String>> sinkPattern = new HashMap<String, List<String>>();
	static {
		String key = "Http";
		List<String> methodsList = new ArrayList<String>();
		methodsList.add("");
		sinkPattern.put(key, methodsList);

		methodsList = new ArrayList<String>();
		key = "SMS Sending";
		methodsList.add("Landroid/telephony/SmsManager;->sendTextMessage(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Landroid/app/PendingIntent;Landroid/app/PendingIntent;)V");
		sinkPattern.put(key, methodsList);

	}

	/*
	 * Remind user sink or not with a message box and so on.
	 */
	public static Map<String, List<String>> reminderPattern = new HashMap<String, List<String>>();
	static {
		String key = "Start Activity";

		key = "Popup Message Box";
	}
	
	/*
	 * action pattern -- type 1
	 * source pattern -- type 2
	 * sink pattern   -- type 3
	 * reminder pattern -- type 4
	 */
	public static List<String> getPatternList(int type) {
		List<String> patternList = new ArrayList<String>();

		Iterator<Entry<String, List<String>>> it = null;
		
		switch (type) {
		case 1:
			it = actionPattern.entrySet().iterator();
			break;
		case 2:
			it = sourcePattern.entrySet().iterator();
			break;
		case 3:
			it = sinkPattern.entrySet().iterator();
			break;
		case 4:
			it = reminderPattern.entrySet().iterator();
			break;
		default:
			break;
		}
				
		while (it.hasNext()) {
			patternList.addAll(it.next().getValue());
		}
		
		return patternList;
	}

	public static String getPatternKey(String method, int type) {
		Iterator<Entry<String, List<String>>> it = null;
		
		switch (type) {
		case 1:
			it = actionPattern.entrySet().iterator();
			break;
		case 2:
			it = sourcePattern.entrySet().iterator();
			break;
		case 3:
			it = sinkPattern.entrySet().iterator();
			break;
		case 4:
			it = reminderPattern.entrySet().iterator();
			break;
		default:
			break;
		}
		
		while (it.hasNext()) {
			Entry<String, List<String>> entry = it.next();
			if (entry.getValue().contains(method)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
