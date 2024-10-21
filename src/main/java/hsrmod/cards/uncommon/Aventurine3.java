package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

public class Aventurine3 extends BaseCard {
    public static final String ID = Aventurine3.class.getSimpleName();
    
    public Aventurine3() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void upgrade() {
        super.upgrade();
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if (!followedUp) addToBot(new FollowUpAction(this));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        if (!followedUp) addToBot(new FollowUpAction(this));
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (damage > 1) {
            int dmg = AbstractDungeon.cardRandomRng.random(1, damage);
            addToBot(new ElementalDamageAction(
                    m, 
                    new ElementalDamageInfo(this, dmg), 
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL
            ));
        }
        if (block > 1) {
            int blk = AbstractDungeon.cardRandomRng.random(1, block);
            addToBot(new GainBlockAction(p, p, blk));
        }
    }
}
