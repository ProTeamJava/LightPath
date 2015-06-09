package me.kinomoto.proteam;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Messages class is responsible for multilingualism of the program. The system language is being checked. 
 * If Polish is detected, the Polish version is provided. 
 * Otherwise there is the English version. 
 * The class uses the files:
 * {@docRoot} messages_en_US.properties
 * {@docRoot} messages_pl_PL.properties
 *
 */
public class Messages {
	private static final String PREFIX = "me.kinomoto.proteam.messages";
	private static String bundleName = PREFIX + "_en_US";

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);

	private Messages() {
	}

	public static void setLocale(Locale loc) {
		bundleName = PREFIX + "_" + loc.getLanguage() + "_" + loc.getCountry();
		try {
			resourceBundle = ResourceBundle.getBundle(bundleName);
		} catch (Exception e) {
			resourceBundle = ResourceBundle.getBundle(PREFIX + "_en_US");
		}
	}

	public static String get(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static char getChar(String key) {
		try {
			return resourceBundle.getString(key).charAt(0);
		} catch (Exception e) {
			return 0;
		}
	}
}
