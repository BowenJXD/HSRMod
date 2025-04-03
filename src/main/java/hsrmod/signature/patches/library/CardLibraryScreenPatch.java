package hsrmod.signature.patches.library;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.compendium.CardLibSortHeader;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.SortHeaderButton;
import com.megacrit.cardcrawl.screens.mainMenu.SortHeaderButtonListener;
import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import hsrmod.signature.utils.internal.SignatureHelperInternal;

@SuppressWarnings("unused")
public class CardLibraryScreenPatch {
    @SpirePatch(clz = CardLibSortHeader.class, method = SpirePatch.STATICINITIALIZER)
    public static class CardLibSortHeaderPatch {
        @SpirePostfixPatch
        public static void Postfix() {
            ReflectionHacks.setPrivateStaticFinal(CardLibSortHeader.class,
                    "START_X", 400.0F * Settings.xScale);

            ReflectionHacks.setPrivateStaticFinal(CardLibSortHeader.class,
                    "SPACE_X", 160.0F * Settings.xScale);
        }
    }

    @SpirePatch(clz = SortHeaderButton.class, method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, float.class, float.class})
    public static class SortHeaderButtonPatch {
        @SpirePostfixPatch
        public static void Postfix(SortHeaderButton _inst, String text, float cx, float cy) {
            ((Hitbox) ReflectionHacks.getPrivate(_inst, SortHeaderButton.class, "hb"))
                    .resize(150.0F * Settings.xScale, 48.0F * Settings.scale);
        }
    }

    @SpirePatch(clz = ColorTabBar.class, method = SpirePatch.CONSTRUCTOR)
    public static class UpgradeHbConstructorPatch {
        @SpirePostfixPatch
        public static void Postfix(ColorTabBar _inst) {
            _inst.viewUpgradeHb.resize(280.0F * Settings.scale, 48.0F * Settings.scale);
        }
    }

    @SpirePatch(clz = ColorTabBar.class, method = "update", paramtypez = {float.class})
    public static class UpgradeHbUpdatePatch {
        @SpireInsertPatch(rloc = 15)
        public static void Insert(ColorTabBar _inst, float y) {
            _inst.viewUpgradeHb.move(1450.0F * Settings.xScale, y);
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = SpirePatch.CLASS)
    public static class ScreenFields {
        public static SpireField<Boolean> signatureOnly = new SpireField<>(() -> false);
        public static SpireField<CardGroup> allCards = new SpireField<>(() -> null);
        public static SpireField<CardGroup> signatureCards = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = CardLibSortHeader.class, method = SpirePatch.CLASS)
    public static class HeaderFields {
        public static SpireField<CardGroup> signatureGroup = new SpireField<>(() -> null);
    }

    private static void filterSignatureCards(CardGroup signatureCards, CardGroup allCards) {
        signatureCards.group.clear();

        allCards.group.stream()
                .filter(c -> SignatureHelperInternal.hasSignature(c) &&
                        !SignatureHelperInternal.hideSCVPanel(c))
                .forEach(signatureCards.group::add);
    }

//	private static void sortByDefault(CardGroup group) {
//		group.sortAlphabetically(true);
//		group.sortByRarity(true);
//		group.sortByStatus(true);
//	}

    @SpirePatch(clz = CardLibraryScreen.class, method = "initialize")
    public static class CardLibraryScreenConstructorPatch {
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                return LineFinder.findInOrder(ctBehavior,
                        new Matcher.MethodCallMatcher(CardLibSortHeader.class, "setGroup"));
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CardLibraryScreen _inst, CardGroup ___visibleCards,
                                  CardLibSortHeader ___sortHeader) {
            ScreenFields.signatureCards.set(_inst, new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));

            ScreenFields.allCards.set(_inst, ___visibleCards);
            filterSignatureCards(ScreenFields.signatureCards.get(_inst), ___visibleCards);

            HeaderFields.signatureGroup.set(___sortHeader, ScreenFields.signatureCards.get(_inst));
            ___sortHeader.setGroup(ScreenFields.signatureCards.get(_inst));
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "didChangeTab",
            paramtypez = {ColorTabBar.class, ColorTabBar.CurrentTab.class})
    public static class DidChangeTabPatch {
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                return LineFinder.findInOrder(ctBehavior,
                        new Matcher.MethodCallMatcher(CardLibSortHeader.class, "setGroup"));
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CardLibraryScreen _inst, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection,
                                  CardGroup ___visibleCards, CardLibSortHeader ___sortHeader) {
            ScreenFields.allCards.set(_inst, ___visibleCards);
            filterSignatureCards(ScreenFields.signatureCards.get(_inst), ___visibleCards);

            HeaderFields.signatureGroup.set(___sortHeader, ScreenFields.signatureCards.get(_inst));

            if (ScreenFields.signatureCards.get(_inst).isEmpty())
                ScreenFields.signatureOnly.set(_inst, false);
            else
                ___sortHeader.setGroup(ScreenFields.signatureCards.get(_inst));
        }

        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
                return LineFinder.findInOrder(ctBehavior,
                        new Matcher.MethodCallMatcher(CardLibraryScreen.class, "calculateScrollBounds"));
            }
        }

        @SpireInsertPatch(locator = Locator2.class)
        public static void Insert2(CardLibraryScreen _inst, ColorTabBar tabBar, ColorTabBar.CurrentTab newSelection,
                                   CardGroup ___visibleCards, CardLibSortHeader ___sortHeader) {
            if (ScreenFields.signatureOnly.get(_inst)) {
                ReflectionHacks.setPrivate(_inst, CardLibraryScreen.class, "visibleCards",
                        ScreenFields.signatureCards.get(_inst));

//				HeaderFields.signatureGroup.set(___sortHeader, ___sortHeader.group);
//				___sortHeader.group = ScreenFields.signatureCards.get(_inst);
            }
        }
    }

    @SpirePatch(clz = CardLibraryScreen.class, method = "updateCards", paramtypez = {})
    public static class UpdateCardsPatch {
        @SpirePrefixPatch
        public static void Prefix(CardLibraryScreen _inst, CardGroup ___visibleCards, CardLibSortHeader ___sortHeader) {
            if (ScreenFields.signatureOnly.get(_inst)) {
                CardGroup allCards = ScreenFields.allCards.get(_inst);

                float drawStartX = ReflectionHacks.getPrivateStatic(CardLibraryScreen.class, "drawStartX");
                float drawStartY = ReflectionHacks.getPrivateStatic(CardLibraryScreen.class, "drawStartY");
                float currentDiffY = ReflectionHacks.getPrivate(_inst,
                        CardLibraryScreen.class, "currentDiffY");
                float padX = ReflectionHacks.getPrivateStatic(CardLibraryScreen.class, "padX");
                float padY = ReflectionHacks.getPrivateStatic(CardLibraryScreen.class, "padY");

                int CARDS_PER_LINE = ReflectionHacks.getPrivateStatic(CardLibraryScreen.class,
                        "CARDS_PER_LINE");
                int p = 0, i = 0, lineNum = 0;

                for (AbstractCard card : ___visibleCards.group) {
                    while (p < allCards.group.size() && allCards.group.get(p) != card) {
                        int mod = i % CARDS_PER_LINE;
                        if (mod == 0 && i != 0)
                            lineNum++;

                        AbstractCard c = allCards.group.get(p);
                        c.target_x = drawStartX + mod * padX;
                        c.target_y = drawStartY + currentDiffY - lineNum * padY;
                        c.update();

                        if (___sortHeader.justSorted) {
                            c.current_x = c.target_x;
                            c.current_y = c.target_y;
                        }

                        i++;
                        p++;
                    }

                    p++;
                }
            }
        }
    }

    private static SortHeaderButtonListener branch;

    @SpirePatch(clz = SortHeaderButton.class, method = "update", paramtypez = {})
    public static class ButtonPatch {
        @SpireInsertPatch(rloc = 16)
        public static void Insert(SortHeaderButton _inst, SortHeaderButtonListener ___delegate) {
            branch = ___delegate;
        }
    }

    @SpirePatch(clz = CardLibSortHeader.class, method = "didChangeOrder",
            paramtypez = {SortHeaderButton.class, boolean.class})
    public static class DidChangeOrderPatch {

        @SpirePrefixPatch
        public static void Prefix(CardLibSortHeader _inst, SortHeaderButton button, boolean isAscending) {
            if (branch != null) {
                branch = null;

                CardGroup backup = _inst.group;
                _inst.group = HeaderFields.signatureGroup.get(_inst);
                _inst.didChangeOrder(button, isAscending);
                _inst.group = backup;
            }
        }
    }

