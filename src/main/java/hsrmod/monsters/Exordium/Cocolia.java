package hsrmod.monsters.Exordium;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.GlowRelicParticle;
import com.megacrit.cardcrawl.vfx.RarePotionParticleEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import com.megacrit.cardcrawl.vfx.scene.LightFlareMEffect;
import com.megacrit.cardcrawl.vfx.scene.LightFlareSEffect;
import hsrmod.actions.ForceWaitAction;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.utils.ModHelper;

public class Cocolia extends BaseMonster {
    public static final String ID = Cocolia.class.getSimpleName();

    public Cocolia() {
        super(ID, 400.0F, 512.0F, -100.0F, 0.0F);
        floatIndex = -1;

        if (ModHelper.moreDamageAscension(type)) {
            setDamages(6, 6, 12, 6);
        } else {
            setDamages(6, 6, 9, 6);
        }
        bgm = Encounter.END_OF_THE_ETERNAL_FREEZE + "_1";
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster lance1 = AbstractDungeon.getMonsters().getMonster(HSRMod.makePath(LanceOfTheEternalFreeze.ID));
        AbstractMonster lance2 = AbstractDungeon.getMonsters().monsters.stream().filter(m -> m.id.equals(HSRMod.makePath(LanceOfTheEternalFreeze.ID)) && m != lance1).findFirst().orElse(null);

        int r;
        switch (this.nextMove) {
            case 1:
                r = AbstractDungeon.miscRng.random(1);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                if (lance1 == null || lance1.isDead) {
                    spawnLance1();
                }
                if (lance2 == null || lance2.isDead) {
                    spawnLance2();
                }
                break;
            case 2:
                r = AbstractDungeon.miscRng.random(2, 3);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true), 1));
                break;
            case 3:
                r = AbstractDungeon.miscRng.random(2, 3);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                addToBot(new VFXAction(new BlizzardEffect(6, true), 0.5f));
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new MakeTempCardInDrawPileAction(new Frozen(), 1, true, true));
                break;
            case 4:
                r = AbstractDungeon.miscRng.random(2, 3);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ExhaustAction(1, true, false, false));
                break;
            case 5:
                r = AbstractDungeon.miscRng.random(4, 5);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[6], 1), 1));
                break;
            case 6:
                if (hasPower(ChargingPower.POWER_ID)) {
                    r = AbstractDungeon.miscRng.random(6, 7);
                    addToBot(new ShoutAction(this, DIALOG[r]));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                    addToBot(new VFXAction(
                            new VfxBuilder(new Texture(PathDefine.EFFECT_PATH + "CocoliaMeteor.png"),
                                    hb.cX, hb.cY + hb.height/2, Settings.FAST_MODE ? 0.5f : 1f)
                                    .fadeIn(0.1f)
                                    .wobble(1f, 1f, 10f)
                                    .scale(0.6f, 1f, VfxBuilder.Interpolations.POW3OUT)
                                    .andThen(Settings.FAST_MODE ? 1.5f : 2f)
                                    .scale(1f, 1.2f, VfxBuilder.Interpolations.POW3OUT_INVERSE)
                                    .moveX(hb.cX, p.hb.cX, VfxBuilder.Interpolations.EXP5IN)
                                    .moveY(hb.cY + hb.height/2, p.hb.cY, VfxBuilder.Interpolations.EXP5IN)
                                    .wobble(1f, 1f, 10f)
                                    .emitEvery(RarePotionParticleEffect::new, 0.02f)
                                    .fadeOut(0.1f)
                                    .triggerVfxAt(Settings.FAST_MODE ? 1.5f : 2f, 1, ExplosionSmallEffect::new)
                                    .build(), Settings.FAST_MODE ? 2f : 3f));
                    CardCrawlGame.music.dispose();
                    CardCrawlGame.music.playTempBGM(Encounter.END_OF_THE_ETERNAL_FREEZE + "_2");

                    int handCount = p.hand.size();
                    addToBot(new ExhaustAction(handCount, true, false, false));
                    for (int i = 0; i < handCount - 1; i++) {
                        addToBot(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        addToBot(new ForceWaitAction(0.1f));
                    }
                    addToBot(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        int handCount = AbstractDungeon.player.hand.size();
        int lanceCount = AbstractDungeon.getMonsters().monsters.stream()
                .mapToInt(m -> m.id.equals(HSRMod.makePath(LanceOfTheEternalFreeze.ID)) && !m.isDead ? 1 : 0).sum();
        if (AbstractDungeon.getMonsters().monsters.size() == 1) {
            lanceCount = -1;
        }
        if (this.lastMove((byte) 5)) {
            this.setMove(MOVES[5], (byte) 6, AbstractMonster.Intent.ATTACK, this.damage.get(3).base);
        } else if (AbstractDungeon.player.hand.group.stream().mapToInt(c -> c instanceof Frozen ? 1 : 0).sum() >= 3) {
            this.setMove(MOVES[4], (byte) 5, AbstractMonster.Intent.UNKNOWN);
        } else if (i < handCount * 10) {
            this.setMove(MOVES[3], (byte) 4, Intent.ATTACK, this.damage.get(2).base);
        } else if (i > (lanceCount + 1) * 33 && !lastMove((byte) 1)) {
            this.setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.UNKNOWN);
        } else if (i % 2 == 0) {
            this.setMove(MOVES[1], (byte) 2, AbstractMonster.Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            this.setMove(MOVES[2], (byte) 3, AbstractMonster.Intent.ATTACK, this.damage.get(1).base);
        }
    }

    void spawnLance1() {
        AbstractMonster lance1 = new LanceOfTheEternalFreeze(-450.0F, 0.0F, 1);
        lance1.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(lance1, false));
    }

    void spawnLance2() {
        AbstractMonster lance2 = new LanceOfTheEternalFreeze(250.0F, 0.0F, 2);
        lance2.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(lance2, false));
    }

    int playerHandCount = 0;

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hand != null
                && !AbstractDungeon.actionManager.turnHasEnded
                && !AbstractDungeon.isScreenUp
                && playerHandCount != AbstractDungeon.player.hand.size()
                && hasPower(ChargingPower.POWER_ID)) {
            playerHandCount = AbstractDungeon.player.hand.size();
            this.setMove(MOVES[5], (byte) 6, Intent.ATTACK, this.damage.get(3).base, playerHandCount, true);
            this.createIntent();
        }
    }

    @Override
    public void die() {
        super.die();
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + 8, 3.0F));

        ModHelper.killAllMinions();
        onBossVictoryLogic();
    }
}
