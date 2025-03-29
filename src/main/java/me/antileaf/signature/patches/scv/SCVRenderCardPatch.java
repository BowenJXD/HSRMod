package me.antileaf.signature.patches.scv;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreviewRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.signature.utils.SignatureHelper;
import me.antileaf.signature.card.AbstractSignatureCard;
import me.antileaf.signature.patches.card.SignaturePatch;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class SCVRenderCardPatch {
	private static void renderHelper(SingleCardViewPopup scv, SpriteBatch sb,
									 float x, float y, TextureAtlas.AtlasRegion img) {
		ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderHelper",
				SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class)
				.invoke(scv, sb, x, y, img);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<TextureAtlas.AtlasRegion> signature = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "loadPortraitImg", paramtypez = {})
	public static class LoadPortraitImgPatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, AbstractCard ___card) {
			if (SignatureHelperInternal.hasSignature(___card)) {
				String sig = ___card instanceof AbstractSignatureCard ?
						((AbstractSignatureCard) ___card).getSignaturePortraitImgPath() :
						SignatureHelperInternal.getInfo(___card.cardID).portrait.apply(___card);

				Fields.signature.set(_inst, SignatureHelperInternal.load(sig));
			}
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "close", paramtypez = {})
	public static class ClosePatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst) {
			if (Fields.signature.get(_inst) != null)
				Fields.signature.set(_inst, null);
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderPortrait", paramtypez = {SpriteBatch.class})
	public static class RenderPortraitPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (___card.isLocked)
				return SpireReturn.Continue();

			if (SignatureHelperInternal.shouldUseSignature(___card)) {
				renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
						Fields.signature.get(_inst));

				if (!SCVPanelPatch.Fields.hideDesc.get(_inst)) {
					SignatureHelper.Style style = SignatureHelperInternal.getStyle(___card);

					String shadow = ___card.description.size() >= 4 || style.descShadowSmallP == null ?
							style.descShadowP : style.descShadowSmallP;

					if (shadow != null && !shadow.isEmpty())
						renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
								SignatureHelperInternal.load(shadow));
				}

				return SpireReturn.Return(null);
			}

			return SpireReturn.Continue();
		}
	}

	private static TextureAtlas.AtlasRegion getFrameP(AbstractCard card) {
		SignatureHelper.Style style = SignatureHelperInternal.getStyle(card);

		String frame;

		if (card.type == AbstractCard.CardType.ATTACK) {
			if (card.rarity == AbstractCard.CardRarity.RARE)
				frame = style.cardTypeAttackRareP;
			else if (card.rarity == AbstractCard.CardRarity.UNCOMMON)
				frame = style.cardTypeAttackUncommonP;
			else
				frame = style.cardTypeAttackCommonP;
		}
		else if (card.type == AbstractCard.CardType.POWER) {
			if (card.rarity == AbstractCard.CardRarity.RARE)
				frame = style.cardTypePowerRareP;
			else if (card.rarity == AbstractCard.CardRarity.UNCOMMON)
				frame = style.cardTypePowerUncommonP;
			else
				frame = style.cardTypePowerCommonP;
		}
		else {
			if (card.rarity == AbstractCard.CardRarity.RARE)
				frame = style.cardTypeSkillRareP;
			else if (card.rarity == AbstractCard.CardRarity.UNCOMMON)
				frame = style.cardTypeSkillUncommonP;
			else
				frame = style.cardTypeSkillCommonP;
		}

		return frame != null && !frame.isEmpty() ? SignatureHelperInternal.load(frame) : null;
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame", paramtypez = {SpriteBatch.class})
	public static class RenderFramePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (SignatureHelperInternal.shouldUseSignature(___card)) {
				TextureAtlas.AtlasRegion frame = getFrameP(___card);

				if (frame != null)
					renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, frame);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner", paramtypez = {SpriteBatch.class})
	public static class RenderCardBannerPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (SignatureHelperInternal.shouldUseSignature(___card))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText", paramtypez = {SpriteBatch.class})
	public static class RenderCardTypeTextPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"label"})
		public static SpireReturn<Void> Insert(SingleCardViewPopup _inst, SpriteBatch sb,
											   AbstractCard ___card, String label) {
			if (SignatureHelperInternal.shouldUseSignature(___card)) {
				if (getFrameP(___card) != null) {
					Color cardTypeColor = ReflectionHacks.getPrivateStatic(SingleCardViewPopup.class,
							"CARD_TYPE_COLOR");

					FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, label,
							(float) Settings.WIDTH / 2.0F,
							(float) Settings.HEIGHT / 2.0F - 392.0F * Settings.scale,
							cardTypeColor);
				}

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription", paramtypez = {SpriteBatch.class})
	public static class RenderDescriptionPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (SignatureHelperInternal.shouldUseSignature(___card) &&
					SCVPanelPatch.Fields.hideDesc.get(_inst))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN", paramtypez = {SpriteBatch.class})
	public static class RenderDescriptionCNPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (SignatureHelperInternal.shouldUseSignature(___card) &&
					SCVPanelPatch.Fields.hideDesc.get(_inst))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips", paramtypez = {SpriteBatch.class})
	public static class RenderTipsPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractCard.class,
								"renderCardPreviewInSingleView"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (SignatureHelperInternal.shouldUseSignature(___card.cardsToPreview))
				SignaturePatch.setPreviewTransparency(___card.cardsToPreview,
						SCVPanelPatch.Fields.hideDesc.get(_inst) ? 0.0F : 1.0F);
		}
	}

	@SpirePatch(clz = MultiCardPreviewRenderer.RenderMultiCardPreviewInSingleViewPatch.class,
			method = "Postfix", paramtypez = {SingleCardViewPopup.class, SpriteBatch.class})
	public static class BaseModMultiCardPreviewSCVPatch {
		@SpirePrefixPatch
		public static void Prefix(SingleCardViewPopup _inst, SpriteBatch sb) {
			AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class, "card");

			if (MultiCardPreview.multiCardPreview.get(card) != null)
				for (AbstractCard preview : MultiCardPreview.multiCardPreview.get(card))
					if (SignatureHelperInternal.shouldUseSignature(preview))
						SignaturePatch.setPreviewTransparency(preview,
								SCVPanelPatch.Fields.hideDesc.get(_inst) ? 0.0F : 1.0F);
		}
	}

	@SpirePatch(clz = MultiCardPreviewRenderer.RenderMultiCardPreviewInSingleViewPatch.class,
			method = "Postfix", paramtypez = {SingleCardViewPopup.class, SpriteBatch.class})
	public static class AvoidOverlappingWithPanelPatch {
		@SpireInsertPatch(rloc = 25, localvars = {"horizontalOnly", "verticalNext", "position", "offset", "toPreview"})
		public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card,
								  boolean horizontalOnly, @ByRef boolean[] verticalNext,
								  Vector2 position, Vector2 offset, AbstractCard toPreview) {
			if (SignatureHelperInternal.hasSignature(___card) &&
					SignatureHelperInternal.isUnlocked(___card.cardID)) {
				if (SignatureHelperInternal.dontAvoidSCVPanel(___card))
					return;

				ArrayList<AbstractCard> previews = MultiCardPreview.multiCardPreview.get(___card);

				if (previews.size() > 1 && toPreview == previews.get(1)) {
					ReflectionHacks.privateMethod(MultiCardPreviewRenderer.class, "reposition",
							Vector2.class, Vector2.class, boolean.class)
							.invoke(null, position, offset, horizontalOnly || !verticalNext[0]);

					verticalNext[0] = !verticalNext[0];
				}
			}
		}
	}
}
