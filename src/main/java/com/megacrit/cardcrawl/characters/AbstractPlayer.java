//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.megacrit.cardcrawl.characters;

import androidTestMod.subscribers.SubscriptionManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.blue.Strike_Blue;
import com.megacrit.cardcrawl.cards.blue.Zap;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.green.Strike_Green;
import com.megacrit.cardcrawl.cards.green.Survivor;
import com.megacrit.cardcrawl.cards.purple.Defend_Watcher;
import com.megacrit.cardcrawl.cards.purple.Eruption;
import com.megacrit.cardcrawl.cards.red.Bash;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.FtueTip.TipType;
import com.megacrit.cardcrawl.ui.MultiPageFtue;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.cardManip.CardDisappearEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class AbstractPlayer extends AbstractCreature {
    private static final Logger logger = LogManager.getLogger(AbstractPlayer.class.getName());
    private static final TutorialStrings tutorialStrings;
    public static final String[] MSG;
    public static final String[] LABEL;
    public PlayerClass chosenClass;
    public int gameHandSize;
    public int masterHandSize;
    public int startingMaxHP;
    public CardGroup masterDeck;
    public CardGroup drawPile;
    public CardGroup hand;
    public CardGroup discardPile;
    public CardGroup exhaustPile;
    public CardGroup limbo;
    public ArrayList<AbstractRelic> relics;
    public ArrayList<AbstractBlight> blights;
    public int potionSlots;
    public ArrayList<AbstractPotion> potions;
    public EnergyManager energy;
    public boolean isEndingTurn;
    public boolean viewingRelics;
    public boolean inspectMode;
    public Hitbox inspectHb;
    public static int poisonKillCount;
    public int damagedThisCombat;
    public String title;
    public ArrayList<AbstractOrb> orbs;
    public int masterMaxOrbs;
    public int maxOrbs;
    public AbstractStance stance;
    /** @deprecated */
    @Deprecated
    public int cardsPlayedThisTurn;
    private boolean isHoveringCard;
    public boolean isHoveringDropZone;
    private float hoverStartLine;
    private boolean passedHesitationLine;
    public AbstractCard hoveredCard;
    public AbstractCard toHover;
    public AbstractCard cardInUse;
    public boolean isDraggingCard;
    private boolean isUsingClickDragControl;
    private float clickDragTimer;
    public boolean inSingleTargetMode;
    private AbstractMonster hoveredMonster;
    public float hoverEnemyWaitTimer;
    private static final float HOVER_ENEMY_WAIT_TIME = 1.0F;
    public boolean isInKeyboardMode;
    private boolean skipMouseModeOnce;
    private int keyboardCardIndex;
    public static ArrayList<String> customMods;
    private int touchscreenInspectCount;
    public Texture img;
    public Texture shoulderImg;
    public Texture shoulder2Img;
    public Texture corpseImg;
    private static final Color ARROW_COLOR;
    private float arrowScale;
    private float arrowScaleTimer;
    private float arrowX;
    private float arrowY;
    private static final float ARROW_TARGET_SCALE = 1.2F;
    private static final int TARGET_ARROW_W = 256;
    public static final float HOVER_CARD_Y_POSITION;
    public boolean endTurnQueued;
    private static final int SEGMENTS = 20;
    private Vector2[] points;
    private Vector2 controlPoint;
    private Vector2 arrowTmp;
    private Vector2 startArrowVector;
    private Vector2 endArrowVector;
    private boolean renderCorpse;
    public static final UIStrings uiStrings;

    public AbstractPlayer(String name, PlayerClass setClass) {
        this.masterDeck = new CardGroup(CardGroupType.MASTER_DECK);
        this.drawPile = new CardGroup(CardGroupType.DRAW_PILE);
        this.hand = new CardGroup(CardGroupType.HAND);
        this.discardPile = new CardGroup(CardGroupType.DISCARD_PILE);
        this.exhaustPile = new CardGroup(CardGroupType.EXHAUST_PILE);
        this.limbo = new CardGroup(CardGroupType.UNSPECIFIED);
        this.relics = new ArrayList();
        this.blights = new ArrayList();
        this.potionSlots = 3;
        this.potions = new ArrayList();
        this.isEndingTurn = false;
        this.viewingRelics = false;
        this.inspectMode = false;
        this.inspectHb = null;
        this.damagedThisCombat = 0;
        this.orbs = new ArrayList();
        this.stance = new NeutralStance();
        this.cardsPlayedThisTurn = 0;
        this.isHoveringCard = false;
        this.isHoveringDropZone = false;
        this.hoverStartLine = 0.0F;
        this.passedHesitationLine = false;
        this.hoveredCard = null;
        this.toHover = null;
        this.cardInUse = null;
        this.isDraggingCard = false;
        this.isUsingClickDragControl = false;
        this.clickDragTimer = 0.0F;
        this.inSingleTargetMode = false;
        this.hoveredMonster = null;
        this.hoverEnemyWaitTimer = 0.0F;
        this.isInKeyboardMode = false;
        this.skipMouseModeOnce = false;
        this.keyboardCardIndex = -1;
        this.touchscreenInspectCount = 0;
        this.arrowScaleTimer = 0.0F;
        this.endTurnQueued = false;
        this.points = new Vector2[20];
        this.controlPoint = new Vector2();
        this.arrowTmp = new Vector2();
        this.startArrowVector = new Vector2();
        this.endArrowVector = new Vector2();
        this.renderCorpse = false;
        this.name = name;
        this.title = this.getTitle(setClass);
        this.drawX = (float)Settings.WIDTH * 0.25F;
        this.drawY = AbstractDungeon.floorY;
        this.chosenClass = setClass;
        this.isPlayer = true;
        this.initializeStarterRelics(setClass);
        this.loadPrefs();
        if (AbstractDungeon.ascensionLevel >= 11) {
            --this.potionSlots;
        }

        this.potions.clear();

        for(int i = 0; i < this.potionSlots; ++i) {
            this.potions.add(new PotionSlot(i));
        }

        for(int i = 0; i < this.points.length; ++i) {
            this.points[i] = new Vector2();
        }

    }

    public abstract String getPortraitImageName();

    public abstract ArrayList<String> getStartingDeck();

    public abstract ArrayList<String> getStartingRelics();

    public abstract CharSelectInfo getLoadout();

    public abstract String getTitle(PlayerClass var1);

    public abstract AbstractCard.CardColor getCardColor();

    public abstract Color getCardRenderColor();

    public abstract String getAchievementKey();

    public abstract ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> var1);

    public abstract AbstractCard getStartCardForEvent();

    public abstract Color getCardTrailColor();

    public abstract String getLeaderboardCharacterName();

    public abstract Texture getEnergyImage();

    public abstract int getAscensionMaxHPLoss();

    public abstract BitmapFont getEnergyNumFont();

    public abstract void renderOrb(SpriteBatch var1, boolean var2, float var3, float var4);

    public abstract void updateOrb(int var1);

    public abstract Prefs getPrefs();

    public abstract void loadPrefs();

    public abstract CharStat getCharStat();

    public abstract int getUnlockedCardCount();

    public abstract int getSeenCardCount();

    public abstract int getCardCount();

    public abstract boolean saveFileExists();

    public abstract String getWinStreakKey();

    public abstract String getLeaderboardWinStreakKey();

    public abstract void renderStatScreen(SpriteBatch var1, float var2, float var3);

    public abstract void doCharSelectScreenSelectEffect();

    public abstract String getCustomModeCharacterButtonSoundKey();

    public abstract Texture getCustomModeCharacterButtonImage();

    public abstract CharacterStrings getCharacterString();

    public abstract String getLocalizedCharacterName();

    public abstract void refreshCharStat();

    public abstract AbstractPlayer newInstance();

    public abstract TextureAtlas.AtlasRegion getOrb();

    public abstract String getSpireHeartText();

    public abstract Color getSlashAttackColor();

    public abstract AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect();

    public abstract String getVampireText();

    public String getSaveFilePath() {
        return SaveAndContinue.getPlayerSavePath(this.chosenClass);
    }

    public void dispose() {
        if (this.atlas != null) {
            this.atlas.dispose();
        }

        if (this.img != null) {
            this.img.dispose();
        }

        if (this.shoulderImg != null) {
            this.shoulderImg.dispose();
        }

        if (this.shoulder2Img != null) {
            this.shoulder2Img.dispose();
        }

        if (this.corpseImg != null) {
            this.corpseImg.dispose();
        }

    }

    public void adjustPotionPositions() {
        for(int i = 0; i < this.potions.size() - 1; ++i) {
            ((AbstractPotion)this.potions.get(i)).adjustPosition(i);
        }

    }

    protected void initializeClass(String imgUrl, String shoulder2ImgUrl, String shouldImgUrl, String corpseImgUrl, CharSelectInfo info, float hb_x, float hb_y, float hb_w, float hb_h, EnergyManager energy) {
        if (imgUrl != null) {
            this.img = ImageMaster.loadImage(imgUrl);
        }

        if (this.img != null) {
            this.atlas = null;
        }

        this.shoulderImg = ImageMaster.loadImage(shouldImgUrl);
        this.shoulder2Img = ImageMaster.loadImage(shoulder2ImgUrl);
        this.corpseImg = ImageMaster.loadImage(corpseImgUrl);
        if (Settings.isMobile) {
            hb_w *= 1.17F;
        }

        this.maxHealth = info.maxHp;
        this.startingMaxHP = this.maxHealth;
        this.currentHealth = info.currentHp;
        this.masterMaxOrbs = info.maxOrbs;
        this.energy = energy;
        this.masterHandSize = info.cardDraw;
        this.gameHandSize = this.masterHandSize;
        this.gold = info.gold;
        this.displayGold = this.gold;
        this.hb_h = hb_h * Settings.xScale;
        this.hb_w = hb_w * Settings.scale;
        this.hb_x = hb_x * Settings.scale;
        this.hb_y = hb_y * Settings.scale;
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.healthHb = new Hitbox(this.hb.width, 72.0F * Settings.scale);
        this.refreshHitboxLocation();
    }

    public void initializeStarterDeck() {
        ArrayList<String> cards = this.getStartingDeck();
        boolean addBaseCards = true;
        if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("Chimera") || ModHelper.isModEnabled("SealedDeck") || ModHelper.isModEnabled("Shiny") || ModHelper.isModEnabled("Insanity")) {
            addBaseCards = false;
        }

        if (ModHelper.isModEnabled("Chimera")) {
            this.masterDeck.addToTop(new Bash());
            this.masterDeck.addToTop(new Survivor());
            this.masterDeck.addToTop(new Zap());
            this.masterDeck.addToTop(new Eruption());
            this.masterDeck.addToTop(new Strike_Red());
            this.masterDeck.addToTop(new Strike_Green());
            this.masterDeck.addToTop(new Strike_Blue());
            this.masterDeck.addToTop(new Defend_Red());
            this.masterDeck.addToTop(new Defend_Green());
            this.masterDeck.addToTop(new Defend_Watcher());
        }

        if (ModHelper.isModEnabled("Insanity")) {
            for(int i = 0; i < 50; ++i) {
                this.masterDeck.addToTop(AbstractDungeon.returnRandomCard().makeCopy());
            }
        }

        if (ModHelper.isModEnabled("Shiny")) {
            CardGroup group = AbstractDungeon.getEachRare();

            for(AbstractCard c : group.group) {
                this.masterDeck.addToTop(c);
            }
        }

        if (addBaseCards) {
            for(String s : cards) {
                this.masterDeck.addToTop(CardLibrary.getCard(this.chosenClass, s).makeCopy());
            }
        }

        for(AbstractCard c : this.masterDeck.group) {
            UnlockTracker.markCardAsSeen(c.cardID);
        }

    }

    protected void initializeStarterRelics(PlayerClass chosenClass) {
        ArrayList<String> relics = this.getStartingRelics();
        if (ModHelper.isModEnabled("Cursed Run")) {
            relics.clear();
            relics.add("Cursed Key");
            relics.add("Darkstone Periapt");
            relics.add("Du-Vu Doll");
        }

        if (ModHelper.isModEnabled("ControlledChaos")) {
            relics.add("Frozen Eye");
        }

        int index = 0;

        for(String s : relics) {
            RelicLibrary.getRelic(s).makeCopy().instantObtain(this, index, false);
            ++index;
        }

        AbstractDungeon.relicsToRemoveOnStart.addAll(relics);
    }

    public void combatUpdate() {
        if (this.cardInUse != null) {
            this.cardInUse.update();
        }

        this.limbo.update();
        this.exhaustPile.update();

        for(AbstractPower p : this.powers) {
            p.updateParticles();
        }

        for(AbstractOrb o : this.orbs) {
            o.update();
        }

        this.stance.update();
    }

    public void update() {
        this.updateControllerInput();
        this.hb.update();
        this.updateHealthBar();
        this.updatePowers();
        this.healthHb.update();
        this.updateReticle();
        this.tint.update();
        if (AbstractDungeon.getCurrRoom().phase != RoomPhase.EVENT) {
            for(AbstractOrb o : this.orbs) {
                o.updateAnimation();
            }
        }

        this.updateEscapeAnimation();
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden) {
            boolean anyHovered = false;
            boolean orbHovered = false;
            int orbIndex = 0;

            for(AbstractOrb o : this.orbs) {
                if (o.hb.hovered) {
                    orbHovered = true;
                    break;
                }

                ++orbIndex;
            }

            if (!orbHovered || !CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                if (!orbHovered || !CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                    if (!orbHovered || !CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                        if (!this.inspectMode || !CInputActionSet.down.isJustPressed() && !CInputActionSet.altDown.isJustPressed()) {
                            if (!this.inspectMode && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
                                if ((float)Gdx.input.getX() < (float)Settings.WIDTH / 2.0F) {
                                    this.inspectHb = this.hb;
                                } else if (AbstractDungeon.getMonsters().monsters.isEmpty()) {
                                    this.inspectHb = this.hb;
                                } else {
                                    ArrayList<Hitbox> hbs = new ArrayList();

                                    for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                                        if (!m.isDying && !m.isEscaping) {
                                            hbs.add(m.hb);
                                        }
                                    }

                                    if (!hbs.isEmpty()) {
                                        this.inspectHb = (Hitbox)hbs.get(0);
                                    } else {
                                        this.inspectHb = this.hb;
                                    }
                                }

                                CInputHelper.setCursor(this.inspectHb);
                                this.inspectMode = true;
                                this.releaseCard();
                            } else if (this.inspectMode && (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
                                ArrayList<Hitbox> hbs = new ArrayList();
                                hbs.add(this.hb);

                                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                                    if (!m.isDying && !m.isEscaping) {
                                        hbs.add(m.hb);
                                    }
                                }

                                int index = 0;

                                for(Hitbox h : hbs) {
                                    h.update();
                                    if (h.hovered) {
                                        anyHovered = true;
                                        break;
                                    }

                                    ++index;
                                }

                                if (!anyHovered) {
                                    CInputHelper.setCursor((Hitbox)hbs.get(0));
                                    this.inspectHb = (Hitbox)hbs.get(0);
                                } else {
                                    ++index;
                                    if (index > hbs.size() - 1) {
                                        index = 0;
                                    }

                                    CInputHelper.setCursor((Hitbox)hbs.get(index));
                                    this.inspectHb = (Hitbox)hbs.get(index);
                                }

                                this.inspectMode = true;
                                this.releaseCard();
                            } else if (this.inspectMode && (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
                                ArrayList<Hitbox> hbs = new ArrayList();
                                hbs.add(this.hb);

                                for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                                    if (!m.isDying && !m.isEscaping) {
                                        hbs.add(m.hb);
                                    }
                                }

                                int index = 0;

                                for(Hitbox h : hbs) {
                                    if (h.hovered) {
                                        anyHovered = true;
                                        break;
                                    }

                                    ++index;
                                }

                                if (!anyHovered) {
                                    CInputHelper.setCursor((Hitbox)hbs.get(hbs.size() - 1));
                                    this.inspectHb = (Hitbox)hbs.get(hbs.size() - 1);
                                } else {
                                    --index;
                                    if (index < 0) {
                                        index = hbs.size() - 1;
                                    }

                                    CInputHelper.setCursor((Hitbox)hbs.get(index));
                                    this.inspectHb = (Hitbox)hbs.get(index);
                                }

                                this.inspectMode = true;
                                this.releaseCard();
                            } else if (this.inspectMode && (CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
                                CInputActionSet.up.unpress();
                                CInputActionSet.altUp.unpress();
                                if (!orbHovered && !this.orbs.isEmpty()) {
                                    CInputHelper.setCursor(((AbstractOrb)this.orbs.get(0)).hb);
                                    this.inspectHb = ((AbstractOrb)this.orbs.get(0)).hb;
                                } else {
                                    this.inspectMode = false;
                                    this.inspectHb = null;
                                    this.viewingRelics = true;
                                    if (!this.blights.isEmpty()) {
                                        CInputHelper.setCursor(((AbstractBlight)this.blights.get(0)).hb);
                                    } else {
                                        CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
                                    }
                                }
                            }
                        } else if (orbHovered) {
                            this.inspectHb = this.hb;
                            CInputHelper.setCursor(this.inspectHb);
                        } else {
                            this.inspectMode = false;
                            this.inspectHb = null;
                            if (!this.hand.isEmpty() && !AbstractDungeon.actionManager.turnHasEnded) {
                                this.hoveredCard = (AbstractCard)this.hand.group.get(0);
                                this.hoverCardInHand(this.hoveredCard);
                                this.keyboardCardIndex = 0;
                            }
                        }
                    } else {
                        --orbIndex;
                        if (orbIndex < 0) {
                            orbIndex = this.orbs.size() - 1;
                        }

                        this.inspectHb = ((AbstractOrb)this.orbs.get(orbIndex)).hb;
                        Gdx.input.setCursorPosition((int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cX, Settings.HEIGHT - (int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cY);
                    }
                } else {
                    ++orbIndex;
                    if (orbIndex > this.orbs.size() - 1) {
                        orbIndex = 0;
                    }

                    this.inspectHb = ((AbstractOrb)this.orbs.get(orbIndex)).hb;
                    Gdx.input.setCursorPosition((int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cX, Settings.HEIGHT - (int)((AbstractOrb)this.orbs.get(orbIndex)).hb.cY);
                }
            } else {
                CInputActionSet.up.unpress();
                CInputActionSet.altUp.unpress();
                this.inspectMode = false;
                this.inspectHb = null;
                orbHovered = false;
                this.viewingRelics = true;
                if (!this.blights.isEmpty()) {
                    CInputHelper.setCursor(((AbstractBlight)this.blights.get(0)).hb);
                } else {
                    CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
                }
            }

        }
    }

    public void updateViewRelicControls() {
        int index = 0;
        boolean anyHovered = false;
        RHoverType type = AbstractPlayer.RHoverType.RELIC;

        for(AbstractRelic r : this.relics) {
            if (r.hb.hovered) {
                anyHovered = true;
                break;
            }

            ++index;
        }

        if (!anyHovered) {
            index = 0;

            for(AbstractBlight b : this.blights) {
                if (b.hb.hovered) {
                    anyHovered = true;
                    type = AbstractPlayer.RHoverType.BLIGHT;
                    break;
                }

                ++index;
            }
        }

        if (!anyHovered) {
            CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
        } else if (!CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
            if (!CInputActionSet.right.isJustPressed() && !CInputActionSet.altRight.isJustPressed()) {
                if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                    if (CInputActionSet.cancel.isJustPressed()) {
                        this.viewingRelics = false;
                        Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
                    } else if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
                        if (type == AbstractPlayer.RHoverType.RELIC) {
                            if (this.blights.isEmpty()) {
                                CInputActionSet.down.unpress();
                                CInputActionSet.altDown.unpress();
                                this.viewingRelics = false;
                                this.inspectMode = true;
                                if (!this.orbs.isEmpty()) {
                                    this.inspectHb = ((AbstractOrb)this.orbs.get(0)).hb;
                                } else {
                                    this.inspectHb = this.hb;
                                }

                                CInputHelper.setCursor(this.inspectHb);
                            } else {
                                CInputHelper.setCursor(((AbstractBlight)this.blights.get(0)).hb);
                            }
                        } else {
                            CInputActionSet.down.unpress();
                            CInputActionSet.altDown.unpress();
                            this.viewingRelics = false;
                            this.inspectMode = true;
                            if (!this.orbs.isEmpty()) {
                                this.inspectHb = ((AbstractOrb)this.orbs.get(0)).hb;
                            } else {
                                this.inspectHb = this.hb;
                            }

                            CInputHelper.setCursor(this.inspectHb);
                        }
                    }
                } else {
                    CInputActionSet.up.unpress();
                    if (type == AbstractPlayer.RHoverType.RELIC) {
                        this.viewingRelics = false;
                        AbstractDungeon.topPanel.selectPotionMode = true;
                        CInputHelper.setCursor(((AbstractPotion)this.potions.get(0)).hb);
                    } else {
                        CInputHelper.setCursor(((AbstractRelic)this.relics.get(0)).hb);
                    }
                }
            } else {
                ++index;
                if (type == AbstractPlayer.RHoverType.RELIC) {
                    if (index > this.relics.size() - 1) {
                        index = 0;
                    } else if (index > (AbstractRelic.relicPage + 1) * AbstractRelic.MAX_RELICS_PER_PAGE - 1) {
                        ++AbstractRelic.relicPage;
                        AbstractDungeon.topPanel.adjustRelicHbs();
                        index = AbstractRelic.relicPage * AbstractRelic.MAX_RELICS_PER_PAGE;
                    }

                    CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
                } else {
                    if (index > this.blights.size() - 1) {
                        index = 0;
                    }

                    CInputHelper.setCursor(((AbstractBlight)this.blights.get(index)).hb);
                }
            }
        } else {
            --index;
            if (type == AbstractPlayer.RHoverType.RELIC) {
                if (index < AbstractRelic.relicPage * AbstractRelic.MAX_RELICS_PER_PAGE) {
                    --AbstractRelic.relicPage;
                    if (AbstractRelic.relicPage < 0) {
                        if (this.relics.size() <= AbstractRelic.MAX_RELICS_PER_PAGE) {
                            AbstractRelic.relicPage = 0;
                        } else {
                            AbstractRelic.relicPage = this.relics.size() / AbstractRelic.MAX_RELICS_PER_PAGE;
                            AbstractDungeon.topPanel.adjustRelicHbs();
                        }

                        index = this.relics.size() - 1;
                    } else {
                        index = (AbstractRelic.relicPage + 1) * AbstractRelic.MAX_RELICS_PER_PAGE - 1;
                        AbstractDungeon.topPanel.adjustRelicHbs();
                    }
                }

                CInputHelper.setCursor(((AbstractRelic)this.relics.get(index)).hb);
            } else {
                if (index < 0) {
                    index = this.blights.size() - 1;
                }

                CInputHelper.setCursor(((AbstractBlight)this.blights.get(index)).hb);
            }
        }

    }

    public void loseGold(int goldAmount) {
        if (AbstractDungeon.getCurrRoom() instanceof ShopRoom) {
            for(AbstractRelic r : this.relics) {
                r.onSpendGold();
            }
        }

        if (!(AbstractDungeon.getCurrRoom() instanceof ShopRoom) && AbstractDungeon.getCurrRoom().phase != RoomPhase.COMBAT) {
            CardCrawlGame.sound.play("EVENT_PURCHASE");
        }

        if (goldAmount > 0) {
            this.gold -= goldAmount;
            if (this.gold < 0) {
                this.gold = 0;
            }

            for(AbstractRelic r : this.relics) {
                r.onLoseGold();
            }
        } else {
            logger.info("NEGATIVE MONEY???");
        }

    }

    public void gainGold(int amount) {
        if (this.hasRelic("Ectoplasm")) {
            this.getRelic("Ectoplasm").flash();
        } else {
            if (amount <= 0) {
                logger.info("NEGATIVE MONEY???");
            } else {
                CardCrawlGame.goldGained += amount;
                this.gold += amount;

                for(AbstractRelic r : this.relics) {
                    r.onGainGold();
                }
            }

        }
    }

    public void playDeathAnimation() {
        this.img = this.corpseImg;
        this.renderCorpse = true;
    }

    public boolean isCursed() {
        boolean cursed = false;

        for(AbstractCard c : this.masterDeck.group) {
            if (c.type == CardType.CURSE && c.cardID != "Necronomicurse" && c.cardID != "CurseOfTheBell" && c.cardID != "AscendersBane") {
                cursed = true;
            }
        }

        return cursed;
    }

    public void updateInput() {
        if (!this.viewingRelics) {
            if (this.hoverEnemyWaitTimer > 0.0F) {
                this.hoverEnemyWaitTimer -= Gdx.graphics.getDeltaTime();
            }

            if (this.inSingleTargetMode) {
                this.updateSingleTargetInput();
            } else {
                int y = InputHelper.mY;
                boolean hMonster = false;

                for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    m.hb.update();
                    if (m.hb.hovered && !m.isDying && !m.isEscaping && m.currentHealth > 0) {
                        hMonster = true;
                        break;
                    }
                }

                boolean tmp = this.isHoveringDropZone;
                if (!Settings.isTouchScreen) {
                    this.isHoveringDropZone = ((float)y > this.hoverStartLine || (float)y > 300.0F * Settings.scale) && (float)y < Settings.CARD_DROP_END_Y || hMonster || Settings.isControllerMode;
                } else {
                    this.isHoveringDropZone = (float)y > 350.0F * Settings.scale && (float)y < Settings.CARD_DROP_END_Y || hMonster || Settings.isControllerMode;
                }

                if (!tmp && this.isHoveringDropZone && this.isDraggingCard) {
                    this.hoveredCard.flash(Color.SKY.cpy());
                    if (this.hoveredCard.showEvokeValue) {
                        if (this.hoveredCard.showEvokeOrbCount == 0) {
                            for(AbstractOrb o : this.orbs) {
                                o.showEvokeValue();
                            }
                        } else {
                            int tmpShowCount = this.hoveredCard.showEvokeOrbCount;
                            int emptyCount = 0;

                            for(AbstractOrb o : this.orbs) {
                                if (o instanceof EmptyOrbSlot) {
                                    ++emptyCount;
                                }
                            }

                            tmpShowCount -= emptyCount;
                            if (tmpShowCount > 0) {
                                for(AbstractOrb o : this.orbs) {
                                    o.showEvokeValue();
                                    --tmpShowCount;
                                    if (tmpShowCount <= 0) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (this.isDraggingCard && this.isHoveringDropZone && this.hoveredCard != null) {
                    this.passedHesitationLine = true;
                }

                if (this.isDraggingCard && (float)y < 50.0F * Settings.scale && this.passedHesitationLine) {
                    if (Settings.isTouchScreen) {
                        InputHelper.moveCursorToNeutralPosition();
                    }

                    this.releaseCard();
                    CardCrawlGame.sound.play("UI_CLICK_1");
                }

                this.updateFullKeyboardCardSelection();
                if (this.isInKeyboardMode && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.potionUi.targetMode && !AbstractDungeon.isScreenUp) {
                    if (this.toHover != null) {
                        this.releaseCard();
                        this.hoveredCard = this.toHover;
                        this.toHover = null;
                        this.isHoveringCard = true;
                        this.hoveredCard.current_y = HOVER_CARD_Y_POSITION;
                        this.hoveredCard.target_y = HOVER_CARD_Y_POSITION;
                        this.hoveredCard.setAngle(0.0F, true);
                        this.hand.hoverCardPush(this.hoveredCard);
                    }
                } else if (this.hoveredCard == null && AbstractDungeon.screen == CurrentScreen.NONE && !AbstractDungeon.topPanel.selectPotionMode) {
                    this.isHoveringCard = false;
                    if (this.toHover != null) {
                        this.hoveredCard = this.toHover;
                        this.toHover = null;
                    } else {
                        this.hoveredCard = this.hand.getHoveredCard();
                    }

                    if (this.hoveredCard != null) {
                        this.isHoveringCard = true;
                        this.hoveredCard.current_y = HOVER_CARD_Y_POSITION;
                        this.hoveredCard.target_y = HOVER_CARD_Y_POSITION;
                        this.hoveredCard.setAngle(0.0F, true);
                        this.hand.hoverCardPush(this.hoveredCard);
                    }
                }

                if (this.hoveredCard != null) {
                    this.hoveredCard.drawScale = 1.0F;
                    this.hoveredCard.targetDrawScale = 1.0F;
                }

                if (!this.isDraggingCard && this.hasRelic("Necronomicon")) {
                    this.getRelic("Necronomicon").stopPulse();
                }

                if (!this.endTurnQueued) {
                    if (!AbstractDungeon.actionManager.turnHasEnded && this.clickAndDragCards()) {
                        return;
                    }

                    if (!this.isInKeyboardMode && this.isHoveringCard && this.hoveredCard != null && !this.hoveredCard.isHoveredInHand(1.0F)) {
                        for(int i = 0; i < this.hand.group.size(); ++i) {
                            if (this.hand.group.get(i) == this.hoveredCard && i != 0 && ((AbstractCard)this.hand.group.get(i - 1)).isHoveredInHand(1.0F)) {
                                this.toHover = (AbstractCard)this.hand.group.get(i - 1);
                                break;
                            }
                        }

                        this.releaseCard();
                    }

                    if (this.hoveredCard != null) {
                        this.hoveredCard.updateHoverLogic();
                    }
                } else if (AbstractDungeon.actionManager.cardQueue.isEmpty() && !AbstractDungeon.actionManager.hasControl) {
                    this.endTurnQueued = false;
                    this.isEndingTurn = true;
                }
            }

        }
    }

    private void updateSingleTargetInput() {
        if (Settings.isTouchScreen && !Settings.isControllerMode && !this.isUsingClickDragControl && !InputHelper.isMouseDown) {
            Gdx.input.setCursorPosition((int)MathUtils.lerp((float)Gdx.input.getX(), (float)Settings.WIDTH / 2.0F, Gdx.graphics.getDeltaTime() * 10.0F), (int)MathUtils.lerp((float)Gdx.input.getY(), (float)Settings.HEIGHT * 1.1F, Gdx.graphics.getDeltaTime() * 4.0F));
        }

        if (this.isInKeyboardMode) {
            if (!InputActionSet.releaseCard.isJustPressed() && !CInputActionSet.cancel.isJustPressed()) {
                this.updateTargetArrowWithKeyboard(false);
            } else {
                AbstractCard card = this.hoveredCard;
                this.inSingleTargetMode = false;
                this.hoveredMonster = null;
                this.hoverCardInHand(card);
            }
        } else {
            this.hoveredMonster = null;

            for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                m.hb.update();
                if (m.hb.hovered && !m.isDying && !m.isEscaping && m.currentHealth > 0) {
                    this.hoveredMonster = m;
                    break;
                }
            }
        }

        if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && !InputHelper.justClickedRight && !((float)InputHelper.mY < 50.0F * Settings.scale) && !((float)InputHelper.mY < this.hoverStartLine - 400.0F * Settings.scale)) {
            AbstractCard cardFromHotkey = InputHelper.getCardSelectedByHotkey(this.hand);
            if (cardFromHotkey != null && !this.isCardQueued(cardFromHotkey)) {
                boolean isSameCard = cardFromHotkey == this.hoveredCard;
                this.releaseCard();
                this.hoveredMonster = null;
                if (isSameCard) {
                    GameCursor.hidden = false;
                } else {
                    this.hoveredCard = cardFromHotkey;
                    this.hoveredCard.setAngle(0.0F, false);
                    this.isUsingClickDragControl = true;
                    this.isDraggingCard = true;
                }
            }

            if (!InputHelper.justClickedLeft && !InputActionSet.confirm.isJustPressed() && !CInputActionSet.select.isJustPressed()) {
                if (!this.isUsingClickDragControl && InputHelper.justReleasedClickLeft && this.hoveredMonster != null) {
                    if (this.hoveredCard.canUse(this, this.hoveredMonster)) {
                        this.playCard();
                    } else {
                        AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
                        this.energyTip(this.hoveredCard);
                        this.releaseCard();
                    }

                    this.inSingleTargetMode = false;
                    GameCursor.hidden = false;
                    this.hoveredMonster = null;
                }
            } else {
                InputHelper.justClickedLeft = false;
                if (this.hoveredMonster == null) {
                    CardCrawlGame.sound.play("UI_CLICK_1");
                } else {
                    if (this.hoveredCard.canUse(this, this.hoveredMonster)) {
                        this.playCard();
                    } else {
                        AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
                        this.energyTip(this.hoveredCard);
                        this.releaseCard();
                    }

                    this.isUsingClickDragControl = false;
                    this.inSingleTargetMode = false;
                    GameCursor.hidden = false;
                    this.hoveredMonster = null;
                }
            }
        } else {
            if (Settings.isTouchScreen) {
                InputHelper.moveCursorToNeutralPosition();
            }

            this.releaseCard();
            CardCrawlGame.sound.play("UI_CLICK_2");
            this.isUsingClickDragControl = false;
            this.inSingleTargetMode = false;
            GameCursor.hidden = false;
            this.hoveredMonster = null;
        }
    }

    private boolean isCardQueued(AbstractCard card) {
        for(CardQueueItem item : AbstractDungeon.actionManager.cardQueue) {
            if (item.card == card) {
                return true;
            }
        }

        return false;
    }

    private void energyTip(AbstractCard cardToCheck) {
        int availableEnergy = EnergyPanel.totalCount;
        if (cardToCheck.cost > availableEnergy && !(Boolean)TipTracker.tips.get("ENERGY_USE_TIP")) {
            ++TipTracker.energyUseCounter;
            if (TipTracker.energyUseCounter >= 2) {
                AbstractDungeon.ftue = new FtueTip(LABEL[1], MSG[1], 330.0F * Settings.scale, 400.0F * Settings.scale, TipType.ENERGY);
                TipTracker.neverShowAgain("ENERGY_USE_TIP");
            }
        }

    }

    private boolean updateFullKeyboardCardSelection() {
        if (Settings.isControllerMode || InputActionSet.left.isJustPressed() || InputActionSet.right.isJustPressed() || InputActionSet.confirm.isJustPressed()) {
            this.isInKeyboardMode = true;
            this.skipMouseModeOnce = true;
            GameCursor.hidden = true;
        }

        if (this.isInKeyboardMode && InputHelper.didMoveMouse()) {
            if (this.skipMouseModeOnce) {
                this.skipMouseModeOnce = false;
            } else {
                this.isInKeyboardMode = false;
                GameCursor.hidden = false;
            }
        }

        if (this.isInKeyboardMode && !this.hand.isEmpty() && !this.inspectMode) {
            if (this.keyboardCardIndex == -2) {
                if (!InputActionSet.left.isJustPressed() && !CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                    if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                        this.keyboardCardIndex = 0;
                    }
                } else {
                    this.keyboardCardIndex = this.hand.size() - 1;
                }

                return false;
            } else {
                if (!InputActionSet.left.isJustPressed() && !CInputActionSet.left.isJustPressed() && !CInputActionSet.altLeft.isJustPressed()) {
                    if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                        ++this.keyboardCardIndex;
                    }
                } else {
                    --this.keyboardCardIndex;
                }

                this.keyboardCardIndex = (this.keyboardCardIndex + this.hand.size()) % this.hand.size();
                if (!AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.potionUi.targetMode) {
                    AbstractCard card = (AbstractCard)this.hand.group.get(this.keyboardCardIndex);
                    if (card != this.hoveredCard && Math.abs(card.current_x - card.target_x) < 400.0F * Settings.scale) {
                        this.hoverCardInHand(card);
                        return true;
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    private void hoverCardInHand(AbstractCard card) {
        this.toHover = card;
        if (Settings.isControllerMode && AbstractDungeon.actionManager.turnHasEnded) {
            this.toHover = null;
        }

        if (card != null && !this.inspectMode) {
            Gdx.input.setCursorPosition((int)card.hb.cX, (int)((float)Settings.HEIGHT - HOVER_CARD_Y_POSITION));
        }

    }

    private void updateTargetArrowWithKeyboard(boolean autoTargetFirst) {
        int directionIndex = 0;
        if (autoTargetFirst) {
            ++directionIndex;
        }

        if (InputActionSet.left.isJustPressed() || CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
            --directionIndex;
        }

        if (InputActionSet.right.isJustPressed() || CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
            ++directionIndex;
        }

        if (directionIndex != 0) {
            ArrayList<AbstractMonster> prefiltered = AbstractDungeon.getCurrRoom().monsters.monsters;
            ArrayList<AbstractMonster> sortedMonsters = new ArrayList(AbstractDungeon.getCurrRoom().monsters.monsters);

            for(AbstractMonster mons : prefiltered) {
                if (mons.isDying) {
                    sortedMonsters.remove(mons);
                }
            }

            sortedMonsters.sort(AbstractMonster.sortByHitbox);
            if (sortedMonsters.isEmpty()) {
                return;
            }

            for(AbstractMonster m : sortedMonsters) {
                if (m.hb.hovered) {
                    this.hoveredMonster = m;
                    break;
                }
            }

            AbstractMonster newTarget = null;
            if (this.hoveredMonster == null) {
                if (directionIndex == 1) {
                    newTarget = (AbstractMonster)sortedMonsters.get(0);
                } else {
                    newTarget = (AbstractMonster)sortedMonsters.get(sortedMonsters.size() - 1);
                }
            } else {
                int currentTargetIndex = sortedMonsters.indexOf(this.hoveredMonster);
                int newTargetIndex = currentTargetIndex + directionIndex;
                newTargetIndex = (newTargetIndex + sortedMonsters.size()) % sortedMonsters.size();
                newTarget = (AbstractMonster)sortedMonsters.get(newTargetIndex);
            }

            if (newTarget != null) {
                Hitbox target = newTarget.hb;
                Gdx.input.setCursorPosition((int)target.cX, Settings.HEIGHT - (int)target.cY);
                this.hoveredMonster = newTarget;
                this.isUsingClickDragControl = true;
                this.isDraggingCard = true;
            }

            if (this.hoveredMonster.halfDead) {
                this.hoveredMonster = null;
            }
        }

    }

    private void renderCardHotKeyText(SpriteBatch sb) {
        int index = 0;

        for(AbstractCard card : this.hand.group) {
            if (index < InputActionSet.selectCardActions.length) {
                float width = AbstractCard.IMG_WIDTH * card.drawScale / 2.0F;
                float height = AbstractCard.IMG_HEIGHT * card.drawScale / 2.0F;
                float topOfCard = card.current_y + height;
                float textSpacing = 50.0F * Settings.scale;
                float textY = topOfCard + textSpacing;
                float sin = (float)Math.sin((double)(card.angle / 180.0F) * Math.PI);
                float xOffset = sin * width;
                FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, InputActionSet.selectCardActions[index].getKeyString(), card.current_x - xOffset, textY, Settings.CREAM_COLOR);
            }

            ++index;
        }

    }

    private boolean clickAndDragCards() {
        boolean simulateRightClickDrop = false;
        AbstractCard cardFromHotkey = InputHelper.getCardSelectedByHotkey(this.hand);
        if (cardFromHotkey != null && !this.isCardQueued(cardFromHotkey)) {
            if (this.isDraggingCard) {
                simulateRightClickDrop = cardFromHotkey == this.hoveredCard;
                CardCrawlGame.sound.play("UI_CLICK_2");
                this.releaseCard();
            }

            if (!simulateRightClickDrop) {
                this.manuallySelectCard(cardFromHotkey);
            }
        }

        if (CInputActionSet.select.isJustPressed() && this.hoveredCard != null && !this.isCardQueued(this.hoveredCard) && !this.isDraggingCard) {
            this.manuallySelectCard(this.hoveredCard);
            if (this.hoveredCard.target == CardTarget.ENEMY) {
                this.updateTargetArrowWithKeyboard(true);
            } else {
                InputHelper.moveCursorToNeutralPosition();
            }

            return true;
        } else {
            if (InputHelper.justClickedLeft && this.isHoveringCard && !this.isDraggingCard) {
                this.hoverStartLine = (float)InputHelper.mY + 140.0F * Settings.scale;
                InputHelper.justClickedLeft = false;
                if (this.hoveredCard != null) {
                    CardCrawlGame.sound.play("CARD_OBTAIN");
                    this.isDraggingCard = true;
                    this.passedHesitationLine = false;
                    this.hoveredCard.targetDrawScale = 0.7F;
                    if (Settings.isTouchScreen && !Settings.isControllerMode && this.touchscreenInspectCount == 0) {
                        this.hoveredCard.current_y = AbstractCard.IMG_HEIGHT / 2.0F;
                        this.hoveredCard.target_y = AbstractCard.IMG_HEIGHT / 2.0F;
                        Gdx.input.setCursorPosition((int)this.hoveredCard.current_x, (int)((float)Settings.HEIGHT - AbstractCard.IMG_HEIGHT / 2.0F));
                        this.touchscreenInspectCount = 0;
                    }

                    return true;
                }
            }

            if (InputHelper.isMouseDown) {
                this.clickDragTimer += Gdx.graphics.getDeltaTime();
            } else {
                this.clickDragTimer = 0.0F;
            }

            if ((InputHelper.justClickedLeft || InputActionSet.confirm.isJustPressed() || CInputActionSet.select.isJustPressed()) && this.isUsingClickDragControl) {
                if (!InputHelper.justClickedRight && !simulateRightClickDrop) {
                    InputHelper.justClickedLeft = false;
                    if (this.isHoveringDropZone && this.hoveredCard.canUse(this, (AbstractMonster)null) && this.hoveredCard.target != CardTarget.ENEMY && this.hoveredCard.target != CardTarget.SELF_AND_ENEMY) {
                        this.playCard();
                    } else {
                        CardCrawlGame.sound.play("CARD_OBTAIN");
                        this.releaseCard();
                    }

                    this.isUsingClickDragControl = false;
                    return true;
                } else {
                    CardCrawlGame.sound.play("UI_CLICK_2");
                    this.releaseCard();
                    return true;
                }
            } else {
                if (this.isInKeyboardMode) {
                    if (!InputActionSet.releaseCard.isJustPressed() && !CInputActionSet.cancel.isJustPressed()) {
                        if ((InputActionSet.confirm.isJustPressed() || CInputActionSet.select.isJustPressed()) && this.hoveredCard != null) {
                            this.manuallySelectCard(this.hoveredCard);
                            if (this.hoveredCard.target == CardTarget.ENEMY) {
                                this.updateTargetArrowWithKeyboard(true);
                            } else {
                                Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
                            }
                        }
                    } else {
                        this.hoverCardInHand(this.hoveredCard);
                    }
                }

                if (!this.isDraggingCard || !InputHelper.isMouseDown && !this.isUsingClickDragControl) {
                    if (!this.isDraggingCard || !InputHelper.justReleasedClickLeft || Settings.isTouchScreen && !Settings.isControllerMode) {
                        if (Settings.isTouchScreen && !Settings.isControllerMode && InputHelper.justReleasedClickLeft && this.hoveredCard != null) {
                            ++this.touchscreenInspectCount;
                            if (this.isHoveringDropZone && this.hoveredCard.hasEnoughEnergy() && this.hoveredCard.canUse(this, (AbstractMonster)null)) {
                                this.playCard();
                                return true;
                            }

                            if (this.touchscreenInspectCount > 1) {
                                AbstractCard newHoveredCard = null;

                                for(AbstractCard c : this.hand.group) {
                                    c.updateHoverLogic();
                                    if (c.hb.hovered && c != this.hoveredCard) {
                                        newHoveredCard = c;
                                        break;
                                    }
                                }

                                this.releaseCard();
                                if (newHoveredCard == null) {
                                    InputHelper.moveCursorToNeutralPosition();
                                } else {
                                    newHoveredCard.current_y = AbstractCard.IMG_HEIGHT / 2.0F;
                                    newHoveredCard.target_y = AbstractCard.IMG_HEIGHT / 2.0F;
                                    newHoveredCard.angle = 0.0F;
                                    Gdx.input.setCursorPosition((int)newHoveredCard.current_x, (int)((float)Settings.HEIGHT - AbstractCard.IMG_HEIGHT / 2.0F));
                                    this.touchscreenInspectCount = 1;
                                }
                            }
                        }
                    } else {
                        if (this.isHoveringDropZone) {
                            if (this.hoveredCard.target != CardTarget.ENEMY && this.hoveredCard.target != CardTarget.SELF_AND_ENEMY) {
                                if (this.hoveredCard.canUse(this, (AbstractMonster)null)) {
                                    this.playCard();
                                    return true;
                                }

                                AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
                                this.energyTip(this.hoveredCard);
                                this.releaseCard();
                                return true;
                            }

                            this.inSingleTargetMode = true;
                            this.arrowX = (float)InputHelper.mX;
                            this.arrowY = (float)InputHelper.mY;
                            GameCursor.hidden = true;
                            this.hoveredCard.untip();
                            this.hand.refreshHandLayout();
                            this.hoveredCard.target_y = AbstractCard.IMG_HEIGHT * 0.75F / 2.5F;
                            this.hoveredCard.target_x = (float)Settings.WIDTH / 2.0F;
                            this.isDraggingCard = false;
                            return true;
                        }

                        if (this.clickDragTimer < 0.4F) {
                            this.isUsingClickDragControl = true;
                            return true;
                        }

                        if (AbstractDungeon.actionManager.currentAction == null) {
                            this.releaseCard();
                            CardCrawlGame.sound.play("CARD_OBTAIN");
                            return true;
                        }
                    }

                    return false;
                } else if (!InputHelper.justClickedRight && !simulateRightClickDrop) {
                    if (Settings.isTouchScreen && !Settings.isControllerMode) {
                        this.hoveredCard.target_x = (float)InputHelper.mX;
                        this.hoveredCard.target_y = (float)InputHelper.mY + 270.0F * Settings.scale;
                    } else {
                        this.hoveredCard.target_x = (float)InputHelper.mX;
                        this.hoveredCard.target_y = (float)InputHelper.mY;
                    }

                    if (!this.hoveredCard.hasEnoughEnergy() && this.isHoveringDropZone) {
                        AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, this.hoveredCard.cantUseMessage, true));
                        this.energyTip(this.hoveredCard);
                        this.releaseCard();
                        CardCrawlGame.sound.play("CARD_REJECT");
                        return true;
                    } else {
                        if (this.isHoveringDropZone && (this.hoveredCard.target == CardTarget.ENEMY || this.hoveredCard.target == CardTarget.SELF_AND_ENEMY)) {
                            this.inSingleTargetMode = true;
                            this.arrowX = (float)InputHelper.mX;
                            this.arrowY = (float)InputHelper.mY;
                            GameCursor.hidden = true;
                            this.hoveredCard.untip();
                            this.hand.refreshHandLayout();
                            if (Settings.isTouchScreen && !Settings.isControllerMode) {
                                this.hoveredCard.target_y = 260.0F * Settings.scale;
                                this.hoveredCard.target_x = (float)Settings.WIDTH / 2.0F;
                                this.hoveredCard.targetDrawScale = 1.0F;
                            } else {
                                this.hoveredCard.target_y = AbstractCard.IMG_HEIGHT * 0.75F / 2.5F;
                                this.hoveredCard.target_x = (float)Settings.WIDTH / 2.0F;
                            }

                            this.isDraggingCard = false;
                        }

                        return true;
                    }
                } else {
                    CardCrawlGame.sound.play("UI_CLICK_2");
                    this.releaseCard();
                    return true;
                }
            }
        }
    }

    private void manuallySelectCard(AbstractCard card) {
        this.hoveredCard = card;
        this.hoveredCard.setAngle(0.0F, false);
        this.isUsingClickDragControl = true;
        this.isDraggingCard = true;
        this.hoveredCard.flash(Color.SKY.cpy());
        if (this.hoveredCard.showEvokeValue) {
            if (this.hoveredCard.showEvokeOrbCount == 0) {
                for(AbstractOrb o : this.orbs) {
                    o.showEvokeValue();
                }
            } else {
                int tmpShowCount = this.hoveredCard.showEvokeOrbCount;
                int emptyCount = 0;

                for(AbstractOrb o : this.orbs) {
                    if (o instanceof EmptyOrbSlot) {
                        ++emptyCount;
                    }
                }

                tmpShowCount -= emptyCount;
                if (tmpShowCount > 0) {
                    for(AbstractOrb o : this.orbs) {
                        o.showEvokeValue();
                        --tmpShowCount;
                        if (tmpShowCount <= 0) {
                            break;
                        }
                    }
                }
            }
        }

    }

    private void playCard() {
        InputHelper.justClickedLeft = false;
        this.hoverEnemyWaitTimer = 1.0F;
        this.hoveredCard.unhover();
        if (!this.queueContains(this.hoveredCard)) {
            if (this.hoveredCard.target != CardTarget.ENEMY && this.hoveredCard.target != CardTarget.SELF_AND_ENEMY) {
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.hoveredCard, (AbstractMonster)null));
            } else {
                if (this.hasPower("Surrounded")) {
                    this.flipHorizontal = this.hoveredMonster.drawX < this.drawX;
                }

                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.hoveredCard, this.hoveredMonster));
            }
        }

        this.isUsingClickDragControl = false;
        this.hoveredCard = null;
        this.isDraggingCard = false;
    }

    private boolean queueContains(AbstractCard card) {
        for(CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
            if (i.card == card) {
                return true;
            }
        }

        return false;
    }

    public void releaseCard() {
        for(AbstractOrb o : this.orbs) {
            o.hideEvokeValues();
        }

        this.passedHesitationLine = false;
        InputHelper.justClickedLeft = false;
        InputHelper.justReleasedClickLeft = false;
        InputHelper.isMouseDown = false;
        this.inSingleTargetMode = false;
        if (!this.isInKeyboardMode) {
            GameCursor.hidden = false;
        }

        this.isUsingClickDragControl = false;
        this.isHoveringDropZone = false;
        this.isDraggingCard = false;
        this.isHoveringCard = false;
        if (this.hoveredCard != null) {
            if (this.hoveredCard.canUse(this, (AbstractMonster)null)) {
                this.hoveredCard.beginGlowing();
            }

            this.hoveredCard.untip();
            this.hoveredCard.hoverTimer = 0.25F;
            this.hoveredCard.unhover();
        }

        this.hoveredCard = null;
        this.hand.refreshHandLayout();
        this.touchscreenInspectCount = 0;
    }

    public void onCardDrawOrDiscard() {
        for(AbstractPower p : this.powers) {
            p.onDrawOrDiscard();
        }

        for(AbstractRelic r : this.relics) {
            r.onDrawOrDiscard();
        }

        if (this.hasPower("Corruption")) {
            for(AbstractCard c : this.hand.group) {
                if (c.type == CardType.SKILL && c.costForTurn != 0) {
                    c.modifyCostForCombat(-9);
                }
            }
        }

        this.hand.applyPowers();
        this.hand.glowCheck();
    }

    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if (c.type == CardType.ATTACK) {
            this.useFastAttackAnimation();
        }

        c.calculateCardDamage(monster);
        if (c.cost == -1 && EnergyPanel.totalCount < energyOnUse && !c.ignoreEnergyOnUse) {
            c.energyOnUse = EnergyPanel.totalCount;
        }

        if (c.cost == -1 && c.isInAutoplay) {
            c.freeToPlayOnce = true;
        }

        c.use(this, monster);
        AbstractDungeon.actionManager.addToBottom(new UseCardAction(c, monster));
        if (!c.dontTriggerOnUseCard) {
            this.hand.triggerOnOtherCardPlayed(c);
        }

        this.hand.removeCard(c);
        this.cardInUse = c;
        c.target_x = (float)(Settings.WIDTH / 2);
        c.target_y = (float)(Settings.HEIGHT / 2);
        if (c.costForTurn > 0 && !c.freeToPlay() && !c.isInAutoplay && (!this.hasPower("Corruption") || c.type != CardType.SKILL)) {
            this.energy.use(c.costForTurn);
        }

        if (!this.hand.canUseAnyCard() && !this.endTurnQueued) {
            AbstractDungeon.overlayMenu.endTurnButton.isGlowing = true;
        }

    }

    public void damage(DamageInfo info) {
        
        int damageAmount = info.output;
        boolean hadBlock = true;
        if (this.currentBlock == 0) {
            hadBlock = false;
        }

        if (damageAmount < 0) {
            damageAmount = 0;
        }

        if (damageAmount > 1 && this.hasPower("IntangiblePlayer")) {
            damageAmount = 1;
        }

        damageAmount = SubscriptionManager.getInstance().triggerOnPlayerDamaged(damageAmount, info);
        damageAmount = this.decrementBlock(info, damageAmount);
        if (info.owner == this) {
            for(AbstractRelic r : this.relics) {
                damageAmount = r.onAttackToChangeDamage(info, damageAmount);
            }
        }

        if (info.owner != null) {
            for(AbstractPower p : info.owner.powers) {
                damageAmount = p.onAttackToChangeDamage(info, damageAmount);
            }
        }

        for(AbstractRelic r : this.relics) {
            damageAmount = r.onAttackedToChangeDamage(info, damageAmount);
        }

        for(AbstractPower p : this.powers) {
            damageAmount = p.onAttackedToChangeDamage(info, damageAmount);
        }

        if (info.owner == this) {
            for(AbstractRelic r : this.relics) {
                r.onAttack(info, damageAmount, this);
            }
        }

        if (info.owner != null) {
            for(AbstractPower p : info.owner.powers) {
                p.onAttack(info, damageAmount, this);
            }

            for(AbstractPower p : this.powers) {
                damageAmount = p.onAttacked(info, damageAmount);
            }

            for(AbstractRelic r : this.relics) {
                damageAmount = r.onAttacked(info, damageAmount);
            }
        } else {
            logger.info("NO OWNER, DON'T TRIGGER POWERS");
        }

        for(AbstractRelic r : this.relics) {
            damageAmount = r.onLoseHpLast(damageAmount);
        }

        this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);
        if (damageAmount > 0) {
            for(AbstractPower p : this.powers) {
                damageAmount = p.onLoseHp(damageAmount);
            }

            for(AbstractRelic r : this.relics) {
                r.onLoseHp(damageAmount);
            }

            for(AbstractPower p : this.powers) {
                p.wasHPLost(info, damageAmount);
            }

            for(AbstractRelic r : this.relics) {
                r.wasHPLost(damageAmount);
            }

            if (info.owner != null) {
                for(AbstractPower p : info.owner.powers) {
                    p.onInflictDamage(info, damageAmount, this);
                }
            }

            if (info.owner != this) {
                this.useStaggerAnimation();
            }

            if (info.type == DamageType.HP_LOSS) {
                GameActionManager.hpLossThisCombat += damageAmount;
            }

            GameActionManager.damageReceivedThisTurn += damageAmount;
            GameActionManager.damageReceivedThisCombat += damageAmount;
            this.currentHealth -= damageAmount;
            if (damageAmount > 0 && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
                this.updateCardsOnDamage();
                ++this.damagedThisCombat;
            }

            AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
            if (this.currentHealth < 0) {
                this.currentHealth = 0;
            } else if (this.currentHealth < this.maxHealth / 4) {
                AbstractDungeon.topLevelEffects.add(new BorderFlashEffect(new Color(1.0F, 0.1F, 0.05F, 0.0F)));
            }

            this.healthBarUpdatedEvent();
            if ((float)this.currentHealth <= (float)this.maxHealth / 2.0F && !this.isBloodied) {
                this.isBloodied = true;

                for(AbstractRelic r : this.relics) {
                    if (r != null) {
                        r.onBloodied();
                    }
                }
            }

            if (this.currentHealth < 1) {
                if (!this.hasRelic("Mark of the Bloom")) {
                    if (this.hasPotion("FairyPotion")) {
                        for(AbstractPotion p : this.potions) {
                            if (p.ID.equals("FairyPotion")) {
                                p.flash();
                                this.currentHealth = 0;
                                p.use(this);
                                AbstractDungeon.topPanel.destroyPotion(p.slot);
                                return;
                            }
                        }
                    } else if (this.hasRelic("Lizard Tail") && ((LizardTail)this.getRelic("Lizard Tail")).counter == -1) {
                        this.currentHealth = 0;
                        this.getRelic("Lizard Tail").onTrigger();
                        return;
                    }
                }

                this.isDead = true;
                AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
                this.currentHealth = 0;
                if (this.currentBlock > 0) {
                    this.loseBlock();
                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
                }
            }
        } else if (this.currentBlock > 0) {
            AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, uiStrings.TEXT[0]));
        } else if (hadBlock) {
            AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, uiStrings.TEXT[0]));
            AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
        } else {
            AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
        }

    }

    private void updateCardsOnDamage() {
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT) {
            for(AbstractCard c : this.hand.group) {
                c.tookDamage();
            }

            for(AbstractCard c : this.discardPile.group) {
                c.tookDamage();
            }

            for(AbstractCard c : this.drawPile.group) {
                c.tookDamage();
            }
        }

    }

    public void updateCardsOnDiscard() {
        for(AbstractCard c : this.hand.group) {
            c.didDiscard();
        }

        for(AbstractCard c : this.discardPile.group) {
            c.didDiscard();
        }

        for(AbstractCard c : this.drawPile.group) {
            c.didDiscard();
        }

    }

    public void heal(int healAmount) {
        super.heal(healAmount);
        if ((float)this.currentHealth > (float)this.maxHealth / 2.0F && this.isBloodied) {
            this.isBloodied = false;

            for(AbstractRelic r : this.relics) {
                r.onNotBloodied();
            }
        }

    }

    public void gainEnergy(int e) {
        EnergyPanel.addEnergy(e);
        this.hand.glowCheck();
    }

    public void loseEnergy(int e) {
        EnergyPanel.useEnergy(e);
    }

    public void preBattlePrep() {
        if (!(Boolean)TipTracker.tips.get("COMBAT_TIP")) {
            AbstractDungeon.ftue = new MultiPageFtue();
            TipTracker.neverShowAgain("COMBAT_TIP");
        }

        AbstractDungeon.actionManager.clear();
        this.damagedThisCombat = 0;
        this.cardsPlayedThisTurn = 0;
        this.maxOrbs = 0;
        this.orbs.clear();
        this.increaseMaxOrbSlots(this.masterMaxOrbs, false);
        this.isBloodied = this.currentHealth <= this.maxHealth / 2;
        poisonKillCount = 0;
        GameActionManager.playerHpLastTurn = this.currentHealth;
        this.endTurnQueued = false;
        this.gameHandSize = this.masterHandSize;
        this.isDraggingCard = false;
        this.isHoveringDropZone = false;
        this.hoveredCard = null;
        this.cardInUse = null;
        this.drawPile.initializeDeck(this.masterDeck);
        AbstractDungeon.overlayMenu.endTurnButton.enabled = false;
        this.hand.clear();
        this.discardPile.clear();
        this.exhaustPile.clear();
        if (AbstractDungeon.player.hasRelic("SlaversCollar")) {
            ((SlaversCollar)AbstractDungeon.player.getRelic("SlaversCollar")).beforeEnergyPrep();
        }

        this.energy.prep();
        this.powers.clear();
        this.isEndingTurn = false;
        this.healthBarUpdatedEvent();
        if (ModHelper.isModEnabled("Lethality")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 3), 3));
        }

        if (ModHelper.isModEnabled("Terminal")) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 5), 5));
        }

        AbstractDungeon.getCurrRoom().monsters.usePreBattleAction();
        if (Settings.isFinalActAvailable && AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
            AbstractDungeon.getCurrRoom().applyEmeraldEliteBuff();
        }

        AbstractDungeon.actionManager.addToTop(new WaitAction(1.0F));
        this.applyPreCombatLogic();
    }

    public ArrayList<String> getRelicNames() {
        ArrayList<String> arr = new ArrayList();

        for(AbstractRelic relic : this.relics) {
            arr.add(relic.relicId);
        }

        return arr;
    }

    public int getCircletCount() {
        int count = 0;
        int counterSum = 0;

        for(AbstractRelic relic : this.relics) {
            if (relic.relicId.equals("Circlet")) {
                ++count;
                counterSum += relic.counter;
            }
        }

        if (counterSum > 0) {
            return counterSum;
        } else {
            return count;
        }
    }

    public void draw(int numCards) {
        for(int i = 0; i < numCards; ++i) {
            if (this.drawPile.isEmpty()) {
                logger.info("ERROR: How did this happen? No cards in draw pile?? Player.java");
            } else {
                AbstractCard c = this.drawPile.getTopCard();
                c.current_x = CardGroup.DRAW_PILE_X;
                c.current_y = CardGroup.DRAW_PILE_Y;
                c.setAngle(0.0F, true);
                c.lighten(false);
                c.drawScale = 0.12F;
                c.targetDrawScale = 0.75F;
                c.triggerWhenDrawn();
                this.hand.addToHand(c);
                this.drawPile.removeTopCard();

                for(AbstractPower p : this.powers) {
                    p.onCardDraw(c);
                }

                for(AbstractRelic r : this.relics) {
                    r.onCardDraw(c);
                }
            }
        }

    }

    public void draw() {
        if (this.hand.size() == 10) {
            this.createHandIsFullDialog();
        } else {
            CardCrawlGame.sound.playAV("CARD_DRAW_8", -0.12F, 0.25F);
            this.draw(1);
            this.onCardDrawOrDiscard();
        }
    }

    public void render(SpriteBatch sb) {
        this.stance.render(sb);
        if ((AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT || AbstractDungeon.getCurrRoom() instanceof MonsterRoom) && !this.isDead) {
            this.renderHealth(sb);
            if (!this.orbs.isEmpty()) {
                for(AbstractOrb o : this.orbs) {
                    o.render(sb);
                }
            }
        }

        if (!(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            if (this.atlas != null && !this.renderCorpse) {
                this.renderPlayerImage(sb);
            } else {
                sb.setColor(Color.WHITE);
                sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
            }

            this.hb.render(sb);
            this.healthHb.render(sb);
        } else {
            sb.setColor(Color.WHITE);
            this.renderShoulderImg(sb);
        }

    }

    public void renderShoulderImg(SpriteBatch sb) {
        if (CampfireUI.hidden) {
            sb.draw(this.shoulder2Img, 0.0F, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
        } else {
            sb.draw(this.shoulderImg, this.animX, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
        }

    }

    public void renderPlayerImage(SpriteBatch sb) {
        if (this.atlas != null) {
            this.state.update(Gdx.graphics.getDeltaTime());
            this.state.apply(this.skeleton);
            this.skeleton.updateWorldTransform();
            this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
            this.skeleton.setColor(this.tint.color);
            this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
            sb.end();
            CardCrawlGame.psb.begin();
            sr.draw(CardCrawlGame.psb, this.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        } else {
            sb.setColor(Color.WHITE);
            sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
        }

    }

    public void renderPlayerBattleUi(SpriteBatch sb) {
        if ((this.hb.hovered || this.healthHb.hovered) && !AbstractDungeon.isScreenUp) {
            this.renderPowerTips(sb);
        }

    }

    public void renderPowerTips(SpriteBatch sb) {
        ArrayList<PowerTip> tips = new ArrayList();
        if (!this.stance.ID.equals("Neutral")) {
            tips.add(new PowerTip(this.stance.name, this.stance.description));
        }

        for(AbstractPower p : this.powers) {
            if (p.region48 != null) {
                tips.add(new PowerTip(p.name, p.description, p.region48));
            } else {
                tips.add(new PowerTip(p.name, p.description, p.img));
            }
        }

        if (!tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(tips, this.hb.cY), tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(tips, this.hb.cY), tips);
            }
        }

    }

    public void renderHand(SpriteBatch sb) {
        if (Settings.SHOW_CARD_HOTKEYS) {
            this.renderCardHotKeyText(sb);
        }

        if (this.inspectMode && this.inspectHb != null) {
            this.renderReticle(sb, this.inspectHb);
        }

        if (this.hoveredCard != null) {
            int aliveMonsters = 0;
            this.hand.renderHand(sb, this.hoveredCard);
            this.hoveredCard.renderHoverShadow(sb);
            if ((this.isDraggingCard || this.inSingleTargetMode) && this.isHoveringDropZone) {
                if (this.isDraggingCard && !this.inSingleTargetMode) {
                    AbstractMonster theMonster = null;

                    for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                        if (!m.isDying && m.currentHealth > 0) {
                            ++aliveMonsters;
                            theMonster = m;
                        }
                    }

                    if (aliveMonsters == 1 && this.hoveredMonster == null) {
                        this.hoveredCard.calculateCardDamage(theMonster);
                        this.hoveredCard.render(sb);
                        this.hoveredCard.applyPowers();
                    } else {
                        this.hoveredCard.render(sb);
                    }
                }

                if (!AbstractDungeon.getCurrRoom().isBattleEnding()) {
                    this.renderHoverReticle(sb);
                }
            }

            if (this.hoveredMonster != null) {
                this.hoveredCard.calculateCardDamage(this.hoveredMonster);
                this.hoveredCard.render(sb);
                this.hoveredCard.applyPowers();
            } else if (aliveMonsters != 1) {
                this.hoveredCard.render(sb);
            }
        } else if (AbstractDungeon.screen == CurrentScreen.HAND_SELECT) {
            this.hand.render(sb);
        } else {
            this.hand.renderHand(sb, this.cardInUse);
        }

        if (this.cardInUse != null && AbstractDungeon.screen != CurrentScreen.HAND_SELECT && !PeekButton.isPeeking) {
            this.cardInUse.render(sb);
            if (AbstractDungeon.getCurrRoom().phase != RoomPhase.COMBAT) {
                AbstractDungeon.effectList.add(new CardDisappearEffect(this.cardInUse.makeCopy(), this.cardInUse.current_x, this.cardInUse.current_y));
                this.cardInUse = null;
            }
        }

        this.limbo.render(sb);
        if (this.inSingleTargetMode && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && !AbstractDungeon.getCurrRoom().isBattleEnding()) {
            this.renderTargetingUi(sb);
        }

    }

    private void renderTargetingUi(SpriteBatch sb) {
        this.arrowX = MathHelper.mouseLerpSnap(this.arrowX, (float)InputHelper.mX);
        this.arrowY = MathHelper.mouseLerpSnap(this.arrowY, (float)InputHelper.mY);
        this.controlPoint.x = this.hoveredCard.current_x - (this.arrowX - this.hoveredCard.current_x) / 4.0F;
        this.controlPoint.y = this.arrowY + (this.arrowY - this.hoveredCard.current_y) / 2.0F;
        if (this.hoveredMonster == null) {
            this.arrowScale = Settings.scale;
            this.arrowScaleTimer = 0.0F;
            sb.setColor(Color.WHITE);
        } else {
            this.arrowScaleTimer += Gdx.graphics.getDeltaTime();
            if (this.arrowScaleTimer > 1.0F) {
                this.arrowScaleTimer = 1.0F;
            }

            this.arrowScale = Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, this.arrowScaleTimer);
            sb.setColor(ARROW_COLOR);
        }

        this.arrowTmp.x = this.controlPoint.x - this.arrowX;
        this.arrowTmp.y = this.controlPoint.y - this.arrowY;
        this.arrowTmp.nor();
        this.startArrowVector.x = this.hoveredCard.current_x;
        this.startArrowVector.y = this.hoveredCard.current_y;
        this.endArrowVector.x = this.arrowX;
        this.endArrowVector.y = this.arrowY;
        this.drawCurvedLine(sb, this.startArrowVector, this.endArrowVector, this.controlPoint);
        sb.draw(ImageMaster.TARGET_UI_ARROW, this.arrowX - 128.0F, this.arrowY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.arrowScale, this.arrowScale, this.arrowTmp.angle() + 90.0F, 0, 0, 256, 256, false, false);
    }

    private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {
        float radius = 7.0F * Settings.scale;

        for(int i = 0; i < this.points.length - 1; ++i) {
            this.points[i] = (Vector2)Bezier.quadratic(this.points[i], (float)i / 20.0F, start, control, end, this.arrowTmp);
            radius += 0.4F * Settings.scale;
            if (i != 0) {
                this.arrowTmp.x = this.points[i - 1].x - this.points[i].x;
                this.arrowTmp.y = this.points[i - 1].y - this.points[i].y;
                sb.draw(ImageMaster.TARGET_UI_CIRCLE, this.points[i].x - 64.0F, this.points[i].y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, this.arrowTmp.nor().angle() + 90.0F, 0, 0, 128, 128, false, false);
            } else {
                this.arrowTmp.x = this.controlPoint.x - this.points[i].x;
                this.arrowTmp.y = this.controlPoint.y - this.points[i].y;
                sb.draw(ImageMaster.TARGET_UI_CIRCLE, this.points[i].x - 64.0F, this.points[i].y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, this.arrowTmp.nor().angle() + 270.0F, 0, 0, 128, 128, false, false);
            }
        }

    }

    public void createHandIsFullDialog() {
        AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[2], true));
    }

    private void renderHoverReticle(SpriteBatch sb) {
        switch (this.hoveredCard.target) {
            case ENEMY:
                if (this.inSingleTargetMode && this.hoveredMonster != null) {
                    this.hoveredMonster.renderReticle(sb);
                }
                break;
            case ALL_ENEMY:
                AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
                break;
            case SELF:
                this.renderReticle(sb);
                break;
            case SELF_AND_ENEMY:
                this.renderReticle(sb);
                if (this.inSingleTargetMode && this.hoveredMonster != null) {
                    this.hoveredMonster.renderReticle(sb);
                }
                break;
            case ALL:
                this.renderReticle(sb);
                AbstractDungeon.getCurrRoom().monsters.renderReticle(sb);
            case NONE:
        }

    }

    public void applyPreCombatLogic() {
        for(AbstractRelic r : this.relics) {
            if (r != null) {
                r.atPreBattle();
            }
        }

    }

    public void applyStartOfCombatLogic() {
        for(AbstractRelic r : this.relics) {
            if (r != null) {
                r.atBattleStart();
            }
        }

        for(AbstractBlight b : this.blights) {
            if (b != null) {
                b.atBattleStart();
            }
        }

    }

    public void applyStartOfCombatPreDrawLogic() {
        for(AbstractRelic r : this.relics) {
            if (r != null) {
                r.atBattleStartPreDraw();
            }
        }

    }

    public void applyStartOfTurnRelics() {
        this.stance.atStartOfTurn();

        for(AbstractRelic r : this.relics) {
            if (r != null) {
                r.atTurnStart();
            }
        }

        for(AbstractBlight b : this.blights) {
            if (b != null) {
                b.atTurnStart();
            }
        }

    }

    public void applyStartOfTurnPostDrawRelics() {
        for(AbstractRelic r : this.relics) {
            if (r != null) {
                r.atTurnStartPostDraw();
            }
        }

    }

    public void applyStartOfTurnPreDrawCards() {
        for(AbstractCard c : this.hand.group) {
            if (c != null) {
                c.atTurnStartPreDraw();
            }
        }

    }

    public void applyStartOfTurnCards() {
        for(AbstractCard c : this.drawPile.group) {
            if (c != null) {
                c.atTurnStart();
            }
        }

        for(AbstractCard c : this.hand.group) {
            if (c != null) {
                c.atTurnStart();
            }
        }

        for(AbstractCard c : this.discardPile.group) {
            if (c != null) {
                c.atTurnStart();
            }
        }

    }

    public void onVictory() {
        if (!this.isDying) {
            for(AbstractRelic r : this.relics) {
                r.onVictory();
            }

            for(AbstractBlight b : this.blights) {
                b.onVictory();
            }

            for(AbstractPower p : this.powers) {
                p.onVictory();
            }
        }

        this.damagedThisCombat = 0;
    }

    public boolean hasRelic(String targetID) {
        for(AbstractRelic r : this.relics) {
            if (r.relicId.equals(targetID)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasBlight(String targetID) {
        for(AbstractBlight b : this.blights) {
            if (b.blightID.equals(targetID)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPotion(String id) {
        for(AbstractPotion p : this.potions) {
            if (p.ID.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyPotions() {
        for(AbstractPotion p : this.potions) {
            if (!(p instanceof PotionSlot)) {
                return true;
            }
        }

        return false;
    }

    public void loseRandomRelics(int amount) {
        if (amount > this.relics.size()) {
            for(AbstractRelic r : this.relics) {
                r.onUnequip();
            }

            this.relics.clear();
        } else {
            for(int i = 0; i < amount; ++i) {
                int index = MathUtils.random(0, this.relics.size() - 1);
                ((AbstractRelic)this.relics.get(index)).onUnequip();
                this.relics.remove(index);
            }

            this.reorganizeRelics();
        }
    }

    public boolean loseRelic(String targetID) {
        if (!this.hasRelic(targetID)) {
            return false;
        } else {
            AbstractRelic toRemove = null;

            for(AbstractRelic r : this.relics) {
                if (r.relicId.equals(targetID)) {
                    r.onUnequip();
                    toRemove = r;
                }
            }

            if (toRemove == null) {
                logger.info("WHY WAS RELIC: " + this.name + " NOT FOUND???");
                return false;
            } else {
                this.relics.remove(toRemove);
                this.reorganizeRelics();
                return true;
            }
        }
    }

    public void reorganizeRelics() {
        logger.info("Reorganizing relics");
        ArrayList<AbstractRelic> tmpRelics = new ArrayList();
        tmpRelics.addAll(this.relics);
        this.relics.clear();

        for(int i = 0; i < tmpRelics.size(); ++i) {
            ((AbstractRelic)tmpRelics.get(i)).reorganizeObtain(this, i, false, tmpRelics.size());
        }

    }

    public AbstractRelic getRelic(String targetID) {
        for(AbstractRelic r : this.relics) {
            if (r.relicId.equals(targetID)) {
                return r;
            }
        }

        return null;
    }

    public AbstractBlight getBlight(String targetID) {
        for(AbstractBlight b : this.blights) {
            if (b.blightID.equals(targetID)) {
                return b;
            }
        }

        return null;
    }

    public void obtainPotion(int slot, AbstractPotion potionToObtain) {
        if (slot <= this.potionSlots) {
            this.potions.set(slot, potionToObtain);
            potionToObtain.setAsObtained(slot);
        }
    }

    public boolean obtainPotion(AbstractPotion potionToObtain) {
        int index = 0;

        for(AbstractPotion p : this.potions) {
            if (p instanceof PotionSlot) {
                break;
            }

            ++index;
        }

        if (index < this.potionSlots) {
            this.potions.set(index, potionToObtain);
            potionToObtain.setAsObtained(index);
            potionToObtain.flash();
            AbstractPotion.playPotionSound();
            return true;
        } else {
            logger.info("NOT ENOUGH POTION SLOTS");
            AbstractDungeon.topPanel.flashRed();
            return false;
        }
    }

    public void renderRelics(SpriteBatch sb) {
        for(int i = 0; i < this.relics.size(); ++i) {
            if (i / AbstractRelic.MAX_RELICS_PER_PAGE == AbstractRelic.relicPage) {
                ((AbstractRelic)this.relics.get(i)).renderInTopPanel(sb);
            }
        }

        for(AbstractRelic r : this.relics) {
            if (r.hb.hovered) {
                r.renderTip(sb);
            }
        }

    }

    public void renderBlights(SpriteBatch sb) {
        for(AbstractBlight b : this.blights) {
            b.renderInTopPanel(sb);
        }

        for(AbstractBlight b : this.blights) {
            if (b.hb.hovered) {
                b.renderTip(sb);
            }
        }

    }

    public void bottledCardUpgradeCheck(AbstractCard c) {
        if (c.inBottleFlame && this.hasRelic("Bottled Flame")) {
            ((BottledFlame)this.getRelic("Bottled Flame")).setDescriptionAfterLoading();
        }

        if (c.inBottleLightning && this.hasRelic("Bottled Lightning")) {
            ((BottledLightning)this.getRelic("Bottled Lightning")).setDescriptionAfterLoading();
        }

        if (c.inBottleTornado && this.hasRelic("Bottled Tornado")) {
            ((BottledTornado)this.getRelic("Bottled Tornado")).setDescriptionAfterLoading();
        }

    }

    public void triggerEvokeAnimation(int slot) {
        if (this.maxOrbs > 0) {
            ((AbstractOrb)this.orbs.get(slot)).triggerEvokeAnimation();
        }
    }

    public void evokeOrb() {
        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)this.orbs.get(0)).onEvoke();
            AbstractOrb orbSlot = new EmptyOrbSlot();

            for(int i = 1; i < this.orbs.size(); ++i) {
                Collections.swap(this.orbs, i, i - 1);
            }

            this.orbs.set(this.orbs.size() - 1, orbSlot);

            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }
        }

    }

    public void evokeNewestOrb() {
        if (!this.orbs.isEmpty() && !(this.orbs.get(this.orbs.size() - 1) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)this.orbs.get(this.orbs.size() - 1)).onEvoke();
        }

    }

    public void evokeWithoutLosingOrb() {
        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
            ((AbstractOrb)this.orbs.get(0)).onEvoke();
        }

    }

    public void removeNextOrb() {
        if (!this.orbs.isEmpty() && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
            AbstractOrb orbSlot = new EmptyOrbSlot(((AbstractOrb)this.orbs.get(0)).cX, ((AbstractOrb)this.orbs.get(0)).cY);

            for(int i = 1; i < this.orbs.size(); ++i) {
                Collections.swap(this.orbs, i, i - 1);
            }

            this.orbs.set(this.orbs.size() - 1, orbSlot);

            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }
        }

    }

    public boolean hasEmptyOrb() {
        if (this.orbs.isEmpty()) {
            return false;
        } else {
            for(AbstractOrb o : this.orbs) {
                if (o instanceof EmptyOrbSlot) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean hasOrb() {
        if (this.orbs.isEmpty()) {
            return false;
        } else {
            return !(this.orbs.get(0) instanceof EmptyOrbSlot);
        }
    }

    public int filledOrbCount() {
        int orbCount = 0;

        for(AbstractOrb o : this.orbs) {
            if (!(o instanceof EmptyOrbSlot)) {
                ++orbCount;
            }
        }

        return orbCount;
    }

    public void channelOrb(AbstractOrb orbToSet) {
        if (this.maxOrbs <= 0) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[4], true));
        } else {
            if (this.maxOrbs > 0) {
                if (this.hasRelic("Dark Core") && !(orbToSet instanceof Dark)) {
                    orbToSet = new Dark();
                }

                int index = -1;

                for(int i = 0; i < this.orbs.size(); ++i) {
                    if (this.orbs.get(i) instanceof EmptyOrbSlot) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    orbToSet.cX = ((AbstractOrb)this.orbs.get(index)).cX;
                    orbToSet.cY = ((AbstractOrb)this.orbs.get(index)).cY;
                    this.orbs.set(index, orbToSet);
                    ((AbstractOrb)this.orbs.get(index)).setSlot(index, this.maxOrbs);
                    orbToSet.playChannelSFX();

                    for(AbstractPower p : this.powers) {
                        p.onChannel(orbToSet);
                    }

                    AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orbToSet);
                    AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orbToSet);
                    int plasmaCount = 0;

                    for(AbstractOrb o : AbstractDungeon.actionManager.orbsChanneledThisTurn) {
                        if (o instanceof Plasma) {
                            ++plasmaCount;
                        }
                    }

                    if (plasmaCount == 9) {
                        UnlockTracker.unlockAchievement("NEON");
                    }

                    orbToSet.applyFocus();
                } else {
                    AbstractDungeon.actionManager.addToTop(new ChannelAction(orbToSet));
                    AbstractDungeon.actionManager.addToTop(new EvokeOrbAction(1));
                    AbstractDungeon.actionManager.addToTop(new AnimateOrbAction(1));
                }
            }

        }
    }

    public void increaseMaxOrbSlots(int amount, boolean playSfx) {
        if (this.maxOrbs == 10) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, MSG[3], true));
        } else {
            if (playSfx) {
                CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
            }

            this.maxOrbs += amount;

            for(int i = 0; i < amount; ++i) {
                this.orbs.add(new EmptyOrbSlot());
            }

            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }

        }
    }

    public void decreaseMaxOrbSlots(int amount) {
        if (this.maxOrbs > 0) {
            this.maxOrbs -= amount;
            if (this.maxOrbs < 0) {
                this.maxOrbs = 0;
            }

            if (!this.orbs.isEmpty()) {
                this.orbs.remove(this.orbs.size() - 1);
            }

            for(int i = 0; i < this.orbs.size(); ++i) {
                ((AbstractOrb)this.orbs.get(i)).setSlot(i, this.maxOrbs);
            }

        }
    }

    public void applyStartOfTurnOrbs() {
        if (!this.orbs.isEmpty()) {
            for(AbstractOrb o : this.orbs) {
                o.onStartOfTurn();
            }

            if (this.hasRelic("Cables") && !(this.orbs.get(0) instanceof EmptyOrbSlot)) {
                ((AbstractOrb)this.orbs.get(0)).onStartOfTurn();
            }
        }

    }

    private void updateEscapeAnimation() {
        if (this.escapeTimer != 0.0F) {
            this.escapeTimer -= Gdx.graphics.getDeltaTime();
            if (this.flipHorizontal) {
                this.drawX -= Gdx.graphics.getDeltaTime() * 400.0F * Settings.scale;
            } else {
                this.drawX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
            }
        }

        if (this.escapeTimer < 0.0F) {
            AbstractDungeon.getCurrRoom().endBattle();
            this.flipHorizontal = false;
            this.isEscaping = false;
            this.escapeTimer = 0.0F;
        }

    }

    public boolean relicsDoneAnimating() {
        for(AbstractRelic r : this.relics) {
            if (!r.isDone) {
                return false;
            }
        }

        return true;
    }

    public void resetControllerValues() {
        if (Settings.isControllerMode) {
            this.toHover = null;
            this.hoveredCard = null;
            this.inspectMode = false;
            this.inspectHb = null;
            this.keyboardCardIndex = -1;
            this.hand.refreshHandLayout();
        }

    }

    public AbstractPotion getRandomPotion() {
        ArrayList<AbstractPotion> list = new ArrayList();

        for(AbstractPotion p : this.potions) {
            if (!(p instanceof PotionSlot)) {
                list.add(p);
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));
            return (AbstractPotion)list.get(0);
        }
    }

    public void removePotion(AbstractPotion potionOption) {
        int slot = this.potions.indexOf(potionOption);
        if (slot >= 0) {
            this.potions.set(slot, new PotionSlot(slot));
        }

    }

    public void movePosition(float x, float y) {
        this.drawX = x;
        this.drawY = y;
        this.dialogX = this.drawX + 0.0F * Settings.scale;
        this.dialogY = this.drawY + 170.0F * Settings.scale;
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.refreshHitboxLocation();
    }

    public void switchedStance() {
        for(AbstractCard c : this.hand.group) {
            c.switchedStance();
        }

        for(AbstractCard c : this.discardPile.group) {
            c.switchedStance();
        }

        for(AbstractCard c : this.drawPile.group) {
            c.switchedStance();
        }

    }

    public CharacterOption getCharacterSelectOption() {
        return null;
    }

    public void onStanceChange(String id) {
    }

    static {
        tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Player Tips");
        MSG = tutorialStrings.TEXT;
        LABEL = tutorialStrings.LABEL;
        poisonKillCount = 0;
        customMods = null;
        ARROW_COLOR = new Color(1.0F, 0.2F, 0.3F, 1.0F);
        HOVER_CARD_Y_POSITION = 210.0F * Settings.scale;
        uiStrings = CardCrawlGame.languagePack.getUIString("AbstractPlayer");
    }

    private static enum RHoverType {
        RELIC,
        BLIGHT;

        private RHoverType() {
        }
    }

    public static enum PlayerClass {
        IRONCLAD,
        THE_SILENT,
        DEFECT,
        WATCHER;

        private PlayerClass() {
        }
    }
}
