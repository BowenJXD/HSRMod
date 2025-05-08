package com.megacrit.cardcrawl.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.TintEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import hsrmod.subscribers.SubscriptionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class AbstractCreature {
    private static final Logger logger = LogManager.getLogger(AbstractCreature.class.getName());
    public String name;
    public String id;
    public ArrayList<AbstractPower> powers = new ArrayList();
    public boolean isPlayer;
    public boolean isBloodied;
    public float drawX;
    public float drawY;
    public float dialogX;
    public float dialogY;
    public Hitbox hb;
    public int gold;
    public int displayGold;
    public boolean isDying = false;
    public boolean isDead = false;
    public boolean halfDead = false;
    public boolean flipHorizontal = false;
    public boolean flipVertical = false;
    public float escapeTimer = 0.0F;
    public boolean isEscaping = false;
    protected static final float TIP_X_THRESHOLD;
    protected static final float MULTI_TIP_Y_OFFSET;
    protected static final float TIP_OFFSET_R_X;
    protected static final float TIP_OFFSET_L_X;
    protected static final float TIP_OFFSET_Y;
    protected ArrayList<PowerTip> tips = new ArrayList();
    private static UIStrings uiStrings;
    public static final String[] TEXT;
    public Hitbox healthHb;
    private float healthHideTimer = 0.0F;
    public int lastDamageTaken = 0;
    public float hb_x;
    public float hb_y;
    public float hb_w;
    public float hb_h;
    public int currentHealth;
    public int maxHealth;
    public int currentBlock;
    private float healthBarWidth;
    private float targetHealthBarWidth;
    private float hbShowTimer = 0.0F;
    private float healthBarAnimTimer = 0.0F;
    private float blockAnimTimer = 0.0F;
    private float blockOffset = 0.0F;
    private float blockScale = 1.0F;
    public float hbAlpha = 0.0F;
    private float hbYOffset;
    private Color hbBgColor;
    private Color hbShadowColor;
    private Color blockColor;
    private Color blockOutlineColor;
    private Color blockTextColor;
    private Color redHbBarColor;
    private Color greenHbBarColor;
    private Color blueHbBarColor;
    private Color orangeHbBarColor;
    private Color hbTextColor;
    private static final float BLOCK_ANIM_TIME = 0.7F;
    private static final float BLOCK_OFFSET_DIST;
    private static final float SHOW_HB_TIME = 0.7F;
    private static final float HB_Y_OFFSET_DIST;
    protected static final float BLOCK_ICON_X;
    protected static final float BLOCK_ICON_Y;
    private static final int BLOCK_W = 64;
    private static final float HEALTH_BAR_PAUSE_DURATION = 1.2F;
    private static final float HEALTH_BAR_HEIGHT;
    private static final float HEALTH_BAR_OFFSET_Y;
    private static final float HEALTH_TEXT_OFFSET_Y;
    private static final float POWER_ICON_PADDING_X;
    private static final float HEALTH_BG_OFFSET_X;
    public TintEffect tint;
    public static SkeletonMeshRenderer sr;
    private boolean shakeToggle;
    private static final float SHAKE_THRESHOLD;
    private static final float SHAKE_SPEED;
    public float animX;
    public float animY;
    protected float vX;
    protected float vY;
    protected CreatureAnimation animation;
    protected float animationTimer;
    protected static final float SLOW_ATTACK_ANIM_DUR = 1.0F;
    protected static final float STAGGER_ANIM_DUR = 0.3F;
    protected static final float FAST_ATTACK_ANIM_DUR = 0.4F;
    protected static final float HOP_ANIM_DURATION = 0.7F;
    private static final float STAGGER_MOVE_SPEED;
    protected TextureAtlas atlas;
    protected Skeleton skeleton;
    public AnimationState state;
    protected AnimationStateData stateData;
    private static final int RETICLE_W = 36;
    public float reticleAlpha;
    private Color reticleColor;
    private Color reticleShadowColor;
    public boolean reticleRendered;
    private float reticleOffset;
    private float reticleAnimTimer;
    private static final float RETICLE_OFFSET_DIST;

    public AbstractCreature() {
        this.hbYOffset = HB_Y_OFFSET_DIST * 5.0F;
        this.hbBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.hbShadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.blockColor = new Color(0.6F, 0.93F, 0.98F, 0.0F);
        this.blockOutlineColor = new Color(0.6F, 0.93F, 0.98F, 0.0F);
        this.blockTextColor = new Color(0.9F, 0.9F, 0.9F, 0.0F);
        this.redHbBarColor = new Color(0.8F, 0.05F, 0.05F, 0.0F);
        this.greenHbBarColor = Color.valueOf("78c13c00");
        this.blueHbBarColor = Color.valueOf("31568c00");
        this.orangeHbBarColor = new Color(1.0F, 0.5F, 0.0F, 0.0F);
        this.hbTextColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.tint = new TintEffect();
        this.shakeToggle = true;
        this.animationTimer = 0.0F;
        this.atlas = null;
        this.reticleAlpha = 0.0F;
        this.reticleColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.reticleShadowColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.reticleRendered = false;
        this.reticleOffset = 0.0F;
        this.reticleAnimTimer = 0.0F;
    }

    public abstract void damage(DamageInfo var1);

    private void brokeBlock() {
        SubscriptionManager.getInstance().triggerPostBreakBlock(this);
        if (this instanceof AbstractMonster) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                r.onBlockBroken(this);
            }
        }

        AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
        CardCrawlGame.sound.play("BLOCK_BREAK");
    }

    protected int decrementBlock(DamageInfo info, int damageAmount) {
        if (info.type != DamageType.HP_LOSS && this.currentBlock > 0) {
            CardCrawlGame.screenShake.shake(ShakeIntensity.MED, ShakeDur.SHORT, false);
            if (damageAmount > this.currentBlock) {
                damageAmount -= this.currentBlock;
                if (Settings.SHOW_DMG_BLOCK) {
                    AbstractDungeon.effectList.add(new BlockedNumberEffect(this.hb.cX, this.hb.cY + this.hb.height / 2.0F, Integer.toString(this.currentBlock)));
                }

                this.loseBlock();
                this.brokeBlock();
            } else if (damageAmount == this.currentBlock) {
                damageAmount = 0;
                this.loseBlock();
                this.brokeBlock();
                AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[1]));
            } else {
                CardCrawlGame.sound.play("BLOCK_ATTACK");
                this.loseBlock(damageAmount);

                for(int i = 0; i < 18; ++i) {
                    AbstractDungeon.effectList.add(new BlockImpactLineEffect(this.hb.cX, this.hb.cY));
                }

                if (Settings.SHOW_DMG_BLOCK) {
                    AbstractDungeon.effectList.add(new BlockedNumberEffect(this.hb.cX, this.hb.cY + this.hb.height / 2.0F, Integer.toString(damageAmount)));
                }

                damageAmount = 0;
            }
        }

        return damageAmount;
    }

    public void increaseMaxHp(int amount, boolean showEffect) {
        if (!Settings.isEndless || !AbstractDungeon.player.hasBlight("FullBelly")) {
            if (amount < 0) {
                logger.info("Why are we decreasing health with increaseMaxHealth()?");
            }

            this.maxHealth += amount;
            AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(this.hb.cX - this.animX, this.hb.cY, TEXT[2] + Integer.toString(amount), Settings.GREEN_TEXT_COLOR));
            this.heal(amount, true);
            this.healthBarUpdatedEvent();
        }

    }

    public void decreaseMaxHealth(int amount) {
        if (amount < 0) {
            logger.info("Why are we increasing health with decreaseMaxHealth()?");
        }

        this.maxHealth -= amount;
        if (this.maxHealth <= 1) {
            this.maxHealth = 1;
        }

        if (this.currentHealth > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        }

        this.healthBarUpdatedEvent();
    }

    protected void refreshHitboxLocation() {
        this.hb.move(this.drawX + this.hb_x + this.animX, this.drawY + this.hb_y + this.hb_h / 2.0F);
        this.healthHb.move(this.hb.cX, this.hb.cY - this.hb_h / 2.0F - this.healthHb.height / 2.0F);
    }

    public void updateAnimations() {
        if (this.animationTimer != 0.0F) {
            switch (this.animation) {
                case ATTACK_FAST:
                    this.updateFastAttackAnimation();
                    break;
                case ATTACK_SLOW:
                    this.updateSlowAttackAnimation();
                    break;
                case FAST_SHAKE:
                    this.updateFastShakeAnimation();
                    break;
                case HOP:
                    this.updateHopAnimation();
                    break;
                case JUMP:
                    this.updateJumpAnimation();
                    break;
                case SHAKE:
                    this.updateShakeAnimation();
                    break;
                case STAGGER:
                    this.updateStaggerAnimation();
            }
        }

        this.refreshHitboxLocation();
        if (!this.isPlayer) {
            ((AbstractMonster)this).refreshIntentHbLocation();
        }

    }

    protected void updateFastAttackAnimation() {
        this.animationTimer -= Gdx.graphics.getDeltaTime();
        float targetPos = 90.0F * Settings.scale;
        if (!this.isPlayer) {
            targetPos = -targetPos;
        }

        if (this.animationTimer > 0.5F) {
            this.animX = Interpolation.exp5In.apply(0.0F, targetPos, (1.0F - this.animationTimer / 1.0F) * 2.0F);
        } else if (this.animationTimer < 0.0F) {
            this.animationTimer = 0.0F;
            this.animX = 0.0F;
        } else {
            this.animX = Interpolation.fade.apply(0.0F, targetPos, this.animationTimer / 1.0F * 2.0F);
        }

    }

    protected void updateSlowAttackAnimation() {
        this.animationTimer -= Gdx.graphics.getDeltaTime();
        float targetPos = 90.0F * Settings.scale;
        if (!this.isPlayer) {
            targetPos = -targetPos;
        }

        if (this.animationTimer > 0.5F) {
            this.animX = Interpolation.exp10In.apply(0.0F, targetPos, (1.0F - this.animationTimer / 1.0F) * 2.0F);
        } else if (this.animationTimer < 0.0F) {
            this.animationTimer = 0.0F;
            this.animX = 0.0F;
        } else {
            this.animX = Interpolation.fade.apply(0.0F, targetPos, this.animationTimer / 1.0F * 2.0F);
        }

    }

    protected void updateFastShakeAnimation() {
        this.animationTimer -= Gdx.graphics.getDeltaTime();
        if (this.animationTimer < 0.0F) {
            this.animationTimer = 0.0F;
            this.animX = 0.0F;
        } else if (this.shakeToggle) {
            this.animX += SHAKE_SPEED * Gdx.graphics.getDeltaTime();
            if (this.animX > SHAKE_THRESHOLD / 2.0F) {
                this.shakeToggle = !this.shakeToggle;
            }
        } else {
            this.animX -= SHAKE_SPEED * Gdx.graphics.getDeltaTime();
            if (this.animX < -SHAKE_THRESHOLD / 2.0F) {
                this.shakeToggle = !this.shakeToggle;
            }
        }

    }

    protected void updateHopAnimation() {
        this.vY -= 17.0F * Settings.scale;
        this.animY += this.vY * Gdx.graphics.getDeltaTime();
        if (this.animY < 0.0F) {
            this.animationTimer = 0.0F;
            this.animY = 0.0F;
        }

    }

    protected void updateJumpAnimation() {
        this.vY -= 17.0F * Settings.scale;
        this.animY += this.vY * Gdx.graphics.getDeltaTime();
        if (this.animY < 0.0F) {
            this.animationTimer = 0.0F;
            this.animY = 0.0F;
        }

    }

    protected void updateStaggerAnimation() {
        if (this.animationTimer != 0.0F) {
            this.animationTimer -= Gdx.graphics.getDeltaTime();
            if (!this.isPlayer) {
                this.animX = Interpolation.pow2.apply(STAGGER_MOVE_SPEED, 0.0F, 1.0F - this.animationTimer / 0.3F);
            } else {
                this.animX = Interpolation.pow2.apply(-STAGGER_MOVE_SPEED, 0.0F, 1.0F - this.animationTimer / 0.3F);
            }

            if (this.animationTimer < 0.0F) {
                this.animationTimer = 0.0F;
                this.animX = 0.0F;
                this.vX = STAGGER_MOVE_SPEED;
            }
        }

    }

    protected void updateShakeAnimation() {
        this.animationTimer -= Gdx.graphics.getDeltaTime();
        if (this.animationTimer < 0.0F) {
            this.animationTimer = 0.0F;
            this.animX = 0.0F;
        } else if (this.shakeToggle) {
            this.animX += SHAKE_SPEED * Gdx.graphics.getDeltaTime();
            if (this.animX > SHAKE_THRESHOLD) {
                this.shakeToggle = !this.shakeToggle;
            }
        } else {
            this.animX -= SHAKE_SPEED * Gdx.graphics.getDeltaTime();
            if (this.animX < -SHAKE_THRESHOLD) {
                this.shakeToggle = !this.shakeToggle;
            }
        }

    }

    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(this.atlas);
        if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null) {
            if (AbstractDungeon.player.hasRelic("PreservedInsect") && !this.isPlayer && AbstractDungeon.getCurrRoom().eliteTrigger) {
                scale += 0.3F;
            }

            if (ModHelper.isModEnabled("MonsterHunter") && !this.isPlayer) {
                scale -= 0.3F;
            }
        }

        json.setScale(Settings.renderScale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);
    }

    public void heal(int healAmount, boolean showEffect) {
        if (Settings.isEndless && this.isPlayer && AbstractDungeon.player.hasBlight("FullBelly")) {
            healAmount /= 2;
            if (healAmount < 1) {
                healAmount = 1;
            }
        }

        if (!this.isDying) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if (this.isPlayer) {
                    healAmount = r.onPlayerHeal(healAmount);
                }
            }

            for(AbstractPower p : this.powers) {
                healAmount = p.onHeal(healAmount);
            }

            this.currentHealth += healAmount;
            if (this.currentHealth > this.maxHealth) {
                this.currentHealth = this.maxHealth;
            }

            if ((float)this.currentHealth > (float)this.maxHealth / 2.0F && this.isBloodied) {
                this.isBloodied = false;

                for(AbstractRelic r2 : AbstractDungeon.player.relics) {
                    r2.onNotBloodied();
                }
            }

            if (healAmount > 0) {
                if (showEffect && this.isPlayer) {
                    AbstractDungeon.topPanel.panelHealEffect();
                    AbstractDungeon.effectsQueue.add(new HealEffect(this.hb.cX - this.animX, this.hb.cY, healAmount));
                }

                this.healthBarUpdatedEvent();
            }

        }
    }

    public void heal(int amount) {
        this.heal(amount, true);
    }

    public void addBlock(int blockAmount) {
        blockAmount = SubscriptionManager.getInstance().triggerPreBlockChange(this, blockAmount);
        float tmp = (float)blockAmount;
        if (this.isPlayer) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                tmp = (float)r.onPlayerGainedBlock(tmp);
            }

            if (tmp > 0.0F) {
                for(AbstractPower p : this.powers) {
                    p.onGainedBlock(tmp);
                }
            }
        }

        boolean effect = false;
        if (this.currentBlock == 0) {
            effect = true;
        }

        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            for(AbstractPower p : m.powers) {
                tmp = (float)p.onPlayerGainedBlock(tmp);
            }
        }

        this.currentBlock += MathUtils.floor(tmp);
        if (this.currentBlock >= 99 && this.isPlayer) {
            UnlockTracker.unlockAchievement("IMPERVIOUS");
        }

        if (this.currentBlock > 999) {
            this.currentBlock = 999;
        }

        if (this.currentBlock == 999) {
            UnlockTracker.unlockAchievement("BARRICADED");
        }

        if (effect && this.currentBlock > 0) {
            this.gainBlockAnimation();
        } else if (blockAmount > 0 && blockAmount > 0) {
            Color tmpCol = Settings.GOLD_COLOR.cpy();
            tmpCol.a = this.blockTextColor.a;
            this.blockTextColor = tmpCol;
            this.blockScale = 5.0F;
        }

    }

    public void loseBlock(int amount, boolean noAnimation) {
        amount = -SubscriptionManager.getInstance().triggerPreBlockChange(this, -amount);
        boolean effect = false;
        if (this.currentBlock != 0) {
            effect = true;
        }

        this.currentBlock -= amount;
        if (this.currentBlock < 0) {
            this.currentBlock = 0;
        }

        if (this.currentBlock == 0 && effect) {
            if (!noAnimation) {
                AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
            }
        } else if (this.currentBlock > 0 && amount > 0) {
            Color tmp = Color.SCARLET.cpy();
            tmp.a = this.blockTextColor.a;
            this.blockTextColor = tmp;
            this.blockScale = 5.0F;
        }

    }

    public void loseBlock() {
        this.loseBlock(this.currentBlock);
    }

    public void loseBlock(boolean noAnimation) {
        this.loseBlock(this.currentBlock, noAnimation);
    }

    public void loseBlock(int amount) {
        this.loseBlock(amount, false);
    }

    public void showHealthBar() {
        this.hbShowTimer = 0.7F;
        this.hbAlpha = 0.0F;
    }

    public void hideHealthBar() {
        this.hbAlpha = 0.0F;
    }

    public void addPower(AbstractPower powerToApply) {
        boolean hasBuffAlready = false;

        for(AbstractPower p : this.powers) {
            if (p.ID.equals(powerToApply.ID)) {
                p.stackPower(powerToApply.amount);
                p.updateDescription();
                hasBuffAlready = true;
            }
        }

        if (!hasBuffAlready) {
            this.powers.add(powerToApply);
            if (this.isPlayer) {
                int buffCount = 0;

                for(AbstractPower p : this.powers) {
                    if (p.type == PowerType.BUFF) {
                        ++buffCount;
                    }
                }

                if (buffCount >= 10) {
                    UnlockTracker.unlockAchievement("POWERFUL");
                }
            }
        }

    }

    public void applyStartOfTurnPowers() {
        for(AbstractPower p : this.powers) {
            p.atStartOfTurn();
        }

    }

    public void applyTurnPowers() {
        for(AbstractPower p : this.powers) {
            p.duringTurn();
        }

    }

    public void applyStartOfTurnPostDrawPowers() {
        for(AbstractPower p : this.powers) {
            p.atStartOfTurnPostDraw();
        }

    }

    public void applyEndOfTurnTriggers() {
        for(AbstractPower p : this.powers) {
            if (!this.isPlayer) {
                p.atEndOfTurnPreEndTurnCards(false);
            }

            p.atEndOfTurn(this.isPlayer);
        }

    }

    public void healthBarUpdatedEvent() {
        this.healthBarAnimTimer = 1.2F;
        this.targetHealthBarWidth = this.hb.width * (float)this.currentHealth / (float)this.maxHealth;
        if (this.maxHealth == this.currentHealth) {
            this.healthBarWidth = this.targetHealthBarWidth;
        } else if (this.currentHealth == 0) {
            this.healthBarWidth = 0.0F;
            this.targetHealthBarWidth = 0.0F;
        }

        if (this.targetHealthBarWidth > this.healthBarWidth) {
            this.healthBarWidth = this.targetHealthBarWidth;
        }

    }

    public void healthBarRevivedEvent() {
        this.healthBarAnimTimer = 1.2F;
        this.targetHealthBarWidth = this.hb.width * (float)this.currentHealth / (float)this.maxHealth;
        this.healthBarWidth = this.targetHealthBarWidth;
        this.hbBgColor.a = 0.75F;
        this.hbShadowColor.a = 0.5F;
        this.hbTextColor.a = 1.0F;
    }

    protected void updateHealthBar() {
        this.updateHbHoverFade();
        this.updateBlockAnimations();
        this.updateHbPopInAnimation();
        this.updateHbDamageAnimation();
        this.updateHbAlpha();
    }

    private void updateHbHoverFade() {
        if (this.healthHb.hovered) {
            this.healthHideTimer -= Gdx.graphics.getDeltaTime() * 4.0F;
            if (this.healthHideTimer < 0.2F) {
                this.healthHideTimer = 0.2F;
            }
        } else {
            this.healthHideTimer += Gdx.graphics.getDeltaTime() * 4.0F;
            if (this.healthHideTimer > 1.0F) {
                this.healthHideTimer = 1.0F;
            }
        }

    }

    private void updateHbAlpha() {
        if (this instanceof AbstractMonster && ((AbstractMonster)this).isEscaping) {
            this.hbAlpha = MathHelper.fadeLerpSnap(this.hbAlpha, 0.0F);
            this.targetHealthBarWidth = 0.0F;
            this.hbBgColor.a = this.hbAlpha * 0.75F;
            this.hbShadowColor.a = this.hbAlpha * 0.5F;
            this.hbTextColor.a = this.hbAlpha;
            this.orangeHbBarColor.a = this.hbAlpha;
            this.redHbBarColor.a = this.hbAlpha;
            this.greenHbBarColor.a = this.hbAlpha;
            this.blueHbBarColor.a = this.hbAlpha;
            this.blockOutlineColor.a = this.hbAlpha;
        } else if (this.targetHealthBarWidth == 0.0F && this.healthBarAnimTimer <= 0.0F) {
            this.hbShadowColor.a = MathHelper.fadeLerpSnap(this.hbShadowColor.a, 0.0F);
            this.hbBgColor.a = MathHelper.fadeLerpSnap(this.hbBgColor.a, 0.0F);
            this.hbTextColor.a = MathHelper.fadeLerpSnap(this.hbTextColor.a, 0.0F);
            this.blockOutlineColor.a = MathHelper.fadeLerpSnap(this.blockOutlineColor.a, 0.0F);
        } else {
            this.hbBgColor.a = this.hbAlpha * 0.5F;
            this.hbShadowColor.a = this.hbAlpha * 0.2F;
            this.hbTextColor.a = this.hbAlpha;
            this.orangeHbBarColor.a = this.hbAlpha;
            this.redHbBarColor.a = this.hbAlpha;
            this.greenHbBarColor.a = this.hbAlpha;
            this.blueHbBarColor.a = this.hbAlpha;
            this.blockOutlineColor.a = this.hbAlpha;
        }

    }

    protected void gainBlockAnimation() {
        this.blockAnimTimer = 0.7F;
        this.blockTextColor.a = 0.0F;
        this.blockColor.a = 0.0F;
    }

    private void updateBlockAnimations() {
        if (this.currentBlock > 0) {
            if (this.blockAnimTimer > 0.0F) {
                this.blockAnimTimer -= Gdx.graphics.getDeltaTime();
                if (this.blockAnimTimer < 0.0F) {
                    this.blockAnimTimer = 0.0F;
                }

                this.blockOffset = Interpolation.swingOut.apply(BLOCK_OFFSET_DIST * 3.0F, 0.0F, 1.0F - this.blockAnimTimer / 0.7F);
                this.blockScale = Interpolation.pow3In.apply(3.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
                this.blockColor.a = Interpolation.pow2Out.apply(0.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
                this.blockTextColor.a = Interpolation.pow5In.apply(0.0F, 1.0F, 1.0F - this.blockAnimTimer / 0.7F);
            } else if (this.blockScale != 1.0F) {
                this.blockScale = MathHelper.scaleLerpSnap(this.blockScale, 1.0F);
            }

            if (this.blockTextColor.r != 1.0F) {
                this.blockTextColor.r = MathHelper.slowColorLerpSnap(this.blockTextColor.r, 1.0F);
            }

            if (this.blockTextColor.g != 1.0F) {
                this.blockTextColor.g = MathHelper.slowColorLerpSnap(this.blockTextColor.g, 1.0F);
            }

            if (this.blockTextColor.b != 1.0F) {
                this.blockTextColor.b = MathHelper.slowColorLerpSnap(this.blockTextColor.b, 1.0F);
            }
        }

    }

    private void updateHbPopInAnimation() {
        if (this.hbShowTimer > 0.0F) {
            this.hbShowTimer -= Gdx.graphics.getDeltaTime();
            if (this.hbShowTimer < 0.0F) {
                this.hbShowTimer = 0.0F;
            }

            this.hbAlpha = Interpolation.fade.apply(0.0F, 1.0F, 1.0F - this.hbShowTimer / 0.7F);
            this.hbYOffset = Interpolation.exp10Out.apply(HB_Y_OFFSET_DIST * 5.0F, 0.0F, 1.0F - this.hbShowTimer / 0.7F);
        }

    }

    private void updateHbDamageAnimation() {
        if (this.healthBarAnimTimer > 0.0F) {
            this.healthBarAnimTimer -= Gdx.graphics.getDeltaTime();
        }

        if (this.healthBarWidth != this.targetHealthBarWidth && this.healthBarAnimTimer <= 0.0F && this.targetHealthBarWidth < this.healthBarWidth) {
            this.healthBarWidth = MathHelper.uiLerpSnap(this.healthBarWidth, this.targetHealthBarWidth);
        }

    }

    public void updatePowers() {
        for(int i = 0; i < this.powers.size(); ++i) {
            ((AbstractPower)this.powers.get(i)).update(i);
        }

    }

    public static void initialize() {
        sr = new SkeletonMeshRenderer();
        sr.setPremultipliedAlpha(true);
    }

    public void renderPowerTips(SpriteBatch sb) {
        this.tips.clear();

        for(AbstractPower p : this.powers) {
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48));
            } else {
                this.tips.add(new PowerTip(p.name, p.description, p.img));
            }
        }

        if (!this.tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
        }

    }

    public void useFastAttackAnimation() {
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.animationTimer = 0.4F;
        this.animation = AbstractCreature.CreatureAnimation.ATTACK_FAST;
    }

    public void useSlowAttackAnimation() {
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.animationTimer = 1.0F;
        this.animation = AbstractCreature.CreatureAnimation.ATTACK_SLOW;
    }

    public void useHopAnimation() {
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.vY = 300.0F * Settings.scale;
        this.animationTimer = 0.7F;
        this.animation = AbstractCreature.CreatureAnimation.HOP;
    }

    public void useJumpAnimation() {
        this.animX = 0.0F;
        this.animY = 0.0F;
        this.vY = 500.0F * Settings.scale;
        this.animationTimer = 0.7F;
        this.animation = AbstractCreature.CreatureAnimation.JUMP;
    }

    public void useStaggerAnimation() {
        if (this.animY == 0.0F) {
            this.animX = 0.0F;
            this.animationTimer = 0.3F;
            this.animation = AbstractCreature.CreatureAnimation.STAGGER;
        }

    }

    public void useFastShakeAnimation(float duration) {
        if (this.animY == 0.0F) {
            this.animX = 0.0F;
            this.animationTimer = duration;
            this.animation = AbstractCreature.CreatureAnimation.FAST_SHAKE;
        }

    }

    public void useShakeAnimation(float duration) {
        if (this.animY == 0.0F) {
            this.animX = 0.0F;
            this.animationTimer = duration;
            this.animation = AbstractCreature.CreatureAnimation.SHAKE;
        }

    }

    public AbstractPower getPower(String targetID) {
        for(AbstractPower p : this.powers) {
            if (p.ID.equals(targetID)) {
                return p;
            }
        }

        return null;
    }

    public boolean hasPower(String targetID) {
        for(AbstractPower p : this.powers) {
            if (p.ID.equals(targetID)) {
                return true;
            }
        }

        return false;
    }

    public boolean isDeadOrEscaped() {
        if (!this.isDying && !this.halfDead) {
            if (!this.isPlayer) {
                AbstractMonster m = (AbstractMonster)this;
                if (m.isEscaping) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public void loseGold(int goldAmount) {
        if (goldAmount > 0) {
            this.gold -= goldAmount;
            if (this.gold < 0) {
                this.gold = 0;
            }
        } else {
            logger.info("NEGATIVE MONEY???");
        }

    }

    public void gainGold(int amount) {
        if (amount < 0) {
            logger.info("NEGATIVE MONEY???");
        } else {
            this.gold += amount;
        }

    }

    public void renderReticle(SpriteBatch sb) {
        this.reticleRendered = true;
        this.renderReticleCorner(sb, -this.hb.width / 2.0F + this.reticleOffset, this.hb.height / 2.0F - this.reticleOffset, false, false);
        this.renderReticleCorner(sb, this.hb.width / 2.0F - this.reticleOffset, this.hb.height / 2.0F - this.reticleOffset, true, false);
        this.renderReticleCorner(sb, -this.hb.width / 2.0F + this.reticleOffset, -this.hb.height / 2.0F + this.reticleOffset, false, true);
        this.renderReticleCorner(sb, this.hb.width / 2.0F - this.reticleOffset, -this.hb.height / 2.0F + this.reticleOffset, true, true);
    }

    public void renderReticle(SpriteBatch sb, Hitbox hb) {
        this.reticleRendered = true;
        this.renderReticleCorner(sb, -hb.width / 2.0F + this.reticleOffset, hb.height / 2.0F - this.reticleOffset, hb, false, false);
        this.renderReticleCorner(sb, hb.width / 2.0F - this.reticleOffset, hb.height / 2.0F - this.reticleOffset, hb, true, false);
        this.renderReticleCorner(sb, -hb.width / 2.0F + this.reticleOffset, -hb.height / 2.0F + this.reticleOffset, hb, false, true);
        this.renderReticleCorner(sb, hb.width / 2.0F - this.reticleOffset, -hb.height / 2.0F + this.reticleOffset, hb, true, true);
    }

    protected void updateReticle() {
        if (this.reticleRendered) {
            this.reticleRendered = false;
            this.reticleAlpha += Gdx.graphics.getDeltaTime() * 3.0F;
            if (this.reticleAlpha > 1.0F) {
                this.reticleAlpha = 1.0F;
            }

            this.reticleAnimTimer += Gdx.graphics.getDeltaTime();
            if (this.reticleAnimTimer > 1.0F) {
                this.reticleAnimTimer = 1.0F;
            }

            this.reticleOffset = Interpolation.elasticOut.apply(RETICLE_OFFSET_DIST, 0.0F, this.reticleAnimTimer);
        } else {
            this.reticleAlpha = 0.0F;
            this.reticleAnimTimer = 0.0F;
            this.reticleOffset = RETICLE_OFFSET_DIST;
        }

    }

    public void renderHealth(SpriteBatch sb) {
        if (!Settings.hideCombatElements) {
            float x = this.hb.cX - this.hb.width / 2.0F;
            float y = this.hb.cY - this.hb.height / 2.0F + this.hbYOffset;
            this.renderHealthBg(sb, x, y);
            if (this.targetHealthBarWidth != 0.0F) {
                this.renderOrangeHealthBar(sb, x, y);
                if (this.hasPower("Poison")) {
                    this.renderGreenHealthBar(sb, x, y);
                }

                this.renderRedHealthBar(sb, x, y);
            }

            if (this.currentBlock != 0 && this.hbAlpha != 0.0F) {
                this.renderBlockOutline(sb, x, y);
            }

            this.renderHealthText(sb, y);
            if (this.currentBlock != 0 && this.hbAlpha != 0.0F) {
                this.renderBlockIconAndValue(sb, x, y);
            }

            this.renderPowerIcons(sb, x, y);
        }
    }

    private void renderBlockOutline(SpriteBatch sb, float x, float y) {
        sb.setColor(this.blockOutlineColor);
        sb.setBlendFunction(770, 1);
        sb.draw(ImageMaster.BLOCK_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.BLOCK_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.hb.width, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.BLOCK_BAR_R, x + this.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.setBlendFunction(770, 771);
    }

    private void renderBlockIconAndValue(SpriteBatch sb, float x, float y) {
        sb.setColor(this.blockColor);
        sb.draw(ImageMaster.BLOCK_ICON, x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 32.0F + this.blockOffset, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, Integer.toString(this.currentBlock), x + BLOCK_ICON_X, y - 16.0F * Settings.scale, this.blockTextColor, this.blockScale);
    }

    private void renderHealthBg(SpriteBatch sb, float x, float y) {
        sb.setColor(this.hbShadowColor);
        sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, this.hb.width, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HB_SHADOW_R, x + this.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.setColor(this.hbBgColor);
        if (this.currentHealth != this.maxHealth) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + this.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }

    }

    private void renderOrangeHealthBar(SpriteBatch sb, float x, float y) {
        sb.setColor(this.orangeHbBarColor);
        sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.healthBarWidth, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.healthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
    }

    private void renderGreenHealthBar(SpriteBatch sb, float x, float y) {
        sb.setColor(this.greenHbBarColor);
        if (this.currentHealth > 0) {
            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        }

        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.targetHealthBarWidth, HEALTH_BAR_HEIGHT);
        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.targetHealthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
    }

    private void renderRedHealthBar(SpriteBatch sb, float x, float y) {
        if (this.currentBlock > 0) {
            sb.setColor(this.blueHbBarColor);
        } else {
            sb.setColor(this.redHbBarColor);
        }

        if (!this.hasPower("Poison")) {
            if (this.currentHealth > 0) {
                sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            }

            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.targetHealthBarWidth, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + this.targetHealthBarWidth, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        } else {
            int poisonAmt = this.getPower("Poison").amount;
            if (poisonAmt > 0 && this.hasPower("Intangible")) {
                poisonAmt = 1;
            }

            if (this.currentHealth > poisonAmt) {
                float w = 1.0F - (float)(this.currentHealth - poisonAmt) / (float)this.currentHealth;
                w *= this.targetHealthBarWidth;
                if (this.currentHealth > 0) {
                    sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
                }

                sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, this.targetHealthBarWidth - w, HEALTH_BAR_HEIGHT);
                sb.draw(ImageMaster.HEALTH_BAR_R, x + this.targetHealthBarWidth - w, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            }
        }

    }

    private void renderHealthText(SpriteBatch sb, float y) {
        if (this.targetHealthBarWidth != 0.0F) {
            float tmp = this.hbTextColor.a;
            Color var10000 = this.hbTextColor;
            var10000.a *= this.healthHideTimer;
            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, this.currentHealth + "/" + this.maxHealth, this.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, this.hbTextColor);
            this.hbTextColor.a = tmp;
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, TEXT[0], this.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y - 1.0F * Settings.scale, this.hbTextColor);
        }

    }

    private void renderPowerIcons(SpriteBatch sb, float x, float y) {
        float offset = 10.0F * Settings.scale;

        for(AbstractPower p : this.powers) {
            if (Settings.isMobile) {
                p.renderIcons(sb, x + offset, y - 53.0F * Settings.scale, this.hbTextColor);
            } else {
                p.renderIcons(sb, x + offset, y - 48.0F * Settings.scale, this.hbTextColor);
            }

            offset += POWER_ICON_PADDING_X;
        }

        offset = 0.0F * Settings.scale;

        for(AbstractPower p : this.powers) {
            if (Settings.isMobile) {
                p.renderAmount(sb, x + offset + 32.0F * Settings.scale, y - 75.0F * Settings.scale, this.hbTextColor);
            } else {
                p.renderAmount(sb, x + offset + 32.0F * Settings.scale, y - 66.0F * Settings.scale, this.hbTextColor);
            }

            offset += POWER_ICON_PADDING_X;
        }

    }

    private void renderReticleCorner(SpriteBatch sb, float x, float y, Hitbox hb, boolean flipX, boolean flipY) {
        this.reticleShadowColor.a = this.reticleAlpha / 4.0F;
        sb.setColor(this.reticleShadowColor);
        sb.draw(ImageMaster.RETICLE_CORNER, hb.cX + x - 18.0F + 4.0F * Settings.scale, hb.cY + y - 18.0F - 4.0F * Settings.scale, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
        this.reticleColor.a = this.reticleAlpha;
        sb.setColor(this.reticleColor);
        sb.draw(ImageMaster.RETICLE_CORNER, hb.cX + x - 18.0F, hb.cY + y - 18.0F, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
    }

    private void renderReticleCorner(SpriteBatch sb, float x, float y, boolean flipX, boolean flipY) {
        this.reticleShadowColor.a = this.reticleAlpha / 4.0F;
        sb.setColor(this.reticleShadowColor);
        sb.draw(ImageMaster.RETICLE_CORNER, this.hb.cX + x - 18.0F + 4.0F * Settings.scale, this.hb.cY + y - 18.0F - 4.0F * Settings.scale, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
        this.reticleColor.a = this.reticleAlpha;
        sb.setColor(this.reticleColor);
        sb.draw(ImageMaster.RETICLE_CORNER, this.hb.cX + x - 18.0F, this.hb.cY + y - 18.0F, 18.0F, 18.0F, 36.0F, 36.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 36, 36, flipX, flipY);
    }

    public abstract void render(SpriteBatch var1);

    static {
        TIP_X_THRESHOLD = 1544.0F * Settings.scale;
        MULTI_TIP_Y_OFFSET = 80.0F * Settings.scale;
        TIP_OFFSET_R_X = 20.0F * Settings.scale;
        TIP_OFFSET_L_X = -380.0F * Settings.scale;
        TIP_OFFSET_Y = 80.0F * Settings.scale;
        uiStrings = CardCrawlGame.languagePack.getUIString("AbstractCreature");
        TEXT = uiStrings.TEXT;
        BLOCK_OFFSET_DIST = 12.0F * Settings.scale;
        HB_Y_OFFSET_DIST = 12.0F * Settings.scale;
        BLOCK_ICON_X = -14.0F * Settings.scale;
        BLOCK_ICON_Y = -14.0F * Settings.scale;
        HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
        HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
        HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;
        POWER_ICON_PADDING_X = Settings.isMobile ? 55.0F * Settings.scale : 48.0F * Settings.scale;
        HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
        SHAKE_THRESHOLD = Settings.scale * 8.0F;
        SHAKE_SPEED = 150.0F * Settings.scale;
        STAGGER_MOVE_SPEED = 20.0F * Settings.scale;
        RETICLE_OFFSET_DIST = 15.0F * Settings.scale;
    }

    public static enum CreatureAnimation {
        FAST_SHAKE,
        SHAKE,
        ATTACK_FAST,
        ATTACK_SLOW,
        STAGGER,
        HOP,
        JUMP;

        private CreatureAnimation() {
        }
    }
}

