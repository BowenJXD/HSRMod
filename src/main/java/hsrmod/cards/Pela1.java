package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.colorless.Blind;
import com.megacrit.cardcrawl.cards.green.BouncingFlask;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.modcore.ElementType;

import java.util.Iterator;

public class Pela1 extends BaseCard{
    public static final String ID = Pela1.class.getSimpleName();
    
    public Pela1() {
        super(ID);
        energyCost = 100;
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ElementalDamageAllAction(
                        damage,
                        ElementType.Ice,
                        2,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                        mo -> {
                            AbstractDungeon.actionManager.addToBottom(
                                    new ApplyPowerAction(mo, p, new WeakPower(p, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE)
                            );
                        }
                )
        );
    }
}
