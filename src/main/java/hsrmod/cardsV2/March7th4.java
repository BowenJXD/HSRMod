package hsrmod.cardsV2;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class March7th4 extends BaseCard {
    public static final String ID = March7th4.class.getSimpleName();
    
    AbstractCard sifu;
    
    public March7th4(){
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        CardGroup hand = AbstractDungeon.player.hand;
        if (!followedUp 
                && hand.contains(c) 
                && hand.contains(this) 
                && c != this) {
            int delta = hand.group.indexOf(this) - hand.group.indexOf(c);
            if (delta == 1 || (upgraded && delta == -1)) {
                sifu = c;
                followedUp = true;
                addToBot(new FollowUpAction(this));
            }
        }
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (sifu == null) return;
        if (sifu.type == CardType.ATTACK && sifu.damage > 0) {
            addToBot(new ElementalDamageAction(m, new DamageInfo(p, sifu.damage, damageTypeForTurn), elementType, 2));
        }
        else if (sifu.type == CardType.SKILL && sifu.block > 0) {
            addToBot(new GainBlockAction(p, p, sifu.block));
        }
        sifu = null;
    }
}
