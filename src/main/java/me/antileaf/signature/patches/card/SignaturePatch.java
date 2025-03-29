package me.antileaf.signature.patches.card;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreviewRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.signature.utils.SignatureHelper;
import me.antileaf.signature.utils.internal.MiscHelper;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

@SuppressWarnings("unused")
public class SignaturePatch {
	@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Boolean> signatureHovered = new SpireField<>(() -> false);
		public static SpireField<Float> signatureHoveredTimer = new SpireField<>(() -> 0.0F);
		public static SpireField<Float> forcedTimer = new SpireField<>(() -> 0.0F);
		public static SpireField<Float> previewTransparency = new SpireField<>(() -> -1.0F);
	}

	@SpirePatch(clz = AbstractCard.class, method = "update", paramtypez = {})
	public static class UpdatePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				float hoveredTimer = Fields.signatureHoveredTimer.get(_inst);
				float forcedTimer = Fields.forcedTimer.get(_inst);

				if (Fields.signatureHovered.get(_inst) ||
						(MiscHelper.isInBattle() && _inst.isHoveredInHand(1.0F))) {
					hoveredTimer = Math.min(hoveredTimer + Gdx.graphics.getDeltaTime(), SignatureHelperInternal.FADE_DURATION);
					Fields.signatureHoveredTimer.set(_inst, hoveredTimer);
				} else {
					hoveredTimer = Math.max(hoveredTimer - Gdx.graphics.getDeltaTime(), 0.0F);
					Fields.signatureHoveredTimer.set(_inst, hoveredTimer);
				}

				if (forcedTimer > 0.0F) {
					forcedTimer = Math.max(forcedTimer - Gdx.graphics.getDeltaTime(), 0.0F);
					Fields.forcedTimer.set(_inst, forcedTimer);
				}
			}
		}
	}

	public static void forceToShowDescription(AbstractCard card) {
		Fields.forcedTimer.set(card, SignatureHelperInternal.FORCED_FADE_DURATION);
	}

	private static float getSignatureTransparency(AbstractCard card) {
		if (Fields.previewTransparency.get(card) >= 0.0F)
			return Fields.previewTransparency.get(card);

		float ret = Math.max(Fields.signatureHoveredTimer.get(card), Fields.forcedTimer.get(card)) /
				SignatureHelperInternal.FADE_DURATION;
		return Math.min(ret, 1.0F);
	}

	public static void setPreviewTransparency(AbstractCard card, float transparency) {
		Fields.previewTransparency.set(card, transparency);
	}

	@SpirePatch(clz = AbstractCard.class, method = "hover", paramtypez = {})
	public static class HoverPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				Fields.signatureHovered.set(_inst, true);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "unhover", paramtypez = {})
	public static class UnhoverPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				Fields.signatureHovered.set(_inst, false);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderDescription",
			paramtypez = {SpriteBatch.class})
	public static class RenderDescriptionPatch {
		private static float textColorAlpha, goldColorAlpha;

		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				float transparency = getSignatureTransparency(_inst);

				if (transparency <= 0.0F)
					return SpireReturn.Return();

				Color textColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "textColor");
				Color goldColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "goldColor");
				textColorAlpha = textColor.a;
				goldColorAlpha = goldColor.a;

				textColor.a *= transparency;
				goldColor.a *= transparency;
			}

			return SpireReturn.Continue();
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst, SpriteBatch sb) {
			if (SignatureHelperInternal.usePatch(_inst) && getSignatureTransparency(_inst) > 0.0F) {
				Color textColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "textColor");
				Color goldColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "goldColor");

				textColor.a = textColorAlpha;
				goldColor.a = goldColorAlpha;
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderDescriptionCN",
			paramtypez = {SpriteBatch.class})
	public static class RenderDescriptionCNPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb) {
			return RenderDescriptionPatch.Prefix(_inst, sb); // 懒了
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst, SpriteBatch sb) {
			RenderDescriptionPatch.Postfix(_inst, sb);
		}
	}

	private static Color getRenderColor(AbstractCard card) {
		return ReflectionHacks.getPrivate(card, AbstractCard.class, "renderColor");
	}

	private static Color getTypeColor(AbstractCard card) {
		return ReflectionHacks.getPrivate(card, AbstractCard.class, "typeColor");
	}

	private static void renderHelper(AbstractCard card, SpriteBatch sb, Color color,
							  TextureAtlas.AtlasRegion img, float drawX, float drawY) {
		ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper",
						SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class)
				.invoke(card, sb, color, img, drawX, drawY);
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderSmallEnergy",
			paramtypez = {SpriteBatch.class, TextureAtlas.AtlasRegion.class, float.class, float.class})
	public static class RenderSmallEnergyPatch {
		private static float alpha;

		@SpirePrefixPatch
		public static void Prefix(AbstractCard _inst, SpriteBatch sb,
								  TextureAtlas.AtlasRegion region, float x, float y) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				Color renderColor = getRenderColor(_inst);

				alpha = renderColor.a;
				renderColor.a *= getSignatureTransparency(_inst);
			}
		}

		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst, SpriteBatch sb,
								   TextureAtlas.AtlasRegion region, float x, float y) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				getRenderColor(_inst).a = alpha;
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderImage",
			paramtypez = {SpriteBatch.class, boolean.class, boolean.class})
	public static class RenderImagePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				Color renderColor = getRenderColor(_inst);
				float alpha = renderColor.a;
				renderColor.a *= getSignatureTransparency(_inst);

				SignatureHelper.Info info = SignatureHelperInternal.getInfo(_inst.cardID);

				String shadow = _inst.description.size() >= 4 ||
						(info.style.descShadowSmall == null || info.style.descShadowSmall.isEmpty()) ?
						info.style.descShadow : info.style.descShadowSmall;

				if (shadow != null)
					renderHelper(_inst, sb, renderColor, SignatureHelperInternal.load(shadow),
							_inst.current_x, _inst.current_y);

				renderColor.a = alpha;
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderCardBg",
			paramtypez = {SpriteBatch.class, float.class, float.class})
	public static class RenderCardBgPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb, float x, float y) {
			if (SignatureHelperInternal.usePatch(_inst))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderPortrait",
			paramtypez = {SpriteBatch.class})
	public static class RenderPortraitPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				sb.setColor(getRenderColor(_inst));
				sb.draw(SignatureHelperInternal.load(
						SignatureHelperInternal.getInfo(_inst.cardID).portrait.apply(_inst)),
						_inst.current_x - 256.0F,
						_inst.current_y - 256.0F,
						256.0F, 256.0F, 512.0F, 512.0F,
						_inst.drawScale * Settings.scale,
						_inst.drawScale * Settings.scale,
						_inst.angle);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderJokePortrait",
			paramtypez = {SpriteBatch.class})
	public static class RenderJokePortraitPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb) {
			return RenderPortraitPatch.Prefix(_inst, sb);
		}
	}

	private static TextureAtlas.AtlasRegion getFrame(AbstractCard card) {
		SignatureHelper.Style style = SignatureHelperInternal.getStyle(card);

		String frame;

		if (card.type == AbstractCard.CardType.ATTACK) {
			if (card.rarity == AbstractCard.CardRarity.RARE)
				frame = style.cardTypeAttackRare;
			else if (card.rarity == AbstractCard.CardRarity.UNCOMMON)
				frame = style.cardTypeAttackUncommon;
			else
				frame = style.cardTypeAttackCommon;
		}
		else if (card.type == AbstractCard.CardType.POWER) {
			if (card.rarity == AbstractCard.CardRarity.RARE)
				frame = style.cardTypePowerRare;
			else if (card.rarity == AbstractCard.CardRarity.UNCOMMON)
				frame = style.cardTypePowerUncommon;
			else
				frame = style.cardTypePowerCommon;
		}
		else {
			if (card.rarity == AbstractCard.CardRarity.RARE)
				frame = style.cardTypeSkillRare;
			else if (card.rarity == AbstractCard.CardRarity.UNCOMMON)
				frame = style.cardTypeSkillUncommon;
			else
				frame = style.cardTypeSkillCommon;
		}

		return frame != null && !frame.isEmpty() ? SignatureHelperInternal.load(frame) : null;
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderPortraitFrame",
			paramtypez = {SpriteBatch.class, float.class, float.class})
	public static class RenderPortraitFramePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb, float x, float y) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				TextureAtlas.AtlasRegion frame = getFrame(_inst);

				if (frame != null)
					renderHelper(_inst, sb, getRenderColor(_inst), frame, x, y);

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderBannerImage",
			paramtypez = {SpriteBatch.class, float.class, float.class})
	public static class RenderBannerImagePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb, float x, float y) {
			if (SignatureHelperInternal.usePatch(_inst))
				return SpireReturn.Return();

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderType",
			paramtypez = {SpriteBatch.class})
	public static class RenderTypePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(AbstractCard _inst, SpriteBatch sb) {
			if (SignatureHelperInternal.usePatch(_inst)) {
				if (getFrame(_inst) != null) {
					String text;
					if (_inst.type == AbstractCard.CardType.ATTACK)
						text = AbstractCard.TEXT[0];
					else if (_inst.type == AbstractCard.CardType.SKILL)
						text = AbstractCard.TEXT[1];
					else if (_inst.type == AbstractCard.CardType.POWER)
						text = AbstractCard.TEXT[2];
					else if (_inst.type == AbstractCard.CardType.CURSE)
						text = AbstractCard.TEXT[3];
					else if (_inst.type == AbstractCard.CardType.STATUS)
						text = AbstractCard.TEXT[7];
					else
						text = AbstractCard.TEXT[5];

					BitmapFont font = FontHelper.cardTypeFont;
					font.getData().setScale(_inst.drawScale);
					getTypeColor(_inst).a = getRenderColor(_inst).a;
					FontHelper.renderRotatedText(sb, font, text, _inst.current_x,
							_inst.current_y - 195.0F * _inst.drawScale * Settings.scale,
							0.0F,
							-1.0F * _inst.drawScale * Settings.scale,
							_inst.angle, false, getTypeColor(_inst));
				}

				return SpireReturn.Return();
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderCardPreview",
			paramtypez = {SpriteBatch.class})
	public static class RenderCardPreviewPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractCard.class, "render"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(AbstractCard _inst, SpriteBatch sb) {
			if (SignatureHelperInternal.shouldUseSignature(_inst.cardsToPreview))
				setPreviewTransparency(_inst.cardsToPreview, 1.0F);
		}
	}

	@SpirePatch(clz = MultiCardPreviewRenderer.RenderMultiCardPreviewPatch.class,
			method = "Postfix", paramtypez = {AbstractCard.class, SpriteBatch.class})
	public static class BaseModMultiCardPreviewPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractCard _inst, SpriteBatch sb) {
			if (MultiCardPreview.multiCardPreview.get(_inst) != null)
				for (AbstractCard preview : MultiCardPreview.multiCardPreview.get(_inst))
					if (SignatureHelperInternal.shouldUseSignature(preview))
						setPreviewTransparency(preview, 1.0F);
		}
	}
}
