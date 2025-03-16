package hsrmod.monsters.Exordium;

import basemod.BaseMod;
import basemod.interfaces.OnPowersModifiedSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BossCrystalImpactEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import hsrmod.misc.Encounter;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.breaks.ImprisonPower;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SnarelockPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class AntimatterEngine extends BaseMonster implements OnPowersModifiedSubscriber {
    public static final String ID = AntimatterEngine.class.getSimpleName();

    AbstractMonster leftHand;
    AbstractMonster rightHand;
    
    int powerCount = 0;
    int handMove = 0;
    int ultCount = 1;
    
    public AntimatterEngine() {
        super(ID, 25F, 90F, 160, 160, -50, 0);
        setDamages(3);
        if (ModHelper.specialAscension(type)) {
            handMove = 2;
        }
        bgm = Encounter.DESTRUCTIONS_BEGINNING;
        ultCount = ModHelper.moreDamageAscension(type) ? 3 : 1;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ModHelper.specialAscension(type)) {
            addToBot(new ApplyPowerAction(p, this, new SnarelockPower(p, this, 0), 0));
        }
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                break;
            case 1:
                if (leftHand != null) {
                    addToBot(new ApplyPowerAction(leftHand, this, new StrengthPower(leftHand, 1), 1));
                }
                break;
            case 2:
                if (rightHand != null) {
                    addToBot(new ApplyPowerAction(rightHand, this, new StrengthPower(rightHand, 1), 1));
                }
                break;
            case 3:
                if (leftHand != null) {
                    addToBot(new ApplyPowerAction(leftHand, this, new StrengthPower(leftHand, 1), 1));
                }
                if (rightHand != null) {
                    addToBot(new ApplyPowerAction(rightHand, this, new StrengthPower(rightHand, 1), 1));
                }
                break;
            case 4:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, String.format(MOVES[6], ultCount), 1), 1));
                break;
            case 5:
                if (hasPower(ChargingPower.POWER_ID)) {
                    int count = ultCount + powerCount;
                    AbstractDungeon.effectsQueue.add(new LaserBeamEffect(hb.cX, hb.cY));
                    addDamageActions(p, 0, count, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    BaseMod.unsubscribe(this);
                }
                break;
        }
        if (!p.hasPower(SnarelockPower.POWER_ID)) {
            addToBot(new VFXAction(new BossCrystalImpactEffect(p.hb.cX, p.hb.cY)));
            addToBot(new ApplyPowerAction(p, this, new SnarelockPower(p, this, 0), 0));
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (leftHand == null) {
            leftHand = AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof DawnsLeftHand && ModHelper.check(m)).findFirst().orElse(null);
        }
        if (rightHand == null) {
            rightHand = AbstractDungeon.getMonsters().monsters.stream().filter(m -> m instanceof DisastersRightHand && ModHelper.check(m)).findFirst().orElse(null);
        }
        int count = ModHelper.getPowerCount(p, ImprisonPower.POWER_ID) + ModHelper.getPowerCount(p, EntanglePower.POWER_ID);
        if (lastMove((byte) 4)) {
            BaseMod.subscribe(this);
            setMove(MOVES[5], (byte) 5, Intent.ATTACK, damage.get(0).base, ultCount + count, true);
        } else if (!p.hasPower(SnarelockPower.POWER_ID) && handMove == 0) {
            setMove(MOVES[0], (byte) 0, Intent.DEBUFF);
        } else if (count >= 5 || (leftHand == null && rightHand == null && ModHelper.specialAscension(type))) {
            setMove(MOVES[4], (byte) 4, Intent.UNKNOWN);
        } else {
            switch (handMove) {
                case 0:
                    setMove(MOVES[1], (byte) 1, Intent.BUFF);
                    break;
                case 1:
                    setMove(MOVES[2], (byte) 2, Intent.BUFF);
                    break;
                case 2:
                    setMove(MOVES[3], (byte) 3, Intent.BUFF);
                    break;
            }
            handMove = (handMove + 1) % 3;
        }
    }

    @Override
    public void die() {
        if (ModHelper.check(leftHand) || ModHelper.check(rightHand)) {
            ModHelper.addToBotAbstract(() -> {
                if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> !(m instanceof AntimatterEngine) && ModHelper.check(m))) {
                    super.die();
                    onBossVictoryLogic();
                }
            });
            return;
        }
        super.die();
        onBossVictoryLogic();
    }

    @Override
    public void receivePowersModified() {
        if (SubscriptionManager.checkSubscriber(this)) {
            int count = ModHelper.getPowerCount(p, ImprisonPower.POWER_ID) + ModHelper.getPowerCount(p, EntanglePower.POWER_ID);
            if (powerCount != count) {
                powerCount = count;
                if (hasPower(ChargingPower.POWER_ID)) {
                    setMove(MOVES[5], (byte) 5, Intent.ATTACK, damage.get(0).base, ultCount + powerCount, true);
                    createIntent();
                }
            }
        }
    }
}
