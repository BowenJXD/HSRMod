package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.subscribers.ICheckUsableSubscriber;
import hsrmod.subscribers.PreFollowUpSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.ENERGY_COSTING;
import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class ChampionsDinnerCatsCradlePower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(ChampionsDinnerCatsCradlePower.class.getSimpleName());

    int chance = 75;

    public ChampionsDinnerCatsCradlePower(boolean upgraded, int chance) {
        super(POWER_ID, upgraded);
        this.chance = chance;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], chance);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        ModHelper.findCards((c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && !c.hasTag(FOLLOW_UP)).forEach((r) -> processCard(r.card));
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);
        processCard(card);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        processCard(card);
    }

    void processCard(AbstractCard card) {
        if (card.hasTag(CustomEnums.ENERGY_COSTING) && !card.hasTag(FOLLOW_UP)) {
            card.tags.add(FOLLOW_UP);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        /*ModHelper.findCards((c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && c.hasTag(FOLLOW_UP))
                .forEach((r) -> r.card.tags.remove(FOLLOW_UP));*/
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (abstractPower instanceof EnergyPower) {
            if ((abstractPower.amount > 0 && ModHelper.getPowerCount(owner, EnergyPower.POWER_ID) < EnergyPower.AMOUNT_LIMIT)
                    /*|| (abstractPower.amount < 0 && ModHelper.getPowerCount(owner, EnergyPower.POWER_ID) > 0)*/) {
                onSpecificTrigger();
            }
        }
        return true;
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        if (AbstractDungeon.cardRandomRng.random(99) < chance) {
            BaseCard card = (BaseCard) GeneralUtil.getRandomElement(
                    AbstractDungeon.player.hand.group,
                    AbstractDungeon.cardRandomRng,
                    (c) -> c.hasTag(CustomEnums.ENERGY_COSTING) && c instanceof BaseCard
            );
            if (card != null) {
                flash();
                card.energyCost = 0;
                addToBot(new FollowUpAction(card));
            }
        }
    }
}
