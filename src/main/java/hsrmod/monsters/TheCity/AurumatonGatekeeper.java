package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.effects.LinearSweepingBeamEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SanctionRatePower;
import hsrmod.utils.ModHelper;

public class AurumatonGatekeeper extends BaseMonster {
    public static final String ID = AurumatonGatekeeper.class.getSimpleName();

    public AurumatonGatekeeper(float x, float y) {
        super(ID, 300, 384, x, y);

        if (ModHelper.moreDamageAscension(type))
            setDamages(6, 15, 7);
        else
            setDamages(6, 12, 6);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SanctionRatePower(this, 0), SanctionRatePower.stackCount));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                // addToBot(new VFXAction(new SmallLaserEffect(this.hb.cX, this.hb.cY + 100.0F * Settings.scale, p.hb.cX, p.hb.cY), 0.5F));
                int r = AbstractDungeon.miscRng.random(1);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                spawnDragonfishes();
                break;
            case 2:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new VFXAction(new LinearSweepingBeamEffect(hb.cX, hb.cY, p.hb.cX, p.hb.cY - p.hb.height/2, true), 0.5f));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true), 1));
                addToBot(new ApplyPowerAction(this, this, new SanctionRatePower(this, SanctionRatePower.stackCount), SanctionRatePower.stackCount));
                break;
            case 3:
                addToBot(new AnimateFastAttackAction(this));
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                if (ModHelper.specialAscension(type)) {
                    addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true), 1));
                }
                break;
            case 4:
                if (hasPower(ChargingPower.POWER_ID)) {
                    addToBot(new AnimateFastAttackAction(this));
                    for (int i = 0; i < 3; i++) {
                        addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }
                    addToBot(new MakeTempCardInDrawPileAction(new Imprison(), 1, true, true));
                    addToBot(new ApplyPowerAction(this, this, new SanctionRatePower(this, 0), -SanctionRatePower.stackLimit));
                    turnCount = 0;
                }
                break;
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        AbstractPower sanctionPower = getPower(SanctionRatePower.POWER_ID);
        int sanctionRate = sanctionPower != null ? sanctionPower.amount : 0;
        if (sanctionRate < SanctionRatePower.stackLimit) {
            setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            switch (turnCount % 3) {
                case 0:
                    if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(c -> c instanceof IlluminationDragonfish && !c.isDead)) {
                        setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.UNKNOWN);
                        break;
                    } else {
                        turnCount++;
                    }
                case 1:
                    setMove(MOVES[2], (byte) 3, ModHelper.specialAscension(type) ? Intent.ATTACK_DEBUFF : Intent.ATTACK, this.damage.get(1).base);
                    break;
                case 2:
                    setMove(MOVES[3], (byte) 4, Intent.ATTACK, this.damage.get(2).base, 3, true);
                    break;
            }
            turnCount++;
        }
    }

    void spawnDragonfishes() {
        IlluminationDragonfish fish1 = new IlluminationDragonfish(-450, 0);
        addToBot(new SpawnMonsterAction(fish1, true));
        ModHelper.addToBotAbstract(fish1::usePreBattleAction);
        IlluminationDragonfish fish2 = new IlluminationDragonfish(150, 0);
        addToBot(new SpawnMonsterAction(fish2, true));
        ModHelper.addToBotAbstract(fish2::usePreBattleAction);
    }

    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
