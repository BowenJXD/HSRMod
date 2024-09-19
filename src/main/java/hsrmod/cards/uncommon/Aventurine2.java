package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Aventurine2 extends BaseCard {
    public static final String ID = Aventurine2.class.getSimpleName();
    
    int playerBlock = 0;
    
    public Aventurine2() {
        super(ID);
        tags.add(FOLLOW_UP);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            int blockThisTurn = AbstractDungeon.cardRandomRng.random(1, block);
            addToBot(new GainBlockAction(p, p, blockThisTurn));
        }
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 0), 0));
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        playerBlock = AbstractDungeon.player.currentBlock;
    }

    @Override
    public void triggerAtStartOfTurn() {
        super.triggerAtStartOfTurn();
        playerBlock = AbstractDungeon.player.currentBlock;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (!followedUp) {
            ModHelper.addToBotAbstract(() -> {
                if (AbstractDungeon.player.currentBlock > playerBlock) {
                    followedUp = true;
                    addToBot(new FollowUpAction(this));
                }
                else 
                    ModHelper.addToBotAbstract(() -> {
                        if (AbstractDungeon.player.currentBlock > playerBlock) {
                            followedUp = true;
                            addToBot(new FollowUpAction(this));
                        }
                    });
            });
        }
    }
}
