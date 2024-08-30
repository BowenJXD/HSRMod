package hsrmod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

public class Trailblazer1 extends BaseCard {
    public static final String ID = Trailblazer1.class.getSimpleName();
    
    public Trailblazer1() {
        super(ID);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {    
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        ElementType.Physical,
                        2,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_HEAVY
                )
        );
    }

}