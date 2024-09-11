package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.powers.uniqueBuffs.FireflyPower;

public class Firefly2 extends BaseCard {
    public static final String ID = Firefly2.class.getSimpleName();
    
    int energyGain = 120;
    
    public Firefly2() {
        super(ID);
        energyCost = 240;
        
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new TalkAction(true, "直至一切，燃烧殆尽！", 1.0F, 2.0F));
        addToBot(new ApplyPowerAction(p, p, new FireflyPower(), 1));
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
