package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class MultiMovePower extends AbstractPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(MultiMovePower.class.getSimpleName());
    public String[] DESCRIPTIONS;
    public PowerStrings powerStrings;
    
    int turnCountCache = 0;

    public MultiMovePower(AbstractCreature owner, int Amount) {
        this.ID = POWER_ID;
        this.name = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
        this.DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS;
        this.owner = owner;
        this.amount = Amount;
        this.type = PowerType.BUFF;
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        this.loadRegion("doubleTap");
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    public void remove(int val) {
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            this.addToTop(new ReducePowerAction(this.owner, this.owner, this, val));
        }
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        if (GameActionManager.turn == turnCountCache) return;
        turnCountCache = GameActionManager.turn;
        if (owner instanceof AbstractMonster) {
            flash();
            ModHelper.addToBotAbstract(() -> {
                if (owner.hasPower(MultiMovePower.POWER_ID)) {
                    AbstractMonster m = (AbstractMonster) owner;
                    m.applyStartOfTurnPowers();
                    AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem((AbstractMonster) owner));
                    remove(1);
                }
            });
        }
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == this.owner) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
