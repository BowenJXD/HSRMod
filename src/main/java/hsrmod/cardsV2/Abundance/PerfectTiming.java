package hsrmod.cardsV2.Abundance;

import basemod.BaseMod;
import basemod.interfaces.PostPotionUseSubscriber;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.subscribers.SubscriptionManager;

public class PerfectTiming extends BaseCard implements PostPotionUseSubscriber {
    public static final String ID = PerfectTiming.class.getSimpleName();
    
    public PerfectTiming() {
        super(ID);
        isInnate = true;
        tags.add(CustomEnums.FOLLOW_UP);
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
        addToBot(new ApplyPowerAction(p, p, AbstractDungeon.cardRandomRng.randomBoolean() ? new ArtifactPower(p, 1) : new DexterityPower(p, 1)));
        addToBot(new AddTemporaryHPAction(p, p, block * energyOnUse));
        if (!freeToPlayOnce)
            addToBot(new LoseEnergyAction(EnergyPanel.totalCount));
    }

    @Override
    public void receivePostPotionUse(AbstractPotion abstractPotion) {
        if (SubscriptionManager.checkSubscriber(this)) {
            addToBot(new FollowUpAction(this));
        }
    }
}
