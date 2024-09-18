package hsrmod.cards.base;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;

public class March7th0 extends BaseCard {
    public static final String ID = March7th0.class.getSimpleName();
    
    public March7th0() {
        super(ID);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CustomEnums.REVIVE);
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
                        ElementType.Ice,
                        magicNumber,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
    }

}