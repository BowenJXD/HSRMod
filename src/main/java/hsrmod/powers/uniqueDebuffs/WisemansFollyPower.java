package hsrmod.powers.uniqueDebuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.uncommon.DrRatio3;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

import java.util.Iterator;

public class WisemansFollyPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(WisemansFollyPower.class.getSimpleName());

    public WisemansFollyPower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.NORMAL 
                || info.owner != AbstractDungeon.player) {
            return damageAmount;
        }
        
        Iterator<AbstractCard> hand = AbstractDungeon.player.hand.group.iterator();
        
        int debuffNum = 0;
        int chance = 40;
        for (AbstractPower power : owner.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                debuffNum++;
                chance += 20;
            }
        }

        while (hand.hasNext()) {
            AbstractCard card = hand.next();
            if (card instanceof DrRatio3) {
                DrRatio3 c = (DrRatio3) card;
                if (c.followedUp) continue;
                c.debuffNum = debuffNum;

                if (AbstractDungeon.cardRandomRng.random(100) < chance) {
                    c.followedUp = true;
                    addToBot(new FollowUpAction(card, owner));
                }
            }
        }
        
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        boolean hasDrRatio3 = false;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof DrRatio3) {
                hasDrRatio3 = true;
                break;
            }
        }
        
        if (!hasDrRatio3) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onRemove() {
        Iterator<AbstractCard> iterator = AbstractDungeon.player.hand.group.iterator();
        
        while (iterator.hasNext()) {
            AbstractCard card = iterator.next();
            if (card instanceof DrRatio3) {
                addToBot(new DiscardSpecificCardAction(card));
            }
        }
    }
}
