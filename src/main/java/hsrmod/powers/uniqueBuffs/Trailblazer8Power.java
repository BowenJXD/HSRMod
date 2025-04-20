package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerLoseBlockSubscriber;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cardsV2.Preservation.Quake;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.subscribers.PostBreakBlockSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.Collections;
import java.util.List;

public class Trailblazer8Power extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer8Power.class.getSimpleName());
    
    public int percentage = 50;
    
    public Trailblazer8Power(boolean upgraded, int percentage) {
        super(POWER_ID, upgraded);
        this.percentage = percentage;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], percentage);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        if (card.baseBlock > 0 && card.block > 0 
                && AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            Quake quake = new Quake();
            quake.baseDamage = card.block;
            if (upgraded) quake.upgrade();
            addToTop(new MakeTempCardInHandAction(quake));
        }
    }
}
