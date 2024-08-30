package hsrmod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;

public class Himeko0 extends BaseCard {
    public static final String ID = Himeko0.class.getSimpleName();
    
    public Himeko0() {
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
                        ElementType.Fire,
                        2,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.FIRE
                )
        );
    }

}