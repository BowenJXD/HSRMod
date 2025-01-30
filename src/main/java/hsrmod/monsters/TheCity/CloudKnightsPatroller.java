package hsrmod.monsters.TheCity;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ThieveryPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.utils.ModHelper;

public class CloudKnightsPatroller extends BaseMonster {
    public static final String ID = CloudKnightsPatroller.class.getSimpleName();

    int block = 16;
    int goldAmt = 15;
    int stolenGold = 0;

    public CloudKnightsPatroller(float x, float y) {
        super(ID, 200, 256, x, y);
        setDamagesWithAscension(10);
        block = ModHelper.moreHPAscension(type) ? 20 : 18;
        goldAmt = ModHelper.specialAscension(type) ? 20 : 15;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ThieveryPower(this, goldAmt)));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                if (AbstractDungeon.aiRng.randomBoolean(0.6F)) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.3F, 2.0F));
                    playSfx();
                }
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    public void update() {
                        CloudKnightsPatroller.this.stolenGold = CloudKnightsPatroller.this.stolenGold + Math.min(CloudKnightsPatroller.this.goldAmt, AbstractDungeon.player.gold);
                        this.isDone = true;
                    }
                });
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), this.goldAmt));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.75F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[3], 1)));
                break;
            case 2:
                if (hasPower(ChargingPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2], 0.3F, 2.5F));
                    AbstractDungeon.getCurrRoom().mugged = true;
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                    AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        turnCount++;
        if (lastMove((byte) 1)) {
            setMove(MOVES[2], (byte) 2, Intent.ESCAPE);
        }
        else if ((turnCount == 3 && i > 50) || turnCount > 3) {
            setMove(MOVES[1], (byte) 1, Intent.DEFEND);
        }
        else {
            setMove(MOVES[0], (byte) 0, Intent.ATTACK, (this.damage.get(0)).base);
        }
    }

    private void playSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1A"));
        } else if (roll == 1) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1B"));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1C"));
        }
    }
    
    private void playDeathSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_LOOTER_2A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_LOOTER_2B");
        } else {
            CardCrawlGame.sound.play("VO_LOOTER_2C");
        }
    }

    public void die() {
        this.playDeathSfx();
        this.useShakeAnimation(5.0F);
        if (MathUtils.randomBoolean(0.3F)) {
            AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY, 2.0F, DIALOG[3], false));
            if (!Settings.FAST_MODE) {
                ++this.deathTimer;
            }
        }

        if (this.stolenGold > 0) {
            AbstractDungeon.getCurrRoom().addStolenGoldToRewards(this.stolenGold);
        }

        super.die();
    }
}
