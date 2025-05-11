package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.misc.LockToughnessPower;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class AventurineOfStratagems extends BaseMonster {
    public static final String ID = AventurineOfStratagems.class.getSimpleName();
    
    int chargeLoss = 80;
    boolean goldGiven = false;
    
    public AventurineOfStratagems(){
        super(ID, 300, 410, 100, 0);
        bgm = "Hell Is Preferable to Nihility";
        floatIndex = AbstractDungeon.miscRng.randomBoolean() ? 1 : -1;
        
        chargeLoss = specialAs ? 150 : 100;
        addSlot(-200, 100);
        monFunc = slot -> new AllOrNothing(slot.x, slot.y);
        
        addMove(Intent.ATTACK, moreDamageAs ? 5 : 4, 5, mi->{
            shoutIf(1);
            addToBot(new AnimateSlowAttackAction(this));
            for (int i = 0; i < mi.damageTimeSupplier.get(); i++) {
                if (i == 0)
                    addToBot(new VFXAction(new FlickCoinEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY), 0.5f));
                else
                    addToBot(new VFXAction(new FlickCoinEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY), 0.5f));
                addToBot(new DamageAction(p, this.damage.get(mi.index), 5));
            }
        });
        addMove(Intent.ATTACK_DEBUFF, 30, mi->{
            shout(2, 3);
            addToBot(new AnimateSlowAttackAction(this));
            addToBot(new VFXAction(new FlickCoinEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY), 0.5f));
            addToBot(new VFXAction(new ClashEffect(p.hb.cX, p.hb.cY)));
            addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, -chargeLoss), -chargeLoss));
        });
        addMove(Intent.UNKNOWN, mi->{
            addToBot(new VFXAction(new SpotlightEffect()));
            if (!goldGiven) {
                goldGiven = true;
                RelicEventHelper.gainGold(100);
            }
            shout(4, 5);
            spawnMonsters();
            addToBot(new ApplyPowerAction(this, this, new IntangiblePower(this, 7), 7));
            addToBot(new ApplyPowerAction(this, this, new LockToughnessPower(this)));
            addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
        });
        addMoveA(Intent.ATTACK_DEBUFF, 50, mi->{
            if (hasPower(ChargingPower.POWER_ID)) {
                // addToBot(new VFXAction(new RainingGoldEffect(100, true)));
                shout(6);
                addToBot(new VFXAction(new WeightyImpactEffect(p.hb.cX, p.hb.cY), 1));
                addToBot(new DamageAction(p, this.damage.get(mi.index), 50));
                addToBot(new MakeTempCardInDrawPileAction(new Imprison(), 1, true, true));
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, getLastMove())));
            }
            turnCount = 0;
        });
        
        turnCount = specialAs ? 2 : 1;
    }
    
    int goldCache = 0;

    @Override
    public void update() {
        super.update();
        if (p != null && goldCache != p.gold && hasPower(ChargingPower.POWER_ID)) {
            goldCache = p.gold;
            damage.get(3).base = Math.max(0, 50 - p.gold / 100);
            setMove(3);
            ModHelper.addToTopAbstract(this::createIntent);
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + 0, 2f));
    }

    @Override
    protected void getMove(int i) {
        turnCount++;
        if (hasPower(ChargingPower.POWER_ID) && AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> ModHelper.check(m) && m instanceof AllOrNothing)) {
            setMove(3);
        } else if (turnCount == 3) {
            setMove(2);
        } else if (turnCount == 2) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void die() {
        super.die();
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(this.getClass().getSimpleName() + "_" + 7, 2f));
    }
}
