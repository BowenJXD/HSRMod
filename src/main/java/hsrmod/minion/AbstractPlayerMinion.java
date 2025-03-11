package hsrmod.minion;

import hsrmod.minion.MinionMoveInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import com.megacrit.cardcrawl.vfx.combat.HbBlockBrokenEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;
import hsrmod.effects.FlashMinionIntentEffect;
import hsrmod.modcore.HSRMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class AbstractPlayerMinion extends AbstractCreature {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    protected static final byte ESCAPE = 99;
    public float deathTimer;
    private final Color nameColor;
    private final Color nameBgColor;
    protected Texture img;
    public boolean tintFadeOutCalled;
    protected HashMap<Byte, String> moveSet;
    public boolean escaped;
    public boolean escapeNext;
    private final PowerTip intentTip;
    public MinionType type;
    private float hoverTimer;
    public boolean cannotEscape;
    public ArrayList<DamageInfo> damage;
    private float intentParticleTimer;
    private float intentAngle;
    private final ArrayList<AbstractGameEffect> intentVfx;
    public byte nextMove;
    private final BobEffect bobEffect;
    private static final float INTENT_HB_W;
    public Hitbox intentHb;
    public MinionIntent intent;
    public MinionIntent tipIntent;
    public float intentAlpha;
    public float intentAlphaTarget;
    public float intentOffsetX;
    private Texture intentImg;
    private Texture intentBg;
    private int intentDmg;
    private int intentBaseDmg;
    private int intentMultiAmt;
    private boolean isMultiDmg;
    private final Color intentColor;
    public String moveName;
    public static String[] MOVES;
    public static String[] DIALOG;
    public ArrayList<Byte> moveHistory;
    private MinionMoveInfo move;
    protected AbstractCreature targetMonster;

    public AbstractPlayerMinion(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        this(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, false);
    }

    public AbstractPlayerMinion(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        this.deathTimer = 0.0F;
        this.nameColor = new Color();
        this.nameBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        this.tintFadeOutCalled = false;
        this.moveSet = new HashMap();
        this.escaped = false;
        this.escapeNext = false;
        this.intentTip = new PowerTip();
        this.type = MinionType.None;
        this.hoverTimer = 0.0F;
        this.cannotEscape = false;
        this.damage = new ArrayList();
        this.intentParticleTimer = 0.0F;
        this.intentAngle = 0.0F;
        this.moveHistory = new ArrayList();
        this.intentVfx = new ArrayList();
        this.nextMove = -1;
        this.bobEffect = new BobEffect();
        this.intent = MinionIntent.DEBUG;
        this.tipIntent = MinionIntent.DEBUG;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 0.0F;
        this.intentOffsetX = 0.0F;
        this.intentImg = null;
        this.intentBg = null;
        this.intentDmg = -1;
        this.intentBaseDmg = -1;
        this.intentMultiAmt = 0;
        this.isMultiDmg = false;
        this.intentColor = Color.WHITE.cpy();
        this.moveName = null;
        this.isPlayer = false;
        this.name = name;
        this.id = id;
        this.maxHealth = maxHealth;
        if (Settings.isMobile) {
            hb_w *= 1.17F;
        }

        this.currentHealth = this.maxHealth;
        this.currentBlock = 0;
        this.drawX = AbstractDungeon.player.drawX + offsetX * Settings.xScale;
        this.drawY = AbstractDungeon.floorY + offsetY * Settings.yScale;
        this.hb_w = hb_w * Settings.scale;
        this.hb_h = hb_h * Settings.xScale;
        this.hb_x = hb_x * Settings.scale;
        this.hb_y = hb_y * Settings.scale;
        if (imgUrl != null) {
            this.img = ImageMaster.loadImage(imgUrl);
        }

        this.intentHb = new Hitbox(INTENT_HB_W, INTENT_HB_W);
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.healthHb = new Hitbox(this.hb_w, 72.0F * Settings.scale);
        this.refreshHitboxLocation();
        this.refreshIntentHbLocation();
    }

    public AbstractPlayerMinion(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        this(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, 0.0F, 0.0F);
    }

    public void refreshIntentHbLocation() {
        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + INTENT_HB_W / 2.0F);
    }

    public void update() {
        for (AbstractPower p : this.powers) {
            p.updateParticles();
        }

        this.updateReticle();
        this.updateHealthBar();
        this.updatePowers();
        this.updateAnimations();
        this.updateIntent();
        this.tint.update();
        this.hb.update();
        this.intentHb.update();
        this.healthHb.update();
    }

    protected void updateFastAttackAnimation() {
        this.animationTimer -= Gdx.graphics.getDeltaTime();
        float targetPos = 90.0F * Settings.scale;
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
        if (this.animationTimer > 0.5F) {
            this.animX = Interpolation.exp10In.apply(0.0F, targetPos, (1.0F - this.animationTimer / 1.0F) * 2.0F);
        } else if (this.animationTimer < 0.0F) {
            this.animationTimer = 0.0F;
            this.animX = 0.0F;
        } else {
            this.animX = Interpolation.fade.apply(0.0F, targetPos, this.animationTimer / 1.0F * 2.0F);
        }

    }

    protected void updateStaggerAnimation() {
        if (this.animationTimer != 0.0F) {
            this.animationTimer -= Gdx.graphics.getDeltaTime();
            if (!this.isPlayer) {
                this.animX = Interpolation.pow2.apply(20.0F * Settings.scale, 0.0F, 1.0F - this.animationTimer / 0.3F);
            } else {
                this.animX = Interpolation.pow2.apply(-20.0F * Settings.scale, 0.0F, 1.0F - this.animationTimer / 0.3F);
            }

            if (this.animationTimer < 0.0F) {
                this.animationTimer = 0.0F;
                this.animX = 0.0F;
                this.vX = 20.0F * Settings.scale;
            }
        }

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
        this.refreshIntentHbLocation();
    }

    public void unhover() {
        this.healthHb.hovered = false;
        this.hb.hovered = false;
        this.intentHb.hovered = false;
    }

    private void updateIntent() {
        this.bobEffect.update();
        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
            this.intentAlpha += Gdx.graphics.getDeltaTime();
            if (this.intentAlpha > this.intentAlphaTarget) {
                this.intentAlpha = this.intentAlphaTarget;
            }
        } else if (this.intentAlphaTarget == 0.0F) {
            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 1.5F;
            if (this.intentAlpha < 0.0F) {
                this.intentAlpha = 0.0F;
            }
        }

        if (!this.isDying && !this.isEscaping) {
            this.updateIntentVFX();
        }

        Iterator<AbstractGameEffect> i = this.intentVfx.iterator();

        while (i.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect) i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }

    }

    private void updateIntentVFX() {
        if (this.intentAlpha > 0.0F) {
            if (this.intent != MinionIntent.ATTACK_DEBUFF && this.intent != MinionIntent.DEBUFF && this.intent != MinionIntent.STRONG_DEBUFF && this.intent != MinionIntent.DEFEND_DEBUFF) {
                if (this.intent != MinionIntent.ATTACK_BUFF && this.intent != MinionIntent.BUFF && this.intent != MinionIntent.DEFEND_BUFF) {
                    if (this.intent == MinionIntent.ATTACK_DEFEND) {
                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (this.intentParticleTimer < 0.0F) {
                            this.intentParticleTimer = 0.5F;
                            this.intentVfx.add(new ShieldParticleEffect(this.intentHb.cX, this.intentHb.cY));
                        }
                    } else if (this.intent == MinionIntent.UNKNOWN) {
                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (this.intentParticleTimer < 0.0F) {
                            this.intentParticleTimer = 0.5F;
                            this.intentVfx.add(new UnknownParticleEffect(this.intentHb.cX, this.intentHb.cY));
                        }
                    } else if (this.intent == MinionIntent.STUN) {
                        this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (this.intentParticleTimer < 0.0F) {
                            this.intentParticleTimer = 0.67F;
                            this.intentVfx.add(new StunStarEffect(this.intentHb.cX, this.intentHb.cY));
                        }
                    }
                } else {
                    this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                    if (this.intentParticleTimer < 0.0F) {
                        this.intentParticleTimer = 0.1F;
                        this.intentVfx.add(new BuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                    }
                }
            } else {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 1.0F;
                    this.intentVfx.add(new DebuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            }
        }

    }

    public void renderTip(SpriteBatch sb) {
        this.tips.clear();
        if (this.intentAlphaTarget == 1.0F && this.intent != MinionIntent.NONE) {
            this.tips.add(this.intentTip);
        }

        for (AbstractPower p : this.powers) {
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

    private void updateIntentTip() {
        switch (this.intent) {
            case ATTACK:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[1] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[4] + this.intentDmg + TEXT[5];
                }

                this.intentTip.img = this.getAttackIntentTip();
                return;
            case ATTACK_BUFF:
                this.intentTip.header = TEXT[6];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[7] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[8];
                } else {
                    this.intentTip.body = TEXT[9] + this.intentDmg + TEXT[5];
                }

                this.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                return;
            case ATTACK_DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[11] + this.intentDmg + TEXT[5];
                this.intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
                return;
            case ATTACK_DEFEND:
                this.intentTip.header = TEXT[0];
                if (this.isMultiDmg) {
                    this.intentTip.body = TEXT[12] + this.intentDmg + TEXT[2] + this.intentMultiAmt + TEXT[3];
                } else {
                    this.intentTip.body = TEXT[12] + this.intentDmg + TEXT[5];
                }

                this.intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                return;
            case BUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[19];
                this.intentTip.img = ImageMaster.INTENT_BUFF;
                return;
            case DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[20];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF;
                return;
            case STRONG_DEBUFF:
                this.intentTip.header = TEXT[10];
                this.intentTip.body = TEXT[21];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF2;
                return;
            case DEFEND:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[22];
                this.intentTip.img = ImageMaster.INTENT_DEFEND;
                return;
            case DEFEND_DEBUFF:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[23];
                this.intentTip.img = ImageMaster.INTENT_DEFEND;
                return;
            case DEFEND_BUFF:
                this.intentTip.header = TEXT[13];
                this.intentTip.body = TEXT[24];
                this.intentTip.img = ImageMaster.INTENT_DEFEND_BUFF;
                return;
            case ESCAPE:
                this.intentTip.header = TEXT[14];
                this.intentTip.body = TEXT[25];
                this.intentTip.img = ImageMaster.INTENT_ESCAPE;
                return;
            case MAGIC:
                this.intentTip.header = TEXT[15];
                this.intentTip.body = TEXT[26];
                this.intentTip.img = ImageMaster.INTENT_MAGIC;
                return;
            case SLEEP:
                this.intentTip.header = TEXT[16];
                this.intentTip.body = TEXT[27];
                this.intentTip.img = ImageMaster.INTENT_SLEEP;
                return;
            case STUN:
                this.intentTip.header = TEXT[17];
                this.intentTip.body = TEXT[28];
                this.intentTip.img = ImageMaster.INTENT_STUN;
                return;
            case UNKNOWN:
                this.intentTip.header = TEXT[18];
                this.intentTip.body = TEXT[29];
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
            case NONE:
                this.intentTip.header = "";
                this.intentTip.body = "";
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
            default:
                this.intentTip.header = "NOT SET";
                this.intentTip.body = "NOT SET";
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
        }
    }

    public void heal(int healAmount, boolean showEffect) {
        if (!this.isDying) {
            for (AbstractPower p : this.powers) {
                healAmount = p.onHeal(healAmount);
            }

            this.currentHealth += healAmount;
            if (this.currentHealth > this.maxHealth) {
                this.currentHealth = this.maxHealth;
            }

            if (healAmount > 0) {
                AbstractDungeon.effectsQueue.add(new HealEffect(this.hb.cX - this.animX, this.hb.cY, healAmount));
                this.healthBarUpdatedEvent();
            }

        }
    }

    public void flashIntent() {
        if (this.intentImg != null) {
            AbstractDungeon.effectList.add(new FlashMinionIntentEffect(this.intentImg, this));
        }

        this.intentAlphaTarget = 0.0F;
    }

    public void createIntent() {
        this.intent = this.move.intent;
        this.intentParticleTimer = 0.5F;
        this.nextMove = this.move.nextMove;
        this.intentBaseDmg = this.move.baseDamage;
        if (this.move.baseDamage > -1) {
            this.calculateDamage(this.intentBaseDmg);
            if (this.move.isMultiDamage) {
                this.intentMultiAmt = this.move.multiplier;
                this.isMultiDmg = true;
            } else {
                this.intentMultiAmt = -1;
                this.isMultiDmg = false;
            }
        }

        this.intentImg = this.getIntentImg();
        this.intentBg = this.getIntentBg();
        this.tipIntent = this.intent;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        this.updateIntentTip();
    }

    public void setMove(String moveName, byte nextMove, MinionIntent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        this.moveName = moveName;
        if (nextMove != -1) {
            this.moveHistory.add(nextMove);
        }

        this.move = new MinionMoveInfo(nextMove, intent, baseDamage, multiplier, isMultiDamage);
    }

    public void setMove(byte nextMove, MinionIntent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        this.setMove((String) null, nextMove, intent, baseDamage, multiplier, isMultiDamage);
    }

    public void setMove(byte nextMove, MinionIntent intent, int baseDamage) {
        this.setMove((String) null, nextMove, intent, baseDamage, 0, false);
    }

    public void setMove(String moveName, byte nextMove, MinionIntent intent, int baseDamage) {
        this.setMove(moveName, nextMove, intent, baseDamage, 0, false);
    }

    public void setMove(String moveName, byte nextMove, MinionIntent intent) {
        if (intent == MinionIntent.ATTACK || intent == MinionIntent.ATTACK_BUFF || intent == MinionIntent.ATTACK_DEFEND || intent == MinionIntent.ATTACK_DEBUFF) {
            for (int i = 0; i < 8; ++i) {
                AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(MathUtils.random((float) Settings.WIDTH * 0.25F, (float) Settings.WIDTH * 0.75F), MathUtils.random((float) Settings.HEIGHT * 0.25F, (float) Settings.HEIGHT * 0.75F), "PLAYER MINION MOVE " + moveName + " IS SET INCORRECTLY! REPORT TO DEV", Color.RED.cpy()));
            }
        }

        this.setMove(moveName, nextMove, intent, -1, 0, false);
    }

    public void setMove(byte nextMove, MinionIntent intent) {
        this.setMove((String) null, nextMove, intent, -1, 0, false);
    }

    public void rollMove() {
        this.getMove(AbstractDungeon.aiRng.random(99));
    }

    private Texture getIntentImg() {
        switch (this.intent) {
            case ATTACK:
                return this.getAttackIntent();
            case ATTACK_BUFF:
                return this.getAttackIntent();
            case ATTACK_DEBUFF:
                return this.getAttackIntent();
            case ATTACK_DEFEND:
                return this.getAttackIntent();
            case BUFF:
                return ImageMaster.INTENT_BUFF_L;
            case DEBUFF:
                return ImageMaster.INTENT_DEBUFF_L;
            case STRONG_DEBUFF:
                return ImageMaster.INTENT_DEBUFF2_L;
            case DEFEND:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_DEBUFF:
                return ImageMaster.INTENT_DEFEND_L;
            case DEFEND_BUFF:
                return ImageMaster.INTENT_DEFEND_BUFF_L;
            case ESCAPE:
                return ImageMaster.INTENT_ESCAPE_L;
            case MAGIC:
                return ImageMaster.INTENT_MAGIC_L;
            case SLEEP:
                return ImageMaster.INTENT_SLEEP_L;
            case STUN:
                return null;
            case UNKNOWN:
                return ImageMaster.INTENT_UNKNOWN_L;
            default:
                return ImageMaster.INTENT_UNKNOWN_L;
        }
    }

    private Texture getIntentBg() {
        switch (this.intent) {
            case ATTACK_DEFEND:
                return null;
            default:
                return null;
        }
    }

    protected Texture getAttackIntent(int dmg) {
        if (dmg < 5) {
            return ImageMaster.INTENT_ATK_1;
        } else if (dmg < 10) {
            return ImageMaster.INTENT_ATK_2;
        } else if (dmg < 15) {
            return ImageMaster.INTENT_ATK_3;
        } else if (dmg < 20) {
            return ImageMaster.INTENT_ATK_4;
        } else if (dmg < 25) {
            return ImageMaster.INTENT_ATK_5;
        } else {
            return dmg < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
        }
    }

    protected Texture getAttackIntent() {
        int tmp;
        if (this.isMultiDmg) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        }

        if (tmp < 5) {
            return ImageMaster.INTENT_ATK_1;
        } else if (tmp < 10) {
            return ImageMaster.INTENT_ATK_2;
        } else if (tmp < 15) {
            return ImageMaster.INTENT_ATK_3;
        } else if (tmp < 20) {
            return ImageMaster.INTENT_ATK_4;
        } else if (tmp < 25) {
            return ImageMaster.INTENT_ATK_5;
        } else {
            return tmp < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
        }
    }

    private Texture getAttackIntentTip() {
        int tmp;
        if (this.isMultiDmg) {
            tmp = this.intentDmg * this.intentMultiAmt;
        } else {
            tmp = this.intentDmg;
        }

        if (tmp < 5) {
            return ImageMaster.INTENT_ATK_TIP_1;
        } else if (tmp < 10) {
            return ImageMaster.INTENT_ATK_TIP_2;
        } else if (tmp < 15) {
            return ImageMaster.INTENT_ATK_TIP_3;
        } else if (tmp < 20) {
            return ImageMaster.INTENT_ATK_TIP_4;
        } else if (tmp < 25) {
            return ImageMaster.INTENT_ATK_TIP_5;
        } else {
            return tmp < 30 ? ImageMaster.INTENT_ATK_TIP_6 : ImageMaster.INTENT_ATK_TIP_7;
        }
    }

    public void damage(DamageInfo info) {
        if (info.output > 0 && this.hasPower("IntangiblePlayer")) {
            info.output = 1;
        }

        int damageAmount = info.output;
        if (!this.isDying && !this.isEscaping) {
            if (damageAmount < 0) {
                damageAmount = 0;
            }

            boolean hadBlock = true;
            if (this.currentBlock == 0) {
                hadBlock = false;
            }

            boolean weakenedToZero = damageAmount == 0;
            damageAmount = this.decrementBlock(info, damageAmount);
            // damageAmount = ((Shield)AddFields.shield.get(this)).decrementBlock(info, damageAmount, this);
            if (info.owner == AbstractDungeon.player) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    damageAmount = r.onAttackToChangeDamage(info, damageAmount);
                }
            }

            if (info.owner != null) {
                for (AbstractPower p : info.owner.powers) {
                    damageAmount = p.onAttackToChangeDamage(info, damageAmount);
                }
            }

            for (AbstractPower p : this.powers) {
                damageAmount = p.onAttackedToChangeDamage(info, damageAmount);
            }

            if (info.owner == AbstractDungeon.player) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onAttack(info, damageAmount, this);
                }
            }

            for (AbstractPower p : this.powers) {
                p.wasHPLost(info, damageAmount);
            }

            if (info.owner != null) {
                for (AbstractPower p : info.owner.powers) {
                    p.onAttack(info, damageAmount, this);
                }
            }

            for (AbstractPower p : this.powers) {
                damageAmount = p.onAttacked(info, damageAmount);
            }

            this.lastDamageTaken = Math.min(damageAmount, this.currentHealth);
            boolean probablyInstantKill = this.currentHealth == 0;
            if (damageAmount > 0) {
                if (info.owner != this) {
                    this.useStaggerAnimation();
                }

                if (damageAmount >= 99 && !CardCrawlGame.overkill) {
                    CardCrawlGame.overkill = true;
                }

                this.currentHealth -= damageAmount;
                if (!probablyInstantKill) {
                    AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, damageAmount));
                }

                if (this.currentHealth < 0) {
                    this.currentHealth = 0;
                }

                this.healthBarUpdatedEvent();
            } else if (!probablyInstantKill) {
                if (weakenedToZero && this.currentBlock == 0) {
                    if (hadBlock) {
                        AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                    } else {
                        AbstractDungeon.effectList.add(new StrikeEffect(this, this.hb.cX, this.hb.cY, 0));
                    }
                } else if (Settings.SHOW_DMG_BLOCK) {
                    AbstractDungeon.effectList.add(new BlockedWordEffect(this, this.hb.cX, this.hb.cY, TEXT[30]));
                }
            }

            if (this.currentHealth <= 0) {
                this.die();
                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.cleanCardQueue();
                    AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
                    AbstractDungeon.effectList.add(new DeckPoofEffect((float) Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
                    AbstractDungeon.overlayMenu.hideCombatPanels();
                }

                if (this.currentBlock > 0) {
                    this.loseBlock();
                    AbstractDungeon.effectList.add(new HbBlockBrokenEffect(this.hb.cX - this.hb.width / 2.0F + BLOCK_ICON_X, this.hb.cY - this.hb.height / 2.0F + BLOCK_ICON_Y));
                }
            }

        }
    }

    public void init() {
        this.refreshTargetMonster();
        this.rollMove();
        this.healthBarUpdatedEvent();
    }

    public void render(SpriteBatch sb) {
        if (!AbstractDungeon.player.isDeadOrEscaped()) {
            if (this.atlas == null) {
                sb.setColor(this.tint.color);
                if (this.img != null) {
                    sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
                }
            } else {
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
                sb.setBlendFunction(770, 771);
            }

            if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && !AbstractDungeon.player.isDead && this.intent != MinionIntent.NONE && !Settings.hideCombatElements) {
                this.renderIntentVfxBehind(sb);
                this.renderIntent(sb);
                this.renderIntentVfxAfter(sb);
                this.renderDamageRange(sb);
            }

            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
            this.renderHealth(sb);
            this.renderName(sb);
        }

    }

    protected void setHp(int minHp, int maxHp) {
        this.currentHealth = AbstractDungeon.monsterHpRng.random(minHp, maxHp);
        this.maxHealth = this.currentHealth;
    }

    protected void setHp(int hp) {
        this.setHp(hp, hp);
    }

    private void renderDamageRange(SpriteBatch sb) {
        if (this.intent.name().contains("ATTACK")) {
            if (this.isMultiDmg) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, this.intentDmg + "x" + this.intentMultiAmt, this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            } else {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentDmg), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            }
        }

    }

    private void renderIntentVfxBehind(SpriteBatch sb) {
        for (AbstractGameEffect e : this.intentVfx) {
            if (e.renderBehind) {
                e.render(sb);
            }
        }

    }

    private void renderIntentVfxAfter(SpriteBatch sb) {
        for (AbstractGameEffect e : this.intentVfx) {
            if (!e.renderBehind) {
                e.render(sb);
            }
        }

    }

    private void renderName(SpriteBatch sb) {
        if (!this.hb.hovered) {
            this.hoverTimer = MathHelper.fadeLerpSnap(this.hoverTimer, 0.0F);
        } else {
            this.hoverTimer += Gdx.graphics.getDeltaTime();
        }

        if ((!AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard == null || AbstractDungeon.player.hoveredCard.target == CardTarget.ENEMY) && !this.isDying) {
            if (this.hoverTimer != 0.0F) {
                if (this.hoverTimer * 2.0F > 1.0F) {
                    this.nameColor.a = 1.0F;
                } else {
                    this.nameColor.a = this.hoverTimer * 2.0F;
                }
            } else {
                this.nameColor.a = MathHelper.slowColorLerpSnap(this.nameColor.a, 0.0F);
            }

            float tmp = Interpolation.exp5Out.apply(1.5F, 2.0F, this.hoverTimer);
            this.nameColor.r = Interpolation.fade.apply(Color.DARK_GRAY.r, Settings.CREAM_COLOR.r, this.hoverTimer * 10.0F);
            this.nameColor.g = Interpolation.fade.apply(Color.DARK_GRAY.g, Settings.CREAM_COLOR.g, this.hoverTimer * 3.0F);
            this.nameColor.b = Interpolation.fade.apply(Color.DARK_GRAY.b, Settings.CREAM_COLOR.b, this.hoverTimer * 3.0F);
            float y = Interpolation.exp10Out.apply(this.healthHb.cY, this.healthHb.cY - 8.0F * Settings.scale, this.nameColor.a);
            float x = this.hb.cX - this.animX;
            this.nameBgColor.a = this.nameColor.a / 2.0F * this.hbAlpha;
            sb.setColor(this.nameBgColor);
            TextureAtlas.AtlasRegion img = ImageMaster.MOVE_NAME_BG;
            sb.draw(img, x - (float) img.packedWidth / 2.0F, y - (float) img.packedHeight / 2.0F, (float) img.packedWidth / 2.0F, (float) img.packedHeight / 2.0F, (float) img.packedWidth, (float) img.packedHeight, Settings.scale * tmp, Settings.scale * 2.0F, 0.0F);
            Color var10000 = this.nameColor;
            var10000.a *= this.hbAlpha;
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, this.name, x, y, this.nameColor);
        }

    }

    private void renderIntent(SpriteBatch sb) {
        this.intentColor.a = this.intentAlpha;
        sb.setColor(this.intentColor);
        if (this.intentBg != null) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.intentAlpha / 2.0F));
            if (Settings.isMobile) {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, 0.0F, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            }
        }

        if (this.intentImg != null && this.intent != MinionIntent.UNKNOWN && this.intent != MinionIntent.STUN) {
            if (this.intent != MinionIntent.DEBUFF && this.intent != MinionIntent.STRONG_DEBUFF) {
                this.intentAngle = 0.0F;
            } else {
                this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
            }

            sb.setColor(this.intentColor);
            if (Settings.isMobile) {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, this.intentAngle, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, this.intentAngle, 0, 0, 128, 128, false, false);
            }
        }

    }

    protected void updateHitbox(float hb_x, float hb_y, float hb_w, float hb_h) {
        this.hb_w = hb_w * Settings.scale;
        this.hb_h = hb_h * Settings.xScale;
        this.hb_y = hb_y * Settings.scale;
        this.hb_x = hb_x * Settings.scale;
        this.hb = new Hitbox(this.hb_w, this.hb_h);
        this.hb.move(this.drawX + this.hb_x + this.animX, this.drawY + this.hb_y + this.hb_h / 2.0F);
        this.healthHb.move(this.hb.cX, this.hb.cY - this.hb_h / 2.0F - this.healthHb.height / 2.0F);
        this.intentHb.move(this.hb.cX + this.intentOffsetX, this.hb.cY + this.hb_h / 2.0F + 32.0F * Settings.scale);
    }

    public void dispose() {
        if (this.img != null) {
            this.img.dispose();
            this.img = null;
        }

        if (this.atlas != null) {
            this.atlas.dispose();
            this.atlas = null;
        }

    }

    public void die() {
        this.die(true);
    }

    public void die(boolean triggerRelics) {
        if (!this.isDying) {
            this.isDying = true;
            /*if (this.currentHealth <= 0 && triggerRelics) {
                for(AbstractPower power : AbstractDungeon.player.powers) {
                    if (power instanceof AbstractShionPower) {
                        ((AbstractShionPower)power).onPlayerMinionDeath();
                    }
                }
            }*/

            if (this.currentHealth < 0) {
                this.currentHealth = 0;
            }

            if (!Settings.FAST_MODE) {
                ++this.deathTimer;
            } else {
                ++this.deathTimer;
            }
        }

    }

    public void usePreBattleAction() {
    }

    public void useUniversalPreBattleAction() {
    }

    private DamageInfo applyPowerElf(DamageInfo info, AbstractCreature owner, AbstractCreature target) {
        info.output = info.base;
        info.isModified = false;
        float tmp = (float) info.output;

        for (AbstractPower p : owner.powers) {
            tmp = p.atDamageGive(tmp, info.type);
            if (info.base != (int) tmp) {
                info.isModified = true;
            }
        }

        /*if (AbstractDungeon.player.hasPower(SpiritCloisterPower.POWER_ID)) {
            for(AbstractPower p : AbstractDungeon.player.powers) {
                if (p.ID.equals("Strength")) {
                    tmp = p.atDamageGive(tmp, DamageType.NORMAL);
                    if (info.base != (int)tmp) {
                        info.isModified = true;
                    }
                }
            }
        }*/

        if (target != null) {
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageReceive(tmp, info.type);
                if (info.base != (int) tmp) {
                    info.isModified = true;
                }
            }
        }

        for (AbstractPower p : owner.powers) {
            tmp = p.atDamageFinalGive(tmp, info.type);
            if (info.base != (int) tmp) {
                info.isModified = true;
            }
        }

        if (target != null) {
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageFinalReceive(tmp, info.type);
                if (info.base != (int) tmp) {
                    info.isModified = true;
                }
            }
        }

        info.output = MathUtils.floor(tmp);
        if (info.output < 0) {
            info.output = 0;
        }

        return info;
    }

    private void calculateDamage(int dmg) {
        AbstractCreature target = this.targetMonster;
        float tmp = (float) dmg;

        for (AbstractPower p : this.powers) {
            tmp = p.atDamageGive(tmp, DamageType.NORMAL);
        }

        /*if (AbstractDungeon.player.hasPower(SpiritCloisterPower.POWER_ID)) {
            for(AbstractPower p : AbstractDungeon.player.powers) {
                if (p.ID.equals("Strength")) {
                    tmp = p.atDamageGive(tmp, DamageType.NORMAL);
                }
            }
        }*/

        if (target != null) {
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageReceive(tmp, DamageType.NORMAL);
            }
        }

        for (AbstractPower p : this.powers) {
            tmp = p.atDamageFinalGive(tmp, DamageType.NORMAL);
        }

        if (target != null) {
            for (AbstractPower p : target.powers) {
                tmp = p.atDamageFinalReceive(tmp, DamageType.NORMAL);
            }
        }

        dmg = MathUtils.floor(tmp);
        if (dmg < 0) {
            dmg = 0;
        }

        this.intentDmg = dmg;
    }

    public boolean isDeadOrEscaped() {
        if (!this.isDying && !this.halfDead) {
            return !this.isPlayer ? this.isEscaping : false;
        } else {
            return true;
        }
    }

    public void applyPowers() {
        for (DamageInfo dmg : this.damage) {
            if (this.targetMonster != null) {
                /*if (this.id.equals(ElfMinion.ID)) {
                    DamageInfo info = this.applyPowerElf(dmg, this, this.targetMonster);
                    dmg.output = info.output;
                    dmg.isModified = info.isModified;
                } else {*/
                dmg.applyPowers(this, this.targetMonster);
                // }
            } else {
                dmg.output = dmg.base;
            }
        }

        if (this.move.baseDamage > -1) {
            this.calculateDamage(this.move.baseDamage);
        }

        this.intentImg = this.getIntentImg();
        this.updateIntentTip();
    }

    public void changeState(String stateName) {
    }

    public void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public int getIntentDmg() {
        return this.intentDmg;
    }

    public abstract void takeTurn();

    protected abstract void getMove(int var1);

    public void onMonsterDeath() {
        this.refreshTargetMonster();
    }

    public void onSpawnMonster() {
        this.refreshTargetMonster();
    }

    public void refreshTargetMonster() {
        this.targetMonster = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.miscRng);
    }

    public AbstractCreature getTargetMonster() {
        return this.targetMonster;
    }

    static {
        uiStrings = null;//CardCrawlGame.languagePack.getUIString(HSRMod.makePath(class.getSimpleName()));
        TEXT = null; // uiStrings.TEXT;
        INTENT_HB_W = 64.0F * Settings.scale;
    }

    public static enum MinionIntent {
        ATTACK,
        ATTACK_BUFF,
        ATTACK_DEBUFF,
        ATTACK_DEFEND,
        BUFF,
        DEBUFF,
        STRONG_DEBUFF,
        DEBUG,
        DEFEND,
        DEFEND_DEBUFF,
        DEFEND_BUFF,
        ESCAPE,
        MAGIC,
        NONE,
        SLEEP,
        STUN,
        UNKNOWN;

        private MinionIntent() {
        }
    }

    public static enum MinionType {
        Elf,
        None;

        private MinionType() {
        }
    }
}