//	@SpirePatch(clz = CardLibSortHeader.class, method = "updateScrollPositions", paramtypez = {})
//	public static class UpdateScrollPositionsPatch {
//		@SpireInsertPatch(rloc = 1, localvars = {"scrolledY"})
//		public static void Insert(CardLibSortHeader _inst, @ByRef float[] scrolledY, ) {
//
//		}
//	}

    @SpirePatch(clz = ColorTabBar.class, method = SpirePatch.CLASS)
    public static class BarFields {
        public static SpireField<Hitbox> signatureHb = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = ColorTabBar.class, method = SpirePatch.CONSTRUCTOR)
    public static class BarConstructorPatch {
        @SpirePostfixPatch
        public static void Postfix(ColorTabBar _inst) {
            BarFields.signatureHb.set(_inst, new Hitbox(280.0F * Settings.scale, 48.0F * Settings.scale));
        }
    }

    @SpirePatch(clz = ColorTabBar.class, method = "update",
            paramtypez = {float.class})
    public static class BarUpdatePatch {
        @SpirePostfixPatch
        public static void Postfix(ColorTabBar _inst, float y, TabBarListener ___delegate) {
            CardLibraryScreen parent = (CardLibraryScreen) ___delegate;
            Hitbox signatureHb = BarFields.signatureHb.get(_inst);

            if (ScreenFields.signatureCards.get(parent).isEmpty())
                signatureHb.move(-1000.0F, -1000.0F);
            else
                signatureHb.move(1150.0F * Settings.xScale, y);
            signatureHb.update();

            if (signatureHb.justHovered)
                CardCrawlGame.sound.playA("UI_HOVER", -0.3F);

            if (signatureHb.hovered && InputHelper.justClickedLeft)
                signatureHb.clickStarted = true;

            if (signatureHb.clicked || (signatureHb.hovered && CInputActionSet.select.isJustPressed())) {
                signatureHb.clicked = false;
                CardCrawlGame.sound.playA("UI_CLICK_1", -0.2F);
                ScreenFields.signatureOnly.set(parent, !ScreenFields.signatureOnly.get(parent));

                CardGroup newVisible = ScreenFields.signatureOnly.get(parent) ?
                        ScreenFields.signatureCards.get(parent) : ScreenFields.allCards.get(parent);
                ReflectionHacks.setPrivate(parent, CardLibraryScreen.class,
                        "visibleCards", newVisible);

                float top = (float) ReflectionHacks.getPrivateStatic(CardLibraryScreen.class, "drawStartY") +
                        (float) ReflectionHacks.getPrivate(parent, CardLibraryScreen.class, "currentDiffY");
                newVisible.getBottomCard().current_y = newVisible.getBottomCard().target_y = top;

//				if (newVisible.group.stream()
//						.noneMatch(c -> ReflectionHacks.privateMethod(AbstractCard.class,
//								"isOnScreen").invoke(c))) {
//					CardLibSortHeader header = ReflectionHacks.getPrivate(parent,
//							CardLibraryScreen.class, "sortHeader");
//					header.justSorted = true;
//				}

//				CardLibSortHeader header = ReflectionHacks.getPrivate(parent,
//						CardLibraryScreen.class, "sortHeader");
//				CardGroup backup = HeaderFields.signatureGroup.get(header);
//				HeaderFields.signatureGroup.set(header, header.group);
//				header.group = backup;
            }
        }
    }

    private static UIStrings uiStrings = null;

    @SpirePatch(clz = ColorTabBar.class, method = "renderViewUpgrade",
            paramtypez = {SpriteBatch.class, float.class})
    public static class BarRenderViewUpgradePatch {
        @SpirePostfixPatch
        public static void Postfix(ColorTabBar _inst, SpriteBatch sb, float y, TabBarListener ___delegate) {
            if (uiStrings == null)
                uiStrings = CardCrawlGame.languagePack.getUIString("SignatureLib:SignatureOnly");

            CardLibraryScreen parent = (CardLibraryScreen) ___delegate;
            if (ScreenFields.signatureCards.get(parent).isEmpty())
                return;

            Color c = Settings.CREAM_COLOR;
            if (ScreenFields.signatureOnly.get(parent))
                c = Settings.GOLD_COLOR;

            FontHelper.renderFontRightAligned(sb, FontHelper.topPanelInfoFont,
                    uiStrings.TEXT[0],
                    1246.0F * Settings.xScale, y, c);

            Texture img = ScreenFields.signatureOnly.get(parent) ?
                    ImageMaster.COLOR_TAB_BOX_TICKED : ImageMaster.COLOR_TAB_BOX_UNTICKED;
            sb.setColor(c);
            sb.draw(img, 1232.0F * Settings.xScale -
                            FontHelper.getSmartWidth(FontHelper.topPanelInfoFont,
                                    uiStrings.TEXT[0], 9999.0F, 0.0F) - 24.0F,
                    y - 24.0F,
                    24.0F, 24.0F,
                    48.0F, 48.0F,
                    Settings.scale, Settings.scale,
                    0.0F,
                    0, 0, 48, 48,
                    false, false);
        }
    }

    @SpirePatch(clz = ColorTabBar.class, method = "render",
            paramtypez = {SpriteBatch.class, float.class})
    public static class BarRenderPatch {
        @SpirePostfixPatch
        public static void Postfix(ColorTabBar _inst, SpriteBatch sb, float y) {
            BarFields.signatureHb.get(_inst).render(sb);
        }
    }
}
