package hsrmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.powers.EnergyPower;

public class Tingyun1 extends BaseCard {
    public static final String ID = Tingyun1.class.getSimpleName();

    public Tingyun1() {
        super(ID);
        energyCost = 10;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new EnergyPower(p, magicNumber),
                        magicNumber
                )
        );
        
    }
}
