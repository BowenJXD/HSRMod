package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;

public class Burst extends BaseCard {
    public static final String ID = Burst.class.getSimpleName();

    public Burst() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int dmg = damage;
        if (m.currentBlock > 0) {
            dmg *= 2;
        }
        boolean hasBlock = m.currentBlock > 0;
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this, dmg),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                c -> {
                    if (hasBlock && c.currentBlock <= 0) {
                        addToBot(new GainBlockAction(p, p, damage));
                    }
                }));
    }
}
