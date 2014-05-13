package utils;

import java.util.Map;

import result.Result;

public class NamespaceAbbreviation {
	public static String getAbbreviatedString(String s, Result r) {
		if(r.getUnderlyingModel()!=null) {
			String result = s;
			for(Map.Entry<String,String> prefix : r.getUnderlyingModel().getNsPrefixMap().entrySet()) {
				if(result.startsWith(prefix.getValue()) && !prefix.getKey().startsWith("ns")) {
					result = result.replace(prefix.getValue(),prefix.getKey()+":");
				}
			}
			return result;
		}
		else {
			return s;
		}
	}
}
