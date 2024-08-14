package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.powers.EnergyCounter;

public class Trailblazer1 extends BaseCard {
    public static final String ID = Trailblazer1.class.getSimpleName();
    
    public Trailblazer1() {
        super(ID);
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {    
        AbstractDungeon.actionManager.addToBottom(
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