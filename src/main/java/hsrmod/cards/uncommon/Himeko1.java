package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.utils.SubscribeManager;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Himeko1 extends BaseCard implements PostPowerApplySubscriber {
    public static final String ID = Himeko1.class.getSimpleName();
    
    public Himeko1() {
        super(ID);
        tags.add(FOLLOW_UP);
        BaseMod.subscribe(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(
                    new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage),
                            ElementType.Fire, 1,
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL))
            );
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SubscribeManager.checkSubscriber(this)
                && AbstractDungeon.player.hand.contains(this) 
                && abstractPower instanceof BrokenPower 
                && !followedUp) {
            followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
