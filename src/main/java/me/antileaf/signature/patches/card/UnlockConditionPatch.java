package me.antileaf.signature.patches.card;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.annotations.SerializedName;
import com.megacrit.cardcrawl.localization.CardStrings;

@SuppressWarnings("unused")
public class UnlockConditionPatch {
	@SpirePatch(clz = CardStrings.class, method = SpirePatch.CLASS)
	public static class Fields {
		@SerializedName("SIGNATURE_UNLOCK_CONDITION")
		public static SpireField<String> unlockCondition = new SpireField<>(() -> null);
	}
}
