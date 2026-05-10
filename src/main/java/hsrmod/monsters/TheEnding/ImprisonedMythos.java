package hsrmod.monsters.TheEnding;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.ImprisonPower;
import hsrmod.powers.enemyOnly.EternalRecurrencePower;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.utils.ModHelper;

public class ImprisonedMythos extends BaseMonster {
    public static final String ID = ImprisonedMythos.class.getSimpleName();

    public ImprisonedMythos(float x, float y) {
        super(ID, 256, 256, x, y);
        floatIndex = 1;

        addMove(Intent.ATTACK_DEBUFF, 5, 3, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new ImprisonPower(p, 1), 1));
        });

        addMove(Intent.ATTACK_DEBUFF, 5, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_VERTICAL, AttackAnim.SLOW);
            addToBot(new MakeTempCardInDrawPileAction(new Imprison(), 1, false, false));
        });
        
        addMove(Intent.UNKNOWN, mi -> {
            addToBot(new HealAction(this, this, maxHealth));
            this.halfDead = false;
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new EternalRecurrencePower(this, 1)));
        /*if (specialAs) {
            addToBot(new ApplyPowerAction(this, this, new AngryPower(this, 1), 1));
        }*/
    }

    @Override
    public void takeTurn() {
        super.takeTurn();
        
    }

    @Override
    protected void getMove(int i) {
        if (halfDead) {
            setMove(2);
            return;
        }
        setMove(lastTwoMoves((byte) 0) ? 1 : 0);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && !halfDead && AbstractDungeon.getCurrRoom().cannotLose) {
            halfDead = true;
            for (AbstractPower pow : powers) {
                pow.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }
            addToBot(new RollMoveAction(this));
            ModHelper.addToBotAbstract(this::createIntent);
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
}
