package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.misc.DoTPower;

import java.util.Iterator;

public class Kafka1 extends BaseCard {
    public static final String ID = Kafka1.class.getSimpleName();
    
    public Kafka1() {
        super(ID);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (inHand && !followedUp) {
            if (m != null && m.powers.stream().anyMatch(power -> power instanceof DoTPower)) {
                followedUp = true;
                addToBot(new FollowUpAction(this, m));
            } else if ((c.target == CardTarget.ALL || c.target == CardTarget.ALL_ENEMY)) {
                Iterator<AbstractMonster> mons = AbstractDungeon.getMonsters().monsters.iterator();
                
                while (mons.hasNext()) {
                    AbstractMonster mon = mons.next();
                    if (mon.powers.stream().anyMatch(power -> power instanceof DoTPower)) {
                        followedUp = true;
                        addToBot(new FollowUpAction(this, mon));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ElementalDamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), 
                ElementType.Lightning, 2, AbstractGameAction.AttackEffect.LIGHTNING));
        addToBot(new TriggerDoTAction(m, magicNumber));
        addToBot(new ApplyPowerAction(m, p, new ShockPower(m, p, magicNumber), magicNumber));
    }
}
