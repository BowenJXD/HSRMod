package hsrmod.relics.starter;

import hsrmod.Hsrmod;
import hsrmod.actions.ReduceChargeByHandCardNumAction;
import hsrmod.actions.SelectCardsAction;
import hsrmod.actions.SelectCardsInHandAction;
import hsrmod.cards.base.Danheng0;
import hsrmod.cards.base.Himeko0;
import hsrmod.cards.base.March7th0;
import hsrmod.cards.base.Welt0;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.special.DanhengRelic;
import hsrmod.relics.special.HimekoRelic;
import hsrmod.relics.special.March7thRelic;
import hsrmod.relics.special.WeltRelic;
import hsrmod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.android.mods.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PomPomBlessing extends CustomRelic {
    // 遗物ID（此处的ModHelper在“04 - 本地化”中提到）
    public static final String ID = Hsrmod.makePath(PomPomBlessing.class.getSimpleName());
    // 图片路径
    private static final String IMG_PATH = "HSRModResources/img/relics/PomPomBlessing.png";
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    private static int ENERGY_GAIN_PER_CARD = 20;

    private int multiplier = 20;

    public PomPomBlessing() {
        super(Hsrmod.MOD_NAME, ID, IMG_PATH, RELIC_TIER, LANDING_SOUND);
        setCounter(100);
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
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                PomPomBlessing.this.setCounter(-1);
            }
        });
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

        Predicate<AbstractCard> cardFilter = new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard c) {
                return !c.selfRetain && !c.retain && !c.isEthereal;
            }
        };
        Consumer<List<AbstractCard>> callback = new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> cards) {
                AbstractDungeon.actionManager.cardQueue.addAll(tempCardQueue);
                for (AbstractCard card : cards) {
                    card.retain = true;
                }
                PomPomBlessing.this.addToTop(new ReduceChargeByHandCardNumAction(AbstractDungeon.player, AbstractDungeon.player));
            }
        };

        addToTop(new SelectCardsInHandAction(maxRetainNum, String.format(DESCRIPTIONS[1], multiplier), true, true, cardFilter, callback));
        ModHelper.addToTopAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                tempCardQueue = new ArrayList<>(AbstractDungeon.actionManager.cardQueue);
            }
        });
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if (AbstractDungeon.player.currentHealth - damageAmount <= 0) {
            boolean b = false;
            for (AbstractCard abstractCard : AbstractDungeon.player.masterDeck.group) {
                if (abstractCard.hasTag(CustomEnums.REVIVE)) {
                    b = true;
                    break;
                }
            }
            if (b) {
                flash();
                damageAmount = AbstractDungeon.player.currentHealth - 1;
                ModHelper.addToTopAbstract(new ModHelper.Lambda() {
                    @Override
                    public void run() {
                        PomPomBlessing.this.addToTop(new SelectCardsAction(AbstractDungeon.player.masterDeck.group, 1, DESCRIPTIONS[2],
                                false, new Predicate<AbstractCard>() {
                            @Override
                            public boolean test(AbstractCard card) {
                                return card.hasTag(CustomEnums.REVIVE);
                            }
                        }, new Consumer<List<AbstractCard>>() {
                            @Override
                            public void accept(List<AbstractCard> cards) {
                                AbstractCard card = cards.get(0);
                                for (ModHelper.FindResult findResult : ModHelper.findCards(new Predicate<AbstractCard>() {
                                    @Override
                                    public boolean test(AbstractCard card1) {
                                        return Objects.equals(card1.cardID, card.cardID);
                                    }
                                })) {
                                    findResult.group.removeCard(findResult.card);
                                }
                                AbstractDungeon.player.masterDeck.removeCard(card);

                                String relicName = "";
                                String text = "";
                                if (card instanceof March7th0) {
                                    relicName = March7thRelic.ID;
                                    text = DESCRIPTIONS[3];
                                } else if (card instanceof Danheng0) {
                                    relicName = DanhengRelic.ID;
                                    text = DESCRIPTIONS[4];
                                } else if (card instanceof Himeko0) {
                                    relicName = HimekoRelic.ID;
                                    text = DESCRIPTIONS[5];
                                } else if (card instanceof Welt0) {
                                    relicName = WeltRelic.ID;
                                    text = DESCRIPTIONS[6];
                                }

                                if (!relicName.isEmpty()) {
                                    AbstractRelic relic = RelicLibrary.getRelic(relicName).makeCopy();
                                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
                                    ModHelper.addToTopAbstract(new ModHelper.Lambda() {
                                        @Override
                                        public void run() {
                                            relic.setCounter(-2);
                                        }
                                    });
                                }
                                if (!text.isEmpty())
                                    PomPomBlessing.this.addToBot(new TalkAction(true, text, 1.0F, 2.0F));
                            }
                        }));
                    }
                });
            }
        }
        return damageAmount;
    }

    @Override
    public void onVictory() {
        super.onVictory();
        setCounter(ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID));
    }
}
