package me.kinomoto.proteam;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
