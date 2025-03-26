package me.antileaf.signature.utils;

import com.megacrit.cardcrawl.cards.AbstractCard;
import me.antileaf.signature.interfaces.SignatureSubscriber;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class SignatureHelper {
	public static final Style DEFAULT_STYLE;

	public static void register(String id, Info info) {
		SignatureHelperInternal.register(id, info);
	}

	public static void unlock(String id, boolean unlock) {
		SignatureHelperInternal.unlock(id, unlock);
	}

	public static void enable(String id, boolean enable) {
		SignatureHelperInternal.enable(id, enable);
	}

	public static void noDebugging(String id) {
		SignatureHelperInternal.noDebugging(id);
	}

	public static void noDebuggingPrefix(String prefix) {
		SignatureHelperInternal.noDebuggingPrefix(prefix);
	}

	public static void addUnlockConditions(String jsonFile) {
		SignatureHelperInternal.addUnlockConditions(jsonFile);
	}

	public static boolean isUnlocked(String id) {
		return SignatureHelperInternal.isUnlocked(id);
	}

	public static boolean isEnabled(String id) {
		return SignatureHelperInternal.isEnabled(id);
	}

	public static void subscribe(SignatureSubscriber subscriber) {
		SignatureHelperInternal.subscribe(subscriber);
	}

	public static void unsubscribe(SignatureSubscriber subscriber) {
		SignatureHelperInternal.unsubscribe(subscriber);
	}

	public static class Info {
//		public final String img, portrait;
		public final Function<AbstractCard, String> img, portrait;
		public final Predicate<AbstractCard> shouldUseSignature;
		public final String parentID;
		public final boolean hideSCVPanel, dontAvoidSCVPanel;
		public final Style style;

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, String parentID,
					boolean hideSCVPanel, boolean dontAvoidSCVPanel, Style style) {
			this.img = img;
			this.portrait = portrait;
			this.shouldUseSignature = shouldUseSignature;
			this.parentID = parentID;
			this.hideSCVPanel = hideSCVPanel;
			this.dontAvoidSCVPanel = dontAvoidSCVPanel;
			this.style = style;
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature,
					boolean hideSCVPanel, boolean dontAvoidSCVPanel) {
			this(img, portrait, shouldUseSignature,
					null, hideSCVPanel, dontAvoidSCVPanel, DEFAULT_STYLE);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, Style style) {
			this(img, portrait, shouldUseSignature,
					null, false, false, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature) {
			this(img, portrait, shouldUseSignature, DEFAULT_STYLE);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait, Style style) {
			this(img, portrait, c -> true, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait) {
			this(img, portrait, DEFAULT_STYLE);
		}

		public Info(String img, String portrait, Predicate<AbstractCard> shouldUseSignature, Style style) {
			this(c -> img, c -> portrait, shouldUseSignature, style);
		}

		public Info(String img, String portrait, Predicate<AbstractCard> shouldUseSignature) {
			this(img, portrait, shouldUseSignature, DEFAULT_STYLE);
		}

		public Info(String img, String portrait) {
			this(c -> img, c -> portrait, c -> true);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, String parentID, Style style) {
			this(img, portrait, shouldUseSignature, parentID,
					false, false, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait,
					Predicate<AbstractCard> shouldUseSignature, String parentID) {
			this(img, portrait, shouldUseSignature, parentID, DEFAULT_STYLE);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait, String parentID, Style style) {
			this(img, portrait, c -> true, parentID, style);
		}

		public Info(Function<AbstractCard, String> img, Function<AbstractCard, String> portrait, String parentID) {
			this(img, portrait, parentID, DEFAULT_STYLE);
		}

		public Info(String img, String portrait, Predicate<AbstractCard> shouldUseSignature, String parentID) {
			this(c -> img, c -> portrait, shouldUseSignature, parentID);
		}

		public Info(String img, String portrait, String parentID, Style style) {
			this(c -> img, c -> portrait, parentID, style);
		}

		public Info(String img, String portrait, String parentID) {
			this(img, portrait, parentID, DEFAULT_STYLE);
		}
	}

	public static class Style {
		public String cardTypeAttackCommon, cardTypeAttackUncommon, cardTypeAttackRare;
		public String cardTypeSkillCommon, cardTypeSkillUncommon, cardTypeSkillRare;
		public String cardTypePowerCommon, cardTypePowerUncommon, cardTypePowerRare;
		public String cardTypeAttackCommonP, cardTypeAttackUncommonP, cardTypeAttackRareP;
		public String cardTypeSkillCommonP, cardTypeSkillUncommonP, cardTypeSkillRareP;
		public String cardTypePowerCommonP, cardTypePowerUncommonP, cardTypePowerRareP;
		public String descShadow, descShadowP;
		public String descShadowSmall, descShadowSmallP;

		public Style() {}

		public Style(String cardTypeAttackCommon, String cardTypeAttackUncommon, String cardTypeAttackRare,
					 String cardTypeSkillCommon, String cardTypeSkillUncommon, String cardTypeSkillRare,
					 String cardTypePowerCommon, String cardTypePowerUncommon, String cardTypePowerRare,
					 String cardTypeAttackCommonP, String cardTypeAttackUncommonP, String cardTypeAttackRareP,
					 String cardTypeSkillCommonP, String cardTypeSkillUncommonP, String cardTypeSkillRareP,
					 String cardTypePowerCommonP, String cardTypePowerUncommonP, String cardTypePowerRareP,
					 String descShadow, String descShadowP,
					 String descShadowSmall, String descShadowSmallP) {
			this.cardTypeAttackCommon = cardTypeAttackCommon;
			this.cardTypeAttackUncommon = cardTypeAttackUncommon;
			this.cardTypeAttackRare = cardTypeAttackRare;
			this.cardTypeSkillCommon = cardTypeSkillCommon;
			this.cardTypeSkillUncommon = cardTypeSkillUncommon;
			this.cardTypeSkillRare = cardTypeSkillRare;
			this.cardTypePowerCommon = cardTypePowerCommon;
			this.cardTypePowerUncommon = cardTypePowerUncommon;
			this.cardTypePowerRare = cardTypePowerRare;
			this.cardTypeAttackCommonP = cardTypeAttackCommonP;
			this.cardTypeAttackUncommonP = cardTypeAttackUncommonP;
			this.cardTypeAttackRareP = cardTypeAttackRareP;
			this.cardTypeSkillCommonP = cardTypeSkillCommonP;
			this.cardTypeSkillUncommonP = cardTypeSkillUncommonP;
			this.cardTypeSkillRareP = cardTypeSkillRareP;
			this.cardTypePowerCommonP = cardTypePowerCommonP;
			this.cardTypePowerUncommonP = cardTypePowerUncommonP;
			this.cardTypePowerRareP = cardTypePowerRareP;
			this.descShadow = descShadow;
			this.descShadowP = descShadowP;
			this.descShadowSmall = descShadowSmall;
			this.descShadowSmallP = descShadowSmallP;
		}
	}

	static {
		DEFAULT_STYLE = new Style(
				"SignatureLib/512/attack_common.png",
				"SignatureLib/512/attack_uncommon.png",
				"SignatureLib/512/attack_rare.png",
				"SignatureLib/512/skill_common.png",
				"SignatureLib/512/skill_uncommon.png",
				"SignatureLib/512/skill_rare.png",
				"SignatureLib/512/power_common.png",
				"SignatureLib/512/power_uncommon.png",
				"SignatureLib/512/power_rare.png",
				"SignatureLib/1024/attack_common.png",
				"SignatureLib/1024/attack_uncommon.png",
				"SignatureLib/1024/attack_rare.png",
				"SignatureLib/1024/skill_common.png",
				"SignatureLib/1024/skill_uncommon.png",
				"SignatureLib/1024/skill_rare.png",
				"SignatureLib/1024/power_common.png",
				"SignatureLib/1024/power_uncommon.png",
				"SignatureLib/1024/power_rare.png",
				"SignatureLib/512/desc_shadow.png",
				"SignatureLib/1024/desc_shadow.png",
				"SignatureLib/512/desc_shadow_small.png",
				"SignatureLib/1024/desc_shadow_small.png"
		);
	}
}
