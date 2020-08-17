package test;

import java.util.*;

public class UniqueId {

	private final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public String generateId(int count) {
		
		String str="";
		while (count-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			str += ALPHA_NUMERIC_STRING.charAt(character);
		}
		return str;
	}
	
}
