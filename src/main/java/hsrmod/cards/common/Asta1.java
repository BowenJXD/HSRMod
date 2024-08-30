package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.WindShearPower;

import java.util.ArrayList;
import java.util.List;

public class Asta1 extends BaseCard {
    public static final String ID = Asta1.class.getSimpleName();
    
    List<AbstractCreature> monsters;
    
    public Asta1() {
        super(ID);
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        monsters = new ArrayList<>();
        ElementalDamageAction elementalDamageAction = new ElementalDamageAction(m, new DamageInfo(p, this.damage, 
                DamageInfo.DamageType.NORMAL), ElementType.Fire, 1, AbstractGameAction.AttackEffect.FIRE,
                c -> {
                    if (!c.isDeadOrEscaped() && !monsters.contains(c)) {
                        monsters.add(c);
                        addToBot(
                                new ApplyPowerAction(
                                        p,
                                        p,
                                        new StrengthPower(
                                                p,
                                                1
                                        ),
                                        1
                                )
                        );
                    }
                }
                );
        this.addToBot(new BouncingAction(m, magicNumber, elementalDamageAction));
    }
}
