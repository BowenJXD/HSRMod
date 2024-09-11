package hsrmod.relics.starter;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.actions.ReduceECByHandCardNumAction;
import hsrmod.cards.base.*;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.special.*;
import hsrmod.utils.CustomEnums;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PomPomBlessing extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = HSRMod.makePath(PomPomBlessing.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/PomPomBlessing.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private static int BASE_ENERGY = 100;

    private static int ENERGY_GAIN_PER_CARD = 20;

    private int multiplier = 20;

    public PomPomBlessing() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
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
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, BASE_ENERGY), BASE_ENERGY));
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IntangiblePlayerPower(AbstractDungeon.player, 1), 1));
        addToTop(new GainEnergyAction(AbstractDungeon.actNum));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, ENERGY_GAIN_PER_CARD), ENERGY_GAIN_PER_CARD));
    }

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
            for (AbstractCard card : cards) {
                card.retain = true;
            }
            addToBot(new ReduceECByHandCardNumAction(AbstractDungeon.player, AbstractDungeon.player));
        };

        addToBot(new SelectCardsInHandAction(maxRetainNum, String.format("保留（每张消耗%d充能）", multiplier), true, true, cardFilter, callback));
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        super.onObtainCard(c);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        super.onAttack(info, damageAmount, target);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (AbstractDungeon.player.currentHealth - damageAmount <= 0
                && AbstractDungeon.player.masterDeck.group.stream().anyMatch(card -> card.hasTag(CustomEnums.REVIVE))) {
            flash();
            damageAmount = AbstractDungeon.player.currentHealth - 1;
            ModHelper.addToTopAbstract(() -> {
                addToTop(new SelectCardsAction(AbstractDungeon.player.masterDeck.group, 1, "选择牺牲一位列车组成员",
                        false, card -> card.hasTag(CustomEnums.REVIVE), cards -> {
                    AbstractCard card = cards.get(0);
                    ModHelper.findCards(card1 -> card1.getClass().equals(card.getClass())).forEach(findResult -> {
                        findResult.group.removeCard(findResult.card);
                    });
                    AbstractDungeon.player.masterDeck.removeCard(card);

                    String relicName = "";
                    String text = "";
                    if (card instanceof March7th0) {
                        relicName = March7thRelic.ID;
                        text = "我不想…一个人……";
                    } else if (card instanceof Danheng0) {
                        relicName = DanhengRelic.ID;
                        text = "不该是…现在……";
                    } else if (card instanceof Himeko0) {
                        relicName = HimekoRelic.ID;
                        text = "这就是…最后一课……";
                    } else if (card instanceof Welt0) {
                        relicName = WeltRelic.ID;
                        text = "活下去……";
                    }

                    if (!relicName.isEmpty())
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2),
                                RelicLibrary.getRelic(relicName).makeCopy());
                    if (!text.isEmpty())
                        addToBot(new TalkAction(true, text, 1.0F, 2.0F));
                }));
            });
        }
        return damageAmount;
    }
}
