package hsrmod.signature.patches.card;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.CardStrings;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unused")
public class UnlockConditionPatch {
	public static class FakeCardStrings {
		public String SIGNATURE_UNLOCK_CONDITION = null;
		public String SIGNATURE_ILLUSTRATOR = null;
	}

	@SpirePatch(clz = CardStrings.class, method = SpirePatch.CLASS)
	public static class Fields {
		//		@SerializedName("SIGNATURE_UNLOCK_CONDITION")
		public static SpireField<String> unlockCondition = new SpireField<>(() -> null);

		public static SpireField<String> illustrator = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = BaseMod.class, method = "loadJsonStrings",
			paramtypez = {Type.class, String.class})
	public static class LoadJsonStringsPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(ReflectionHacks.class, "setPrivateStaticFinal"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"localizationStrings", "modName"})
		public static void Insert(Type stringType, String jsonString,
								  Map<Object, Object> localizationStrings, String modName) {
			if (stringType.equals(CardStrings.class)) {
				Map<String, FakeCardStrings> map = BaseMod.gson.fromJson(
						jsonString, (new TypeToken<Map<String, FakeCardStrings>>() {}).getType());

				for (Map.Entry<String, FakeCardStrings> entry : map.entrySet()) {
					String key = entry.getKey();
					FakeCardStrings value = entry.getValue();

					String keyWithPrefix = modName == null ? key : modName + ":" + key;

					if (value.SIGNATURE_UNLOCK_CONDITION != null) {
						Fields.unlockCondition.set(localizationStrings.get(keyWithPrefix),
								value.SIGNATURE_UNLOCK_CONDITION);
					}

					if (value.SIGNATURE_ILLUSTRATOR != null) {
						Fields.illustrator.set(localizationStrings.get(keyWithPrefix),
								value.SIGNATURE_ILLUSTRATOR);
					}
				}
			}
		}
	}
}