package hsrmod.powers.uniqueBuffs;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import hsrmod.actions.FollowUpAction;
import hsrmod.cardsV2.Remembrance.Pollux3;
import hsrmod.cardsV2.Trailblaze.Khaslana2;
import hsrmod.cardsV2.Trailblaze.Khaslana4;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.TerritoryPower;
import hsrmod.utils.GAMManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RuinousIrontombPower extends TerritoryPower implements OnReceivePowerPower {
    public static final String POWER_ID = HSRMod.makePath(RuinousIrontombPower.class.getSimpleName());
    
    static final int SCOURGE_COUNT = 7;
    static final int HEAL = 10;
    int cardsPlayed = 0;
    
    boolean triggered = false;
    List<AbstractMonster> queuedMonsters = new ArrayList<>();
    
    public RuinousIrontombPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
        backgroundPath = "HSRModResources/img/scene/RuinousIrontomb.png";
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], SCOURGE_COUNT, HEAL, amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        
        for (AbstractPower power : owner.powers) {
            if (power.type == PowerType.DEBUFF || Objects.equals(power.ID, IntangiblePlayerPower.POWER_ID) || Objects.equals(power.ID, IntangiblePower.POWER_ID)) {
                addToBot(new RemoveSpecificPowerAction(owner, owner, power));
            }
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (ModHelper.check(monster)) {
                if (ModHelper.getPowerCount(monster, IntangiblePlayerPower.POWER_ID) > 0)
                    addToBot(new RemoveSpecificPowerAction(monster, owner, IntangiblePlayerPower.POWER_ID));
                else if (ModHelper.getPowerCount(monster, IntangiblePower.POWER_ID) > 0)
                    addToBot(new RemoveSpecificPowerAction(monster, owner, IntangiblePower.POWER_ID));
            }
        }
        addToBot(new ApplyPowerAction(owner, owner, new ScourgePower(owner, SCOURGE_COUNT)));

        CardCrawlGame.music.silenceBGM();
        CardCrawlGame.music.justFadeOutTempBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly("Flares of the Blazing Sun");

        queuedMonsters = new ArrayList<>();
        GAMManager.addParallelAction(POWER_ID, action -> {
            if (action instanceof RollMoveAction) {
                try {
                    AbstractMonster monster = ReflectionHacks.getPrivate(action, RollMoveAction.class, "monster");
                    if (monster != null && queuedMonsters.contains(monster)) {
                        ModHelper.addToTopAbstract(monster::createIntent);
                        queuedMonsters.remove(monster);
                    }
                } catch (Exception e) {
                    HSRMod.logger.log(Level.WARN, e);
                }
            }
            return false;
        });
    }

    @Override
    public void onRemove() {
        super.onRemove();

        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.fadeOutTempBGM();
        
        GAMManager.removeParallelAction(POWER_ID);
        
        addToBot(new RemoveSpecificPowerAction(owner, owner, ScourgePower.POWER_ID));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        addToBot(new GainBlockAction(owner, owner, HEAL));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        if (!queuedMonsters.isEmpty()) return;
        if (Objects.equals(card.cardID, HSRMod.makePath(Khaslana2.ID))) return;
        if (Objects.equals(card.cardID, HSRMod.makePath(Khaslana4.ID))) return;
        AbstractMonster monster = ModHelper.betterGetRandomMonster();
        ModHelper.addToBotAbstract(() -> {
            if (!monster.hasPower("Barricade")) {
                monster.loseBlock();
            }
            monster.applyStartOfTurnPowers();
            AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem(monster));
            queuedMonsters.add(monster);
        });
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        reducePower(1);
        cardsPlayed++;
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (amount <= 0) {
            amount = 0;
            AbstractCard c = new Khaslana4();
            addToBot(new MakeTempCardInHandAction(c, false, true));
            addToBot(new FollowUpAction(c));
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount >= owner.currentBlock && !triggered) {
            triggered = true;
            AbstractCard card = new Khaslana4();
            card.baseDamage += cardsPlayed;
            addToBot(new MakeTempCardInHandAction(card, false, true));
            addToBot(new FollowUpAction(card));
            return 0;
        }
        return damageAmount;
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power != null && power.type == PowerType.DEBUFF && target == owner) {
            return false;
        }
        return true;
    }
}
