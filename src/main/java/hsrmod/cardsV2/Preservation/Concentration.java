package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;

public class Concentration extends BaseCard {
    public static final String ID = Concentration.class.getSimpleName();

    public Concentration() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        boolean hasBlock = m.currentBlock > 0;
        if (hasBlock) {
            addToBot(new GainBlockAction(p, p, damage));
        }
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                c -> {
                    if (hasBlock && c.currentBlock <= 0) {
                        addToBot(new ElementalDamageAction(c, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                })
        );
    }
}