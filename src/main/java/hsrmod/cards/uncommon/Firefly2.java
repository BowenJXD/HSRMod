package hsrmod.cards.uncommon;

import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.uniqueBuffs.FireflyPower;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;

public class Firefly2 extends BaseCard {
    public static final String ID = Firefly2.class.getSimpleName();
    
    int energyGain = 120;
    
    public Firefly2() {
        super(ID);
        setBaseEnergyCost(240);
        tags.add(CustomEnums.ENERGY_COSTING);
        cardsToPreview = new Firefly1();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        
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
}
