package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Aventurine2 extends BaseCard {
    public static final String ID = Aventurine2.class.getSimpleName();
    
    public Aventurine2() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            int blockThisTurn = AbstractDungeon.cardRandomRng.random(1, block);
            addToBot(new GainBlockAction(p, p, blockThisTurn));
            addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (c instanceof BaseCard) {
            BaseCard card = (BaseCard) c;
            if (card.followedUp && !followedUp) {
                followedUp = true;
                addToBot(new FollowUpAction(this));
            }
        }
    }
}
