package hsrmod.cardsV2.Abundance;

import basemod.BaseMod;
import basemod.interfaces.PostPotionUseSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import hsrmod.actions.RandomCardFromDrawPileToHandAction;
import hsrmod.cards.BaseCard;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class PerfectTiming extends BaseCard implements PostPotionUseSubscriber {
    public static final String ID = PerfectTiming.class.getSimpleName();
    
    public PerfectTiming() {
        super(ID);
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        BaseMod.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        ModHelper.addToBotAbstract(this::trigger);
    }

    @Override
    public void onRetained() {
        super.onRetained();
        ModHelper.addToBotAbstract(this::trigger);
    }

    void trigger() {
        AbstractPlayer p = AbstractDungeon.player;
        addToTop(new ApplyPowerAction(p, p, AbstractDungeon.cardRandomRng.randomBoolean() ? new ArtifactPower(p, 1) : new DexterityPower(p, 1)));
        if (upgraded) addToTop(new RandomCardFromDrawPileToHandAction());
    }

    @Override
    public void receivePostPotionUse(AbstractPotion abstractPotion) {
        if (SubscriptionManager.checkSubscriber(this)) {
            ModHelper.addToBotAbstract(this::trigger);
        }
    }
}
