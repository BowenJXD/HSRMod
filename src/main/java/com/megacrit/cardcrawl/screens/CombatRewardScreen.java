//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.megacrit.cardcrawl.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardItem.RewardType;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.utils.RewardEditor;

import java.util.ArrayList;
import java.util.Iterator;

public class CombatRewardScreen {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public ArrayList<RewardItem> rewards = new ArrayList();
    public ArrayList<AbstractGameEffect> effects = new ArrayList();
    public boolean hasTakenAll = false;
    private String labelOverride = null;
    private boolean mug = false;
    private boolean smoke = false;
    private static final float REWARD_ANIM_TIME = 0.2F;
    private float rewardAnimTimer = 0.2F;
    private float tipY;
    private Color uiColor;
    private static final int SHEET_W = 612;
    private static final int SHEET_H = 716;
    private String tip;

    public CombatRewardScreen() {
        this.tipY = -100.0F * Settings.scale;
        this.uiColor = Color.BLACK.cpy();
    }

    public void update() {
        if (InputHelper.justClickedLeft && Settings.isDebug) {
            this.tip = CardCrawlGame.tips.getTip();
        }

        this.rewardViewUpdate();
        this.updateEffects();
    }

    private void updateEffects() {
        Iterator<AbstractGameEffect> i = this.effects.iterator();

        while(i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

    }

    public void setupItemReward() {
        this.rewardAnimTimer = 0.2F;
        InputHelper.justClickedLeft = false;
        this.rewards = new ArrayList(AbstractDungeon.getCurrRoom().rewards);
        if ((AbstractDungeon.getCurrRoom().event == null || AbstractDungeon.getCurrRoom().event != null && !AbstractDungeon.getCurrRoom().event.noCardsInRewards) && !(AbstractDungeon.getCurrRoom() instanceof TreasureRoom) && !(AbstractDungeon.getCurrRoom() instanceof RestRoom)) {
            if (ModHelper.isModEnabled("Vintage") && AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                    RewardItem cardReward = new RewardItem();
                    //
                    RewardEditor.getInstance().setRewardByPath(cardReward);
                    //
                    if (cardReward.cards.size() > 0) {
                        this.rewards.add(cardReward);
                    }
                }
            } else {
                RewardItem cardReward = new RewardItem();
                //
                System.out.println("CombatReward before change: " + RewardEditor.riToString(cardReward));
                RewardEditor.getInstance().setRewardByPath(cardReward);
                System.out.println("CombatReward after change: " + RewardEditor.riToString(cardReward));
                //
                if (cardReward.cards.size() > 0) {
                    this.rewards.add(cardReward);
                }

                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom && AbstractDungeon.player.hasRelic("Prayer Wheel") && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
                    cardReward = new RewardItem();
                    //
                    RewardEditor.getInstance().setRewardByPath(cardReward);
                    //
                    if (cardReward.cards.size() > 0) {
                        this.rewards.add(cardReward);
                    }
                }
            }
        }

        AbstractDungeon.overlayMenu.proceedButton.show();
        this.hasTakenAll = false;
        this.positionRewards();
        
