package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.cards.BaseCard;
import hsrmod.powers.onlyDebuffs.ThanatoplumRebloomPower;

import java.util.Iterator;

public class RuanMei2 extends BaseCard {
    public static final String ID = RuanMei2.class.getSimpleName();

    public RuanMei2() {
        super(ID);
        energyCost = 130;
        selfRetain = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        Iterator<AbstractMonster> var3 = AbstractDungeon.getMonsters().monsters.iterator();
        
        while (var3.hasNext()) {
            AbstractMonster mo = var3.next();
            addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, magicNumber, false), magicNumber));
            addToBot(new ApplyPowerAction(mo, p, new ThanatoplumRebloomPower(mo, 1), 1));
        }
    }
}
