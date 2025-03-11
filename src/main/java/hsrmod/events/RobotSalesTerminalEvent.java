package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.*;

public class RobotSalesTerminalEvent extends PhasedEvent {
    public static final String ID = RobotSalesTerminalEvent.class.getSimpleName();
    private static final EventStrings eventStrings;
    private static final String[] DESCRIPTIONS;
    private static final String[] OPTIONS;
    private static final String NAME;

    int[] purchaseCosts = new int[]{25, 50, 100};
    int purgeCost;

    public RobotSalesTerminalEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        purchaseCosts = ModHelper.eventAscension() ? new int[]{30, 60, 120} : new int[]{25, 50, 100};
        purgeCost = ModHelper.eventAscension() ? 75 : 50;

        AbstractCard commonCard = RewardEditor.getInstance().getCardByPath(AbstractCard.CardRarity.COMMON, null);
        AbstractCard uncommonCard = RewardEditor.getInstance().getCardByPath(AbstractCard.CardRarity.UNCOMMON, null);
        AbstractCard rareCard = RewardEditor.getInstance().getCardByPath(AbstractCard.CardRarity.RARE, null);

        registerPhase(0, new TextPhase(DESCRIPTIONS[0])
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[0], commonCard.name, purchaseCosts[0]), commonCard)
                        .setOptionResult((i) -> {
                            AbstractDungeon.player.loseGold(purchaseCosts[0]);
                            RelicEventHelper.gainCards(commonCard);
                            transitionKey(1);
                        }).enabledCondition(() -> AbstractDungeon.player.gold >= purchaseCosts[0]))
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[0], uncommonCard.name, purchaseCosts[1]), uncommonCard)
                        .setOptionResult((i) -> {
                            AbstractDungeon.player.loseGold(purchaseCosts[1]);
                            RelicEventHelper.gainCards(uncommonCard);
                            transitionKey(1);
                        }).enabledCondition(() -> AbstractDungeon.player.gold >= purchaseCosts[1]))
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[0], rareCard.name, purchaseCosts[2]), rareCard)
                        .setOptionResult((i) -> {
                            AbstractDungeon.player.loseGold(purchaseCosts[2]);
                            RelicEventHelper.gainCards(rareCard);
                            transitionKey(1);
                        }).enabledCondition(() -> AbstractDungeon.player.gold >= purchaseCosts[2]))
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[1], purgeCost))
                        .setOptionResult((i) -> AbstractDungeon.player.loseGold(purgeCost))
                        .cardRemovalOption(1, OPTIONS[5], 1)
                        .enabledCondition(() -> AbstractDungeon.player.gold >= purgeCost))
                .addOption(OPTIONS[3], (i) -> openMap())
        );

        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[4], (i) -> openMap()));

        transitionKey(0);
    }
    
    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        NAME = eventStrings.NAME;
    }
}
