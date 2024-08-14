package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.Feed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.BreakAction;
import hsrmod.powers.BreakEffect;
import hsrmod.powers.EnergyCounter;

public class Sushang1 extends BaseCard {
    public static final String ID = Sushang1.class.getSimpleName();
    
    public static final int BREAK_EFFECT_GAIN = 1;
    
    public Sushang1() {
        super(ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BreakEffect(p, BREAK_EFFECT_GAIN), BREAK_EFFECT_GAIN));
        for (AbstractCreature mon : AbstractDungeon.getMonsters().monsters) {
            if (mon == m) {
                int x= 1;
            }
        }
        AbstractDungeon.actionManager.addToBottom(
                new BreakAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                damageTypeForTurn
                        ),
                        () -> AbstractDungeon.actionManager.addToBottom( new DrawCardAction(p, magicNumber) )
                )
        );
    }
}
