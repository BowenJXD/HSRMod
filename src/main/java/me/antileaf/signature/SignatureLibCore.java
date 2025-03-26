package me.antileaf.signature;

import basemod.BaseMod;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import me.antileaf.signature.utils.internal.ConfigHelper;
import me.antileaf.signature.utils.internal.MiscHelper;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

@SpireInitializer
public class SignatureLibCore implements EditStringsSubscriber, PostInitializeSubscriber {
	public SignatureLibCore() {
		BaseMod.subscribe(this);
	}

	@SuppressWarnings("unused")
	public static void initialize() {
		new SignatureLibCore();
		ConfigHelper.loadConfig();
	}

	@Override
	public void receiveEditStrings() {
		String lang = MiscHelper.getLangShort();

		String path = "SignatureLib/localization/" + lang + "/ui.json";
		BaseMod.loadCustomStringsFile(UIStrings.class, path);
	}

	@Override
	public void receivePostInitialize() {
		UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SignatureLib:ModPanel");

		BaseMod.registerModBadge(
				ImageMaster.loadImage("SignatureLib/badge.png"),
				uiStrings.TEXT[0],
				"antileaf",
				uiStrings.TEXT[1],
				ConfigHelper.createConfigPanel()
		);
	}

	public static void postPostInitialize() {
		SignatureHelperInternal.initLibraryTypeNotice();
	}
}
