package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.uniqueBuffs.FireflyPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

public class Firefly2 extends BaseCard {
    public static final String ID = Firefly2.class.getSimpleName();
    
    int energyGain = 120;
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
        if ((p.currentHealth <= magicNumber && !p.hasPower(IntangiblePlayerPower.POWER_ID)) 
                || (p.currentHealth <= 1 )) {
            if (unlockSub == null && !SignatureHelper.isUnlocked(cardID)) {
                unlockSub = new UnlockSub();
                BaseMod.subscribe(unlockSub);
            }
        }
        
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Firefly"));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                CardCrawlGame.sound.play(ID);
            }
        });
        addToBot(new VFXAction(new OfferingEffect()));
        
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        addToBot(new ApplyPowerAction(p, p, new FireflyPower()));
    }

    @Override
    public void triggerWhenDrawn() {
        if (AbstractDungeon.player.hasPower(EnergyPower.POWER_ID)) {
            int amt = AbstractDungeon.player.getPower(EnergyPower.POWER_ID).amount;
            if (amt < energyGain) {
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                        new EnergyPower(AbstractDungeon.player, energyGain - amt), energyGain - amt));
            }
        }
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
