package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import basemod.patches.com.megacrit.cardcrawl.helpers.MonsterHelper.GetEncounter;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.cards.rare.IntersegmentalMembrane;
import hsrmod.cardsV2.Propagation.ExcitatoryGland;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.boss.Plaguenest;
import hsrmod.relics.starter.WaxOfPropagation;
import hsrmod.utils.ModHelper;

public class SlumberingOverlordEvent extends PhasedEvent {
    public static final String ID = SlumberingOverlordEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public SlumberingOverlordEvent() {
        super(ID, NAME, "HSRModResources/img/events/" + ID + ".png");

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        registerPhase(1, new TextPhase(DESCRIPTIONS[1]).addOption(OPTIONS[0], (i) -> transitionKey(2)));

        TextPhase phase2 = new TextPhase(DESCRIPTIONS[2]);
        phase2.addOption(OPTIONS[1], (i) -> {
            int dmg = AbstractDungeon.player.currentHealth / 5;
            AbstractDungeon.player.damage(new DamageInfo((AbstractCreature) null, dmg, DamageInfo.DamageType.HP_LOSS));
            transitionKey(5);
        });
        phase2.addOption(new TextPhase.OptionInfo(OPTIONS[2], new IntersegmentalMembrane()).setOptionResult((i) -> transitionKey(3)));
        phase2.addOption(new TextPhase.OptionInfo(OPTIONS[3], new ExcitatoryGland()).setOptionResult((i) -> transitionKey(4)));

        if (ModHelper.hasRelic(WaxOfPropagation.ID)) {
            AbstractCard card = AbstractDungeon.getCard(AbstractCard.CardRarity.CURSE);
            phase2.addOption(new TextPhase.OptionInfo(OPTIONS[4], card).setOptionResult((i) -> {
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractRelic relic = RelicLibrary.getRelic(HSRMod.makePath(Plaguenest.ID)).makeCopy();
                if (relic != null) 
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
                transitionKey(8);
            }).enabledCondition(() -> ModHelper.hasRelic(WaxOfPropagation.ID)));
        }

        registerPhase(2, phase2);

        registerPhase(3, new CombatPhase(MonsterHelper.THREE_DARKLINGS_ENC).setNextKey(6));
        registerPhase(4, new CombatPhase(MonsterHelper.SPIRE_GROWTH_ENC).setNextKey(7));

        registerPhase(5, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[6], (i) -> openMap()));

        AbstractCard card1 = new IntersegmentalMembrane();
        registerPhase(6, new TextPhase(DESCRIPTIONS[4]).addOption(new TextPhase.OptionInfo(OPTIONS[5], card1).setOptionResult((i) -> {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card1, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            transitionKey(9);
        })));

        AbstractCard card2 = new ExcitatoryGland();
        registerPhase(7, new TextPhase(DESCRIPTIONS[4]).addOption(new TextPhase.OptionInfo(OPTIONS[5], card2).setOptionResult((i) -> {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card2, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            transitionKey(9);
        })));

        registerPhase(8, new TextPhase(DESCRIPTIONS[5]).addOption(OPTIONS[6], (i) -> openMap()));
        
        registerPhase(9, new TextPhase(DESCRIPTIONS[6]).addOption(OPTIONS[6], (i) -> openMap()));
        
        transitionKey(0);
    }
}