        //
        RewardEditor.getInstance().triggerExtraRewards(this.rewards);
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            String relicId = RewardEditor.getInstance().checkBossRelic();
            System.out.println("CombatReward relicId: " + relicId);
            if (relicId != null) {
                AbstractRelic relic = RelicLibrary.getRelic(relicId).makeCopy();
                System.out.println("CombatReward relic: " + relic);
                rewards.add(new RewardItem(relic));
            }
        }
    }

    public void positionRewards() {
        for(int i = 0; i < this.rewards.size(); ++i) {
            ((RewardItem)this.rewards.get(i)).move((float)Settings.HEIGHT / 2.0F + 124.0F * Settings.scale - (float)i * 100.0F * Settings.scale);
        }

        if (this.rewards.isEmpty()) {
            this.hasTakenAll = true;
        }

    }

    private void rewardViewUpdate() {
        if (this.rewardAnimTimer != 0.0F) {
            this.rewardAnimTimer -= Gdx.graphics.getDeltaTime();
            if (this.rewardAnimTimer < 0.0F) {
                this.rewardAnimTimer = 0.0F;
            }

            this.uiColor.r = 1.0F - this.rewardAnimTimer / 0.2F;
            this.uiColor.g = 1.0F - this.rewardAnimTimer / 0.2F;
            this.uiColor.b = 1.0F - this.rewardAnimTimer / 0.2F;
        }

        this.tipY = MathHelper.uiLerpSnap(this.tipY, (float)Settings.HEIGHT / 2.0F - 460.0F * Settings.scale);
        this.updateControllerInput();
        boolean removedSomething = false;
        Iterator<RewardItem> i = this.rewards.iterator();

        while(i.hasNext()) {
            RewardItem r = (RewardItem)i.next();
            r.update();
            if (r.isDone) {
                if (r.claimReward()) {
                    i.remove();
                    removedSomething = true;
                } else if (r.type == RewardType.POTION) {
                    r.isDone = false;
                    AbstractDungeon.topPanel.flashRed();
                    this.tip = CardCrawlGame.tips.getPotionTip();
                } else {
                    r.isDone = false;
                }
            }
        }

        if (removedSomething) {
            this.positionRewards();
            this.setLabel();
        }

    }

    private void updateControllerInput() {
        if (Settings.isControllerMode && !this.rewards.isEmpty() && !AbstractDungeon.topPanel.selectPotionMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.player.viewingRelics) {
            int index = 0;
            boolean anyHovered = false;

            for(RewardItem r : this.rewards) {
                if (r.hb.hovered) {
                    anyHovered = true;
                    break;
                }

                ++index;
            }

            if (!anyHovered) {
                index = 0;
                Gdx.input.setCursorPosition((int)((RewardItem)this.rewards.get(index)).hb.cX, Settings.HEIGHT - (int)((RewardItem)this.rewards.get(index)).hb.cY);
            } else if (!CInputActionSet.up.isJustPressed() && !CInputActionSet.altUp.isJustPressed()) {
                if (CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) {
                    ++index;
                    if (index > this.rewards.size() - 1) {
                        index = 0;
                    }

                    Gdx.input.setCursorPosition((int)((RewardItem)this.rewards.get(index)).hb.cX, Settings.HEIGHT - (int)((RewardItem)this.rewards.get(index)).hb.cY);
                }
            } else {
                --index;
                if (index < 0) {
                    index = this.rewards.size() - 1;
                }

                Gdx.input.setCursorPosition((int)((RewardItem)this.rewards.get(index)).hb.cX, Settings.HEIGHT - (int)((RewardItem)this.rewards.get(index)).hb.cY);
            }

        }
    }

    private void setLabel() {
        if (this.rewards.size() == 0) {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[0]);
        } else if (this.rewards.size() == 1) {
            switch (((RewardItem)this.rewards.get(0)).type) {
                case CARD:
                    AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[1]);
                    break;
                case GOLD:
                    AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[2]);
                    break;
                case POTION:
                    AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[3]);
                    break;
                case RELIC:
                    AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[4]);
            }
        } else {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[5]);
        }

    }

    public void render(SpriteBatch sb) {
        this.renderItemReward(sb);
        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, TEXT[12] + this.tip, (float)Settings.WIDTH / 2.0F, this.tipY, Color.LIGHT_GRAY);

        for(AbstractGameEffect e : this.effects) {
            e.render(sb);
        }

    }

    private void renderItemReward(SpriteBatch sb) {
        AbstractDungeon.overlayMenu.proceedButton.render(sb);
        sb.setColor(this.uiColor);
        sb.draw(ImageMaster.REWARD_SCREEN_SHEET, (float)Settings.WIDTH / 2.0F - 306.0F, (float)Settings.HEIGHT / 2.0F - 46.0F * Settings.scale - 358.0F, 306.0F, 358.0F, 612.0F, 716.0F, Settings.xScale, Settings.scale, 0.0F, 0, 0, 612, 716, false, false);

        for(RewardItem i : this.rewards) {
            i.render(sb);
        }

    }

    public void reopen() {
        AbstractDungeon.getCurrRoom().rewardTime = true;
        this.rewardAnimTimer = 0.2F;
        AbstractDungeon.screen = CurrentScreen.COMBAT_REWARD;
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        if (this.labelOverride != null && !this.mug && !this.smoke) {
            AbstractDungeon.dynamicBanner.appear(this.labelOverride);
            AbstractDungeon.overlayMenu.cancelButton.show(TEXT[6]);
        } else {
            if (!this.mug && !this.smoke) {
                AbstractDungeon.dynamicBanner.appear(this.getRandomBannerLabel());
            } else {
                AbstractDungeon.dynamicBanner.appear(this.labelOverride);
            }

            AbstractDungeon.overlayMenu.proceedButton.show();
        }

        this.setLabel();
    }

    public void open(String setLabel) {
        AbstractDungeon.getCurrRoom().rewardTime = true;
        this.labelOverride = setLabel;
        this.mug = false;
        this.smoke = false;
        this.tip = CardCrawlGame.tips.getTip();
        this.tipY = (float)Settings.HEIGHT - 1110.0F * Settings.scale;
        this.rewardAnimTimer = 0.5F;
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = CurrentScreen.COMBAT_REWARD;
        AbstractDungeon.dynamicBanner.appear(setLabel);
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.setupItemReward();
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[6]);

        for(RewardItem r : this.rewards) {
            if (r.type == RewardType.RELIC) {
                UnlockTracker.markRelicAsSeen(r.relic.relicId);
            }
        }

    }

    public void openCombat(String setLabel) {
        this.openCombat(setLabel, false);
    }

    public void openCombat(String setLabel, boolean smoked) {
        AbstractDungeon.getCurrRoom().rewardTime = true;
        this.labelOverride = setLabel;
        this.mug = !smoked;
        this.smoke = smoked;
        this.tip = CardCrawlGame.tips.getTip();
        this.tipY = (float)Settings.HEIGHT - 1110.0F * Settings.scale;
        this.rewardAnimTimer = 0.5F;
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = CurrentScreen.COMBAT_REWARD;
        AbstractDungeon.dynamicBanner.appear(setLabel);
        AbstractDungeon.overlayMenu.showBlackScreen();
        if (!this.smoke) {
            this.setupItemReward();

            for(RewardItem r : this.rewards) {
                if (r.type == RewardType.RELIC) {
                    UnlockTracker.markRelicAsSeen(r.relic.relicId);
                }
            }
        } else {
            AbstractDungeon.overlayMenu.proceedButton.show();
        }

        this.setLabel();
    }

    public void open() {
        AbstractDungeon.getCurrRoom().rewardTime = true;
        this.tip = CardCrawlGame.tips.getTip();
        this.mug = false;
        this.smoke = false;
        this.tipY = (float)Settings.HEIGHT - 1110.0F * Settings.scale;
        this.rewardAnimTimer = 0.5F;
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = CurrentScreen.COMBAT_REWARD;
        AbstractDungeon.dynamicBanner.appear(this.getRandomBannerLabel());
        this.labelOverride = null;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.showBlackScreen();
        this.setupItemReward();
        this.setLabel();

        for(RewardItem r : this.rewards) {
            if (r.type == RewardType.RELIC) {
                UnlockTracker.markRelicAsSeen(r.relic.relicId);
            }
        }

    }

    private String getRandomBannerLabel() {
        ArrayList<String> list = new ArrayList();
        if (AbstractDungeon.getCurrRoom() instanceof TreasureRoom) {
            list.add(TEXT[7]);
            list.add(TEXT[8]);
        } else {
            list.add(TEXT[9]);
            list.add(TEXT[10]);
            list.add(TEXT[11]);
        }

        return (String)list.get(MathUtils.random(list.size() - 1));
    }

    public void clear() {
        this.rewards.clear();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("CombatRewardScreen");
        TEXT = uiStrings.TEXT;
    }
}
