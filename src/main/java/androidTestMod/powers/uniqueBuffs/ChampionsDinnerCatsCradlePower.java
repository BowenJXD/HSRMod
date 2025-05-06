package androidTestMod.powers.uniqueBuffs;

import androidTestMod.AndroidTestMod;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.powers.PowerPower;
import androidTestMod.powers.interfaces.OnReceivePowerPower;
import androidTestMod.powers.misc.EnergyPower;
import androidTestMod.utils.GeneralUtil;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Predicate;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class ChampionsDinnerCatsCradlePower extends PowerPower implements OnReceivePowerPower {
    public static final String POWER_ID = AndroidTestMod.makePath(ChampionsDinnerCatsCradlePower.class.getSimpleName());

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
        for (ModHelper.FindResult r : ModHelper.findCards(new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return c.hasTag(CustomEnums.ENERGY_COSTING) && !c.hasTag(FOLLOW_UP);
            }
        })) {
            ChampionsDinnerCatsCradlePower.this.processCard(r.card);
        }
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
                    new Predicate<AbstractCard>() {
                        @Override
                        public boolean test(AbstractCard c) {
                            return c.hasTag(CustomEnums.ENERGY_COSTING) && c instanceof BaseCard;
                        }
                    }
            );
            if (card != null) {
                flash();
                card.energyCost = 0;
                addToBot(new FollowUpAction(card));
            }
        }
    }
}
