package me.antileaf.signature.patches.scv;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

@SuppressWarnings("unused")
public class SCVPanelPatch {
	private static final float MAN = -1000.0F * Settings.scale;

	@SpirePatch(clz = SingleCardViewPopup.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Hitbox> enableHb = new SpireField<>(() -> null);
		public static SpireField<Hitbox> descHb = new SpireField<>(() -> null);
		
//		public static SpireField<Boolean> enabled = new SpireField<>(() -> false);
		public static SpireField<Boolean> hideDesc = new SpireField<>(() -> false);

		public static SpireField<String> enableText = new SpireField<>(() -> null);
		public static SpireField<String> lockedText = new SpireField<>(() -> null);
		public static SpireField<String> hideDescText = new SpireField<>(() -> null);
		public static SpireField<String> conditionText = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = SpirePatch.CONSTRUCTOR)
	public static class ConstructorPatch {
		public static void Postfix(SingleCardViewPopup _inst) {
			Fields.enableHb.set(_inst, new Hitbox(320.0F * Settings.scale, 80.0F * Settings.scale));
			Fields.descHb.set(_inst, new Hitbox(320.0F * Settings.scale, 80.0F * Settings.scale));

			UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SignatureLib:SCVPanel");

			Fields.enableText.set(_inst, uiStrings.TEXT[0]);
			Fields.lockedText.set(_inst, uiStrings.TEXT[1]);
			Fields.hideDescText.set(_inst, uiStrings.TEXT[2]);
			Fields.conditionText.set(_inst, uiStrings.TEXT[3]);
		}
	}

