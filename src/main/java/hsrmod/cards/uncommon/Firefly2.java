package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.uniqueBuffs.FireflyPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

public class Firefly2 extends BaseCard {
    public static final String ID = Firefly2.class.getSimpleName();
    
    static UnlockSub unlockSub;
    
    public Firefly2() {
        super(ID);
        setBaseEnergyCost(240);
        tags.add(CustomEnums.ENERGY_COSTING);
        cardsToPreview = new Firefly1();
        if(unlockSub != null) {
            BaseMod.unsubscribe(unlockSub);
            unlockSub = null;
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        if ((p.currentHealth + TempHPField.tempHp.get(p) <= magicNumber && !p.hasPower(IntangiblePlayerPower.POWER_ID)) 
                || (p.currentHealth <= 1 )) {
            if (unlockSub == null && !SignatureHelper.isUnlocked(cardID)) {
                unlockSub = new UnlockSub();
                BaseMod.subscribe(unlockSub);
            }
        }

        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Firefly"));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        addToBot(new VFXAction(new WeightyImpactEffect(m.hb.cX, m.hb.cY, Color.GOLD), 1f));
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.FIRE,
                (ci) -> {
                    if (ci.didBreak) {
                        addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, EnergyPower.AMOUNT_LIMIT), EnergyPower.AMOUNT_LIMIT));
                        int energyNum = AbstractDungeon.player.energy.energy - EnergyPanel.getCurrentEnergy();
                        if (energyNum > 0) addToBot(new GainEnergyAction(energyNum));
                        addToBot(new BreakDamageAction(ci.target, new DamageInfo(p, tr)));
                        Firefly1 card = new Firefly1();
                        if (upgraded) card.upgrade();
                        addToBot(new MakeTempCardInHandAction(card));
                    }
                })
        );
    }

    public static class UnlockSub implements PostBattleSubscriber {

        @Override
        public void receivePostBattle(AbstractRoom abstractRoom) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
            BaseMod.unsubscribeLater(unlockSub);
            unlockSub = null;
        }
    }
}
