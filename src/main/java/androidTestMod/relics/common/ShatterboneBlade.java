package androidTestMod.relics.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import androidTestMod.relics.BaseRelic;

public class ShatterboneBlade extends BaseRelic {
    public static final String ID = ShatterboneBlade.class.getSimpleName();

    public ShatterboneBlade() {
        super(ID);
    }
    
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount == 0) return damageAmount;
        if (AbstractDungeon.actNum <= 2) return damageAmount + magicNumber;
        else return damageAmount - magicNumber;
    }
}