	private static void moveHitboxes(SingleCardViewPopup _inst, AbstractCard card) {
		if (SignatureHelperInternal.hasSignature(card) &&
				!SignatureHelperInternal.hideSCVPanel(card))
			Fields.enableHb.get(_inst).move(Settings.WIDTH / 2.0F - 460.0F * Settings.scale,
					340.0F * Settings.scale);
		else
			Fields.enableHb.get(_inst).move(MAN, MAN);

		if (SignatureHelperInternal.shouldUseSignature(card) &&
				!SignatureHelperInternal.hideSCVPanel(card))
			Fields.descHb.get(_inst).move(Settings.WIDTH / 2.0F - 460.0F * Settings.scale,
					260.0F * Settings.scale);
		else
			Fields.descHb.get(_inst).move(MAN, MAN);
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "open",
			paramtypez = {AbstractCard.class, CardGroup.class})
	public static class OpenPatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, AbstractCard card, CardGroup group) {
			moveHitboxes(_inst, card); // group 用不到
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "open", paramtypez = {AbstractCard.class})
	public static class OpenPatch2 {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, AbstractCard card) {
			moveHitboxes(_inst, card);
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "update")
	public static class UpdatePatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, AbstractCard ___card) {
			moveHitboxes(_inst, ___card);

			Fields.enableHb.get(_inst).update();
			Fields.descHb.get(_inst).update();

			if (Fields.enableHb.get(_inst).justHovered)
				CardCrawlGame.sound.play("UI_HOVER");

			if (Fields.enableHb.get(_inst).hovered && InputHelper.justClickedLeft) {
				Fields.enableHb.get(_inst).clickStarted = true;
				CardCrawlGame.sound.play("UI_CLICK_1");
			}

			if (Fields.enableHb.get(_inst).clicked) {
				Fields.enableHb.get(_inst).clicked = false;

				if (SignatureHelperInternal.isUnlocked(___card.cardID)) {
					SignatureHelperInternal.enable(___card.cardID,
							!SignatureHelperInternal.isEnabled(___card.cardID));

					SignatureHelperInternal.setSignatureNotice(___card, false);
				}
			}

			if (Fields.descHb.get(_inst).justHovered)
				CardCrawlGame.sound.play("UI_HOVER");

			if (Fields.descHb.get(_inst).hovered && InputHelper.justClickedLeft) {
				Fields.descHb.get(_inst).clickStarted = true;
				CardCrawlGame.sound.play("UI_CLICK_1");
			}

			if (Fields.descHb.get(_inst).clicked) {
				Fields.descHb.get(_inst).clicked = false;
				Fields.hideDesc.set(_inst, !Fields.hideDesc.get(_inst));
			}
		}
	}

	// 防止点击新增的按钮时触发退出
	// Thanks to OvO!
	@SpirePatch(clz = SingleCardViewPopup.class, method = "updateInput")
	public static class updateInputPatch {
		private static boolean hitBoxesHovered = false;

		@SpireInsertPatch(rloc = 12)
		public static void updateInputPatch1(SingleCardViewPopup _inst, Hitbox ___cardHb) {
			hitBoxesHovered = Fields.enableHb.get(_inst).hovered || Fields.descHb.get(_inst).hovered;
			___cardHb.hovered = hitBoxesHovered;
		}

		@SpireInsertPatch(rloc = 24)
		public static void updateInputPatch2(SingleCardViewPopup _inst, Hitbox ___cardHb) {
			if (hitBoxesHovered)
				___cardHb.hovered = false;
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(SingleCardViewPopup.class, "close"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(SingleCardViewPopup _inst) {
			Fields.hideDesc.set(_inst, false);
		}
	}

	@SpirePatch(clz = SingleCardViewPopup.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class RenderPatch {
		@SpirePostfixPatch
		public static void Postfix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
			if (!SignatureHelperInternal.hasSignature(___card) ||
					SignatureHelperInternal.hideSCVPanel(___card))
				return;

			sb.setColor(Color.WHITE);

			if (SignatureHelperInternal.isUnlocked(___card.cardID))
				sb.draw(ImageMaster.CHECKBOX,
						Fields.enableHb.get(_inst).cX - 120.0F * Settings.scale - 32.0F,
						Fields.enableHb.get(_inst).cY - 32.0F,
						32.0F, 32.0F, 64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F, 0, 0, 64, 64,
						false, false);
			else
				sb.draw(ImageMaster.COLOR_TAB_LOCK,
						Fields.enableHb.get(_inst).cX - 120.0F * Settings.scale - 32.0F,
						Fields.enableHb.get(_inst).cY - 32.0F,
						20.0F, 20.0F, 64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F, 0, 0, 40, 40,
						false, false);

			String text = SignatureHelperInternal.isUnlocked(___card.cardID) ?
					Fields.enableText.get(_inst) : Fields.lockedText.get(_inst);

			if (Fields.enableHb.get(_inst).hovered)
//				FontHelper.renderFontRightTopAligned(sb, FontHelper.cardTitleFont, Fields.enableText.get(_inst),
//						Fields.enableHb.get(_inst).cX + 69.0F * Settings.scale,
				FontHelper.renderFont(sb, FontHelper.cardTitleFont, text,
						Fields.enableHb.get(_inst).cX - 65.0F * Settings.scale,
						Fields.enableHb.get(_inst).cY + 10.0F * Settings.scale,
						Settings.BLUE_TEXT_COLOR);
			else
				FontHelper.renderFont(sb, FontHelper.cardTitleFont, text,
						Fields.enableHb.get(_inst).cX - 65.0F * Settings.scale,
						Fields.enableHb.get(_inst).cY + 10.0F * Settings.scale,
						Settings.GOLD_COLOR);

			if (SignatureHelperInternal.signatureNotice(___card)) {
				FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont,
						CardCrawlGame.languagePack.getUIString("SignatureLib:New").TEXT[1],
						Fields.enableHb.get(_inst).cX + 65.0F * Settings.scale,
						Fields.enableHb.get(_inst).cY + 30.0F * Settings.scale,
						Settings.CREAM_COLOR, 0.6F);
			}

			if (SignatureHelperInternal.shouldUseSignature(___card)) {
				sb.setColor(Color.WHITE);
				sb.draw(ImageMaster.TICK,
						Fields.enableHb.get(_inst).cX - 120.0F * Settings.scale - 32.0F,
						Fields.enableHb.get(_inst).cY - 32.0F,
						32.0F, 32.0F, 64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F, 0, 0, 64, 64,
						false, false);
			}

			Fields.enableHb.get(_inst).render(sb);

			if (SignatureHelperInternal.shouldUseSignature(___card)) {
				sb.setColor(Color.WHITE);
				sb.draw(ImageMaster.CHECKBOX,
						Fields.descHb.get(_inst).cX - 120.0F * Settings.scale - 32.0F,
						Fields.descHb.get(_inst).cY - 32.0F,
						32.0F, 32.0F, 64.0F, 64.0F,
						Settings.scale, Settings.scale,
						0.0F, 0, 0, 64, 64,
						false, false);

				if (Fields.descHb.get(_inst).hovered)
					FontHelper.renderFont(sb, FontHelper.cardTitleFont, Fields.hideDescText.get(_inst),
							Fields.descHb.get(_inst).cX - 65.0F * Settings.scale,
							Fields.descHb.get(_inst).cY + 10.0F * Settings.scale,
							Settings.BLUE_TEXT_COLOR);
				else
					FontHelper.renderFont(sb, FontHelper.cardTitleFont, Fields.hideDescText.get(_inst),
							Fields.descHb.get(_inst).cX - 65.0F * Settings.scale,
							Fields.descHb.get(_inst).cY + 10.0F * Settings.scale,
							Settings.GOLD_COLOR);

				if (Fields.hideDesc.get(_inst)) {
					sb.setColor(Color.WHITE);
					sb.draw(ImageMaster.TICK,
							Fields.descHb.get(_inst).cX - 120.0F * Settings.scale - 32.0F,
							Fields.descHb.get(_inst).cY - 32.0F,
							32.0F, 32.0F, 64.0F, 64.0F,
							Settings.scale, Settings.scale,
							0.0F, 0, 0, 64, 64,
							false, false);
				}

				Fields.descHb.get(_inst).render(sb);
			}
			else if (!SignatureHelperInternal.isUnlocked(___card.cardID)) {
				String condition = SignatureHelperInternal.getUnlockCondition(___card.cardID);
				if (condition != null) {
					FontHelper.renderFont(sb, FontHelper.cardTitleFont, Fields.conditionText.get(_inst),
							Fields.enableHb.get(_inst).cX - 140.0F * Settings.scale,
							Fields.enableHb.get(_inst).cY - 70.0F * Settings.scale,
							Settings.GOLD_COLOR);

					FontHelper.renderSmartText(sb, FontHelper.cardTitleFont, condition,
							Fields.enableHb.get(_inst).cX - 140.0F * Settings.scale,
							Fields.enableHb.get(_inst).cY - 110.0F * Settings.scale,
							280.0F * Settings.scale, 32.0F * Settings.scale,
							Settings.CREAM_COLOR);
				}
			}
		}
	}
}
