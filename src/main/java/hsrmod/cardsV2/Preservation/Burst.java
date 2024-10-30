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
        ElementalDamageInfo info = new ElementalDamageInfo(this);
        if (m.currentBlock > 0) {
            info.output *= 2;
            info.tr *= 2;
        }
        boolean hasBlock = m.currentBlock > 0;
        addToBot(new ElementalDamageAction(
                m,
                info,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                c -> {
                    if (hasBlock && c.currentBlock <= 0) {
                        addToBot(new GainBlockAction(p, p, damage));
                    }
                }));
    }
}
