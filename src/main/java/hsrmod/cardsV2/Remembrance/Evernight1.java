package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.orbs.Frost;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.PostHPUpdateSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.stream.Collectors;

public class Evernight1 extends BaseCard implements PostHPUpdateSubscriber {
    public static final String ID = Evernight1.class.getSimpleName();

    public Evernight1() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onMove(CardGroup group, boolean in) {
        super.onMove(group, in);
        if (group.type == CardGroup.CardGroupType.DISCARD_PILE) {
            if (in) ModHelper.addToBotAbstract(() -> {
                cacheHP = AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player);
                SubscriptionManager.subscribe(this);
            });
            else SubscriptionManager.unsubscribe(this);
        }
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        // addToBot(new ChannelAction(new Frost()));
        ModHelper.addToBotAbstract(() -> {
            if (p.filledOrbCount() > 0) {
                ModHelper.triggerPassiveTo(p.orbs.get(0), m);
            }
            /*GeneralUtil.getRandomElements(AbstractDungeon.player.orbs.stream().filter(o -> !(o instanceof EmptyOrbSlot || o == null)).collect(Collectors.toList()), 
                    AbstractDungeon.cardRandomRng, magicNumber).forEach(o -> ModHelper.triggerPassiveTo(o, m));*/
        });
    }
    
    int cacheHP;

    @Override
    public void postHPUpdate(AbstractCreature creature) {
        if (SubscriptionManager.checkSubscriber(this) 
                && creature == AbstractDungeon.player
                && AbstractDungeon.player.discardPile.contains(this)
                && creature.currentHealth + TempHPField.tempHp.get(creature) < cacheHP - 1
        ) {
            addToTop(new DiscardToHandAction(this));
        }
    }
}
