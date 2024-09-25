package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;

public class Concentration extends BaseCard {
    public static final String ID = Concentration.class.getSimpleName();

    public Concentration() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        boolean hasBlock = m.currentBlock > 0;
        if (hasBlock){
            addToBot(new GainBlockAction(p, p, damage));
        }
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), elementType, 2,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL, c -> {
            if (hasBlock && c.currentBlock <= 0) {
                addToBot(new ElementalDamageAction(c, new DamageInfo(p, damage, damageTypeForTurn), elementType, 2));
            }
        }));
    }
}
