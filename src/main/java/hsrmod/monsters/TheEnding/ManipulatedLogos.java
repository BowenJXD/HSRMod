package hsrmod.monsters.TheEnding;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.cards.common.Xueyi1;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.EntanglePower;
import hsrmod.powers.enemyOnly.ImmortalPower;
import hsrmod.cardsV2.Curse.Entangle;
import hsrmod.utils.ModHelper;

public class ManipulatedLogos extends BaseMonster {
    public static final String ID = ManipulatedLogos.class.getSimpleName();

    public ManipulatedLogos(float x, float y) {
        super(ID, 256, 256, x, y);
        floatIndex = 1;

        addMove(Intent.ATTACK_DEBUFF, 5, 3, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new EntanglePower(p, this, 1), 1));
        });

        addMove(Intent.ATTACK_DEBUFF, 5, mi -> {
            attack(mi, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, AttackAnim.SLOW);
            addToBot(new MakeTempCardInDiscardAction(new Entangle(), 1));
        });

        addMove(Intent.UNKNOWN, mi -> {
            addToBot(new HealAction(this, this, maxHealth));
            this.halfDead = false;
        });
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ImmortalPower(this, 1), 0));
        /*if (specialAs) {
            addToBot(new ApplyPowerAction(this, this, new AngerPower(this, 1), 1));
        }*/
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
