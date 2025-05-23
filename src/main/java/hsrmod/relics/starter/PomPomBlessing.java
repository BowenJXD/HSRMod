package hsrmod.relics.starter;

import basemod.abstracts.CustomMultiPageFtue;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.actions.ReduceChargeByHandCardNumAction;
import hsrmod.cards.base.Danheng0;
import hsrmod.cards.base.Himeko0;
import hsrmod.cards.base.March7th0;
import hsrmod.cards.base.Welt0;
import hsrmod.cardsV2.Abundance.Castorice1;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.HSRModConfig;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.ITutorial;
import hsrmod.relics.special.DanhengRelic;
import hsrmod.relics.special.HimekoRelic;
import hsrmod.relics.special.March7thRelic;
import hsrmod.relics.special.WeltRelic;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PomPomBlessing extends CustomRelic implements ClickableRelic, ITutorial {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = HSRMod.makePath(PomPomBlessing.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = PathDefine.RELIC_PATH + "PomPomBlessing.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private static int ENERGY_GAIN_PER_CARD = 20;

    private int multiplier = 20;
    
    boolean tutorialTriggered = false;

    static Texture[] ftues = {
            ImageMaster.loadImage(PathDefine.UI_PATH + "tutorial/7.png"),
            ImageMaster.loadImage(PathDefine.UI_PATH + "tutorial/8.png"),
            ImageMaster.loadImage(PathDefine.UI_PATH + "tutorial/9.png"),
    };

    static String[] tutTexts;

    public PomPomBlessing() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        setCounter(100);

        tutTexts = new String[]{
                DESCRIPTIONS[8],
                DESCRIPTIONS[9],
                DESCRIPTIONS[10],
        };
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new PomPomBlessing();
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, Math.max(0, counter))));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, 1), 1));
        addToBot(new GainEnergyAction(2));
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        ModHelper.addToBotAbstract(() -> setCounter(-1));
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if (counter > EnergyPower.AMOUNT_LIMIT) {
            this.counter = EnergyPower.AMOUNT_LIMIT;
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, ENERGY_GAIN_PER_CARD), ENERGY_GAIN_PER_CARD));
        
        if (!HSRMod.seenTutorials.contains(relicId) || HSRModConfig.firstTime && !tutorialTriggered) {
            tutorialTriggered = true;
            HSRMod.seenTutorials.add(relicId);
            if ((Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT )
                    && AbstractDungeon.getCurrRoom().monsters != null){
                AbstractDungeon.ftue = new CustomMultiPageFtue(ftues, tutTexts);
            }
        }
    }

    ArrayList<CardQueueItem> tempCardQueue = new ArrayList<>();

    @Override
    public void onPlayerEndTurn() {
        if (AbstractDungeon.player.hand.isEmpty()) {
            return;
        }

        AbstractPower power = AbstractDungeon.player.getPower(EnergyPower.POWER_ID);
        int maxRetainNum = 0;
        if (power != null) {
            maxRetainNum = power.amount / multiplier;
        }

        if (maxRetainNum == 0) return;

        Predicate<AbstractCard> cardFilter = c -> !c.selfRetain && !c.retain && !c.isEthereal;
        Consumer<List<AbstractCard>> callback = cards -> {
            AbstractDungeon.actionManager.cardQueue.addAll(tempCardQueue);
            for (AbstractCard card : cards) {
                card.retain = true;
            }
            addToTop(new ReduceChargeByHandCardNumAction(AbstractDungeon.player, AbstractDungeon.player));
        };

        addToTop(new SelectCardsInHandAction(maxRetainNum, String.format(DESCRIPTIONS[1], multiplier), true, true, cardFilter, callback));
        ModHelper.addToTopAbstract(() -> tempCardQueue = new ArrayList<>(AbstractDungeon.actionManager.cardQueue));

        if (!HSRMod.seenTutorials.contains(relicId) || HSRModConfig.firstTime && !tutorialTriggered) {
            tutorialTriggered = true;
            HSRMod.seenTutorials.add(relicId);
            if ((Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT )
                    && AbstractDungeon.getCurrRoom().monsters != null){
                AbstractDungeon.ftue = new CustomMultiPageFtue(ftues, tutTexts);
            }
        }
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if (AbstractDungeon.player.currentHealth - damageAmount <= 0
                && AbstractDungeon.player.masterDeck.group.stream().anyMatch(card -> card.hasTag(CustomEnums.REVIVE))) {
            flash();
            damageAmount = AbstractDungeon.player.currentHealth - 1;
            ModHelper.addToTopAbstract(() -> {
                addToTop(new SelectCardsAction(AbstractDungeon.player.masterDeck.group, 1, DESCRIPTIONS[2],
                        false, card -> card.hasTag(CustomEnums.REVIVE), cards -> {
                    AbstractCard card = cards.get(0);
                    ModHelper.findCards(card1 -> Objects.equals(card1.cardID, card.cardID)).forEach(findResult -> {
                        findResult.group.removeCard(findResult.card);
                    });
                    AbstractDungeon.player.masterDeck.removeCard(card);

                    String relicName = "";
                    String text = "";
                    if (card instanceof March7th0) {
                        relicName = March7thRelic.ID;
                        text = DESCRIPTIONS[3]; // 
                    } else if (card instanceof Danheng0) {
                        relicName = DanhengRelic.ID;
                        text = DESCRIPTIONS[4]; // 
                    } else if (card instanceof Himeko0) {
                        relicName = HimekoRelic.ID;
                        text = DESCRIPTIONS[5]; // 
                    } else if (card instanceof Welt0) {
                        relicName = WeltRelic.ID;
                        text = DESCRIPTIONS[6]; // 
                    } else if (card instanceof Castorice1) {
                        text = DESCRIPTIONS[7]; //
                        SignatureHelper.unlock(HSRMod.makePath(Castorice1.ID), true);
                    }

                    if (!relicName.isEmpty()) {
                        AbstractRelic relic = RelicLibrary.getRelic(relicName).makeCopy();
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                        ModHelper.addToTopAbstract(() -> relic.setCounter(-2));
                    }
                    if (!text.isEmpty())
                        addToBot(new TalkAction(true, text, 1.0F, 2.0F));
                }));
            });
        }
        return damageAmount;
    }

    @Override
    public void onVictory() {
        super.onVictory();
        setCounter(ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID));
    }

    @Override
    public void onRightClick() {
        if ((Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT )
                && AbstractDungeon.getCurrRoom().monsters != null){
            AbstractDungeon.ftue = new CustomMultiPageFtue(ftues, tutTexts);
        }
    }
}
