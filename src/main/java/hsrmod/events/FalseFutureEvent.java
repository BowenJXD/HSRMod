package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.misc.Encounter;
import hsrmod.modcore.HSRMod;
import hsrmod.relics.special.TowatCards;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;
import hsrmod.utils.RelicEventHelper;

public class FalseFutureEvent extends PhasedEvent {
    public static final String ID = FalseFutureEvent.class.getSimpleName();
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID));
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    int goldAmt = 50;

    public FalseFutureEvent() {
        super(ID, NAME, PathDefine.EVENT_PATH + ID + ".png");

        goldAmt = ModHelper.eventAscension() ? 25 : 50;

        registerPhase(0, new TextPhase(DESCRIPTIONS[0]).addOption(OPTIONS[0], (i) -> transitionKey(1)));
        AbstractRelic towatCards = RelicLibrary.getRelic(HSRMod.makePath(TowatCards.ID)).makeCopy();

        registerPhase(1, new TextPhase(DESCRIPTIONS[1])
                .addOption(new TextPhase.OptionInfo(OPTIONS[1], towatCards), (i) -> {
                    AbstractDungeon.bossList.stream().filter(s -> !s.equals(AbstractDungeon.bossKey)).findAny().ifPresent(this::setBoss);
                    ModHelper.addEffectAbstract(() -> RelicEventHelper.gainRelics(towatCards));
                    transitionKey(2);
                })
                .addOption(new TextPhase.OptionInfo(GeneralUtil.tryFormat(OPTIONS[2], goldAmt)), (i) -> {
                    RelicEventHelper.gainGold(goldAmt);
                    transitionKey(3);
                })
        );

        registerPhase(2, new TextPhase(DESCRIPTIONS[2]).addOption(OPTIONS[3], (i) -> openMap()));
        registerPhase(3, new TextPhase(DESCRIPTIONS[3]).addOption(OPTIONS[3], (i) -> openMap()));

        transitionKey(0);
    }

    private void setBoss(String key) {
        String iconPath = null;
        String outlinePath = null;
        switch (key) {
            case Encounter.SALUTATIONS_OF_ASHEN_DREAMS:
                iconPath = "HSRModResources/img/monsters/SalutationsOfAshenDreams.png";
                outlinePath = "HSRModResources/img/monsters/BossOutline.png";
                break;
            case Encounter.BOREHOLE_PLANETS_OLD_CRATER:
                iconPath = "HSRModResources/img/monsters/BoreholePlanetsOldCrater.png";
                outlinePath = "HSRModResources/img/monsters/BossOutline.png";
                break;
            case Encounter.END_OF_THE_ETERNAL_FREEZE:
                iconPath = "HSRModResources/img/monsters/EndOfTheEternalFreeze.png";
                outlinePath = "HSRModResources/img/monsters/BossOutline.png";
                break;
            case Encounter.DIVINE_SEED:
                iconPath = "HSRModResources/img/monsters/DivineSeed.png";
                outlinePath = "HSRModResources/img/monsters/BossOutline.png";
                break;
            case Encounter.INNER_BEASTS_BATTLEFIELD:
                iconPath = "HSRModResources/img/monsters/InnerBeastsBattlefield.png";
                outlinePath = "HSRModResources/img/monsters/BossOutline.png";
                break;
            case Encounter.DESTRUCTIONS_BEGINNING:
                iconPath = "HSRModResources/img/monsters/DestructionsBeginning.png";
                outlinePath = "HSRModResources/img/monsters/BossOutline.png";
                break;
            case "The Guardian":
                iconPath = "images/ui/map/boss/guardian.png";
                outlinePath = "images/ui/map/bossOutline/guardian.png";
                break;
            case "Hexaghost":
                iconPath = "images/ui/map/boss/hexaghost.png";
                outlinePath = "images/ui/map/bossOutline/hexaghost.png";
                break;
            case "Slime Boss":
                iconPath = "images/ui/map/boss/slime.png";
                outlinePath = "images/ui/map/bossOutline/slime.png";
                break;
            case "Collector":
                iconPath = "images/ui/map/boss/collector.png";
                outlinePath = "images/ui/map/bossOutline/collector.png";
                break;
            case "Automaton":
                iconPath = "images/ui/map/boss/automaton.png";
                outlinePath = "images/ui/map/bossOutline/automaton.png";
                break;
            case "Champ":
                iconPath = "images/ui/map/boss/champ.png";
                outlinePath = "images/ui/map/bossOutline/champ.png";
                break;
            case "Awakened One":
                iconPath = "images/ui/map/boss/awakened.png";
                outlinePath = "images/ui/map/bossOutline/awakened.png";
                break;
            case "Time Eater":
                iconPath = "images/ui/map/boss/timeeater.png";
                outlinePath = "images/ui/map/bossOutline/timeeater.png";
                break;
            case "Donu and Deca":
                iconPath = "images/ui/map/boss/donu.png";
                outlinePath = "images/ui/map/bossOutline/donu.png";
                break;
            case "The Heart":
                iconPath = "images/ui/map/boss/heart.png";
                outlinePath = "images/ui/map/bossOutline/heart.png";
                break;
            default:
                HSRMod.logger.error("Unknown boss key: {}", key);
                break;
        }
        if (iconPath != null) {
            AbstractDungeon.bossKey = key;
            if (DungeonMap.boss != null && DungeonMap.bossOutline != null) {
                DungeonMap.boss.dispose();
                DungeonMap.bossOutline.dispose();
            }
            DungeonMap.boss = ImageMaster.loadImage(iconPath);
            DungeonMap.bossOutline = ImageMaster.loadImage(outlinePath);
        }
    }
}
