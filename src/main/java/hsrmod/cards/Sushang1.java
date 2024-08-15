package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.powers.BreakEffectPower;
import hsrmod.powers.BrokenPower;

public class Sushang1 extends BaseCard {
    public static final String ID = Sushang1.class.getSimpleName();
    
    public Sushang1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        p,
                        p,
                        new BreakEffectPower(p, magicNumber),
                        magicNumber
                )
        );
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
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                        (target) -> {
                            AbstractDungeon.actionManager.addToBottom(
                                    new DrawIfBrokenAction(target, magicNumber)
                            );
                        }
                )
        );
    }
    
    private static class DrawIfBrokenAction extends AbstractGameAction {
        private AbstractCreature c;
        private int amount;
        
        public DrawIfBrokenAction(AbstractCreature c, int amount) {
            this.c = c;
            this.amount = amount;
        }
        
        @Override
        public void update() {
            if (c.hasPower(BrokenPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(
                        new DrawCardAction(c, amount)
                );
            }
            isDone = true;
        }
    }
}
