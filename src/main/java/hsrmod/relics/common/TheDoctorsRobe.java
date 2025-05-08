package hsrmod.relics.common;

import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TheDoctorsRobe extends BaseRelic {
    public static final String ID = TheDoctorsRobe.class.getSimpleName();
    
    public TheDoctorsRobe() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
