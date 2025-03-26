package me.antileaf.signature.utils.internal;

import basemod.ModLabel;
import basemod.ModLabeledButton;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import me.antileaf.signature.utils.SignatureHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

public class ConfigHelper {
	public static final String ENABLE_DEBUGGING = "enableDebugging";
	public static final String SIGNATURE_UNLOCKED = "signatureUnlocked_";
	public static final String SIGNATURE_ENABLED = "signatureEnabled_";
	public static final String SIGNATURE_NOTICE = "signatureNotice_";
	
	public static SpireConfig conf = null;
	public static Map<String, String> strings;

	public static void loadConfig() {
		try {
			Properties defaults = new Properties();
			defaults.setProperty(ENABLE_DEBUGGING, "false");

			conf = new SpireConfig("SignatureLib", "config", defaults);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean enableDebugging() {
		return conf.getBool(ENABLE_DEBUGGING);
	}
	
	public static void setEnableDebugging(boolean enableDebugging) {
		conf.setBool(ENABLE_DEBUGGING, enableDebugging);
	}

	public static boolean isSignatureUnlocked(String id) {
		String key = SIGNATURE_UNLOCKED + id;
		return conf.has(key) && conf.getBool(key);
	}

	public static void setSignatureUnlocked(String id, boolean unlocked) {
		conf.setBool(SIGNATURE_UNLOCKED + id, unlocked);
		save();
	}

	public static boolean isSignatureEnabled(String id) {
		String key = SIGNATURE_ENABLED + id;
		return conf.has(key) && conf.getBool(key);
	}

	public static void setSignatureEnabled(String id, boolean enabled) {
		conf.setBool(SIGNATURE_ENABLED + id, enabled);
		save();
	}

	public static boolean signatureNotice(String id) {
		String key = SIGNATURE_NOTICE + id;
		return !conf.has(key) || conf.getBool(key); // default to true
	}

	public static void setSignatureNotice(String id, boolean notice) {
		conf.setBool(SIGNATURE_NOTICE + id, notice);
		save();
	}
	
	public static void save() {
		try {
			conf.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ModPanel createConfigPanel() {
		ModPanel panel = new ModPanel();
		
		Gson gson = new Gson();
		String json = Gdx.files.internal("SignatureLib/localization/" +
						MiscHelper.getLangShort() + "/config.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		strings = gson.fromJson(json, (new TypeToken<Map<String, String>>() {}).getType());
		
		float y = 700.0F;
		
		ModLabeledToggleButton enableDebuggingButton = new ModLabeledToggleButton(
				strings.get(ENABLE_DEBUGGING),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				FontHelper.charDescFont,
				enableDebugging(),
				panel,
				(modLabel) -> {},
				(button) -> {
					setEnableDebugging(button.enabled);
					save();
				}
		);
		panel.addUIElement(enableDebuggingButton);

		y -= 150.0F;

		ModLabeledButton resetNoticesButton = new ModLabeledButton(
				strings.get("resetNotices"),
				350.0F,
				y,
				Settings.CREAM_COLOR,
				Settings.BLUE_TEXT_COLOR,
				panel,
				(button) -> {
					SignatureHelperInternal.resetNotices();
				}
		);
		panel.addUIElement(resetNoticesButton);
		
		return panel;
	}
}
