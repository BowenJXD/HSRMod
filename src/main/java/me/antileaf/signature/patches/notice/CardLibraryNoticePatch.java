package me.antileaf.signature.patches.notice;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class CardLibraryNoticePatch {
	private static UIStrings uiStrings;

	private static UIStrings getUIStrings() {
		if (uiStrings == null)
			uiStrings = CardCrawlGame.languagePack.getUIString("SignatureLib:New");
		return uiStrings;
	}

	@SpirePatch(clz = MenuButton.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class MainMenuPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Hitbox.class, "render"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"sliderX"})
		public static void Insert(MenuButton _inst, SpriteBatch sb, float sliderX) {
			if (_inst.result == MenuButton.ClickResult.INFO &&
					SignatureHelperInternal.hasAnyNotice()) {
				String label = ReflectionHacks.getPrivate(_inst, MenuButton.class, "label");
				float width = FontHelper.getSmartWidth(FontHelper.buttonLabelFont,
						label, 9999.0F, 0.0F);

				if (Settings.language == Settings.GameLanguage.ZHS ||
						Settings.language == Settings.GameLanguage.ZHT ||
						Settings.language == Settings.GameLanguage.KOR) {
					width -= 10.0F * Settings.scale;
				}

				float scale = 1.0F;
				if (Settings.isTouchScreen || Settings.isMobile) {
					scale = 1.25F;
					width *= scale;
				}

				scale *= 0.6F;

				FontHelper.renderSmartText(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[0],
						(float) ReflectionHacks.getPrivate(_inst, MenuButton.class, "x") +
								MenuButton.FONT_X + sliderX + width,
						_inst.hb.cY + MenuButton.FONT_OFFSET_Y - 16.0F * Settings.scale * scale,
						9999.0F, 1.0F, Settings.GOLD_COLOR, scale);
			}
		}
	}

	@SpirePatch(clz = MainMenuPanelButton.class, method = "render", paramtypez = {SpriteBatch.class})
	public static class PanelButtonPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Hitbox.class, "render"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(MainMenuPanelButton _inst, SpriteBatch sb) {
			if (ReflectionHacks.getPrivate(_inst, MainMenuPanelButton.class, "result") ==
					MainMenuPanelButton.PanelClickResult.INFO_CARD &&
					SignatureHelperInternal.hasAnyNotice()) {
				float uiScale = ReflectionHacks.getPrivate(_inst, MainMenuPanelButton.class, "uiScale");

				FontHelper.renderFontCentered(sb, FontHelper.damageNumberFont, getUIStrings().TEXT[0],
						_inst.hb.cX + 140.0F * Settings.scale * uiScale,
						_inst.hb.cY + (float) ReflectionHacks.getPrivate(
								_inst, MainMenuPanelButton.class, "yMod") + 360.0F * Settings.scale * uiScale,
						Settings.GOLD_COLOR, 0.45F);
			}
		}
	}

	@SpirePatch(clz = ColorTabBar.class, method = "render",
			paramtypez = {SpriteBatch.class, float.class})
	public static class RenderTabPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(Hitbox.class, "render"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(ColorTabBar _inst, SpriteBatch sb, float y) {
			if (SignatureHelperInternal.getLibraryTypeNotice(AbstractCard.CardColor.CURSE) > 0) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.curseHb.cX + 100.0F * Settings.scale, y + 80.0F * Settings.scale,
						_inst.curTab == ColorTabBar.CurrentTab.CURSE ? Settings.CREAM_COLOR : Settings.GOLD_COLOR,
						0.6F);
			}

			if (SignatureHelperInternal.getLibraryTypeNotice(AbstractCard.CardColor.COLORLESS) > 0) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.colorlessHb.cX + 100.0F * Settings.scale, y + 80.0F * Settings.scale,
						_inst.curTab == ColorTabBar.CurrentTab.COLORLESS ? Settings.CREAM_COLOR : Settings.GOLD_COLOR,
						0.6F);
			}

			if (SignatureHelperInternal.getLibraryTypeNotice(AbstractCard.CardColor.BLUE) > 0) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.blueHb.cX + 100.0F * Settings.scale, y + 80.0F * Settings.scale,
						_inst.curTab == ColorTabBar.CurrentTab.BLUE ? Settings.CREAM_COLOR : Settings.GOLD_COLOR,
						0.6F);
			}

			if (SignatureHelperInternal.getLibraryTypeNotice(AbstractCard.CardColor.PURPLE) > 0) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.purpleHb.cX + 100.0F * Settings.scale, y + 80.0F * Settings.scale,
						_inst.curTab == ColorTabBar.CurrentTab.PURPLE ? Settings.CREAM_COLOR : Settings.GOLD_COLOR,
						0.6F);
			}

			if (SignatureHelperInternal.getLibraryTypeNotice(AbstractCard.CardColor.GREEN) > 0) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.greenHb.cX + 100.0F * Settings.scale, y + 80.0F * Settings.scale,
						_inst.curTab == ColorTabBar.CurrentTab.GREEN ? Settings.CREAM_COLOR : Settings.GOLD_COLOR,
						0.6F);
			}

			if (SignatureHelperInternal.getLibraryTypeNotice(AbstractCard.CardColor.RED) > 0) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.redHb.cX + 100.0F * Settings.scale, y + 80.0F * Settings.scale,
						_inst.curTab == ColorTabBar.CurrentTab.RED ? Settings.CREAM_COLOR : Settings.GOLD_COLOR,
						0.6F);
			}
		}
	}

	@SpirePatch(clz = ColorTabBarFix.Render.class, method = "Insert",
			paramtypez = {ColorTabBar.class, SpriteBatch.class, float.class, ColorTabBar.CurrentTab.class})
	public static class ModdedColorsPatch {
		@SpireInsertPatch(rloc = 27)
		public static void Insert(ColorTabBar _inst, SpriteBatch sb, float y, ColorTabBar.CurrentTab currentTab) {
			ArrayList<ColorTabBarFix.ModColorTab> tabs = ReflectionHacks.getPrivateStatic(
					ColorTabBarFix.Fields.class, "modTabs");
			int index = ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabIndex");

			for (int i = 0; i < tabs.size(); i++) {
				if (SignatureHelperInternal.getLibraryTypeNotice(tabs.get(i).color) > 0) {
					FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
							257.0F * Settings.scale, y - 64.0F * (i + 1) * Settings.scale + 80.0F * Settings.scale,
							currentTab == ColorTabBarFix.Enums.MOD && index == i ?
									Settings.CREAM_COLOR : Settings.GOLD_COLOR,
							0.6F);
				}
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderInLibrary", paramtypez = {SpriteBatch.class})
	public static class CardRenderPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst, SpriteBatch sb) {
			if ((boolean) ReflectionHacks.privateMethod(AbstractCard.class, "isOnScreen").invoke(_inst) &&
					SignatureHelperInternal.signatureNotice(_inst)) {
				FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, getUIStrings().TEXT[1],
						_inst.current_x + _inst.hb.width * 0.5F,
						_inst.current_y + _inst.hb.height * 0.5F,
						Settings.GOLD_COLOR, 0.6F);
			}
		}
	}
}
