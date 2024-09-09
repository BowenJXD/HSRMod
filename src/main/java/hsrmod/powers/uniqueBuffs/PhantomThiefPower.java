package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class PhantomThiefPower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(PhantomThiefPower.class.getSimpleName());

    int percentage;

    public PhantomThiefPower(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;

        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], percentage);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.hasTag(FOLLOW_UP)) {
            if (upgraded) trigger();
            else if (card instanceof BaseCard) {
                BaseCard c = (BaseCard) card;
                if (c.followedUp) trigger();
            }
        }
    }

    void trigger(){
        if (AbstractDungeon.player.hand.isEmpty()) return;
        if (AbstractDungeon.cardRandomRng.random(100) < percentage) {
            flash();
            AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            if (card.hasTag(FOLLOW_UP)){
                addToBot(new FollowUpAction(card));
            }
            else {
                addToBot(new NewQueueCardAction(card, true, false, true));
            }
        }
    }
}
