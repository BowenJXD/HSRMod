package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.utils.Timer;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.EnergyPower;

import java.util.concurrent.*;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class RobinPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(RobinPower.class.getSimpleName());
    
    public RobinPower(AbstractCreature owner, boolean upgraded) {
        super(POWER_ID, owner, upgraded);
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();

        if (AbstractDungeon.getMonsters() != null
                && AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> m.type == AbstractMonster.EnemyType.BOSS)) {
            // 取消静音背景音乐
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    CardCrawlGame.music.playTempBgmInstantly("RobinBGM");
                }
            }, 2);
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (AbstractDungeon.getMonsters() != null
                && AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> m.type == AbstractMonster.EnemyType.BOSS)) {
            AbstractDungeon.scene.fadeInAmbiance();
            CardCrawlGame.music.fadeOutTempBGM();
        }
    }

    @Override
    public void atStartOfTurn() {
        addToTop(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, amount));
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        flash();
        if (card.type == AbstractCard.CardType.ATTACK && (card.costForTurn > 0 || card.cost == -1)) {
            int amt = card.cost == -1 ? card.energyOnUse : card.costForTurn;
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amt), amt));
            amount += amt;
            if (card.hasTag(FOLLOW_UP) && upgraded) addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, 20), 20));
        }
    }
}
