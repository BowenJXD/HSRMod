package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChannelPower;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;

public class FortuneBananAdvisor extends BaseMonster implements IBanana {
    public static final String ID = FortuneBananAdvisor.class.getSimpleName();
    
    int channelCount = 1;
    boolean inClass = true;
    String outClassImg = PathDefine.MONSTER_PATH + "FortuneSeller" + ".png";
    String inClassImg = PathDefine.MONSTER_PATH + FortuneBananAdvisor.ID + ".png";
    
    public FortuneBananAdvisor(float x, float y, boolean inClass) {
        super(ID, 150F, 200F, x, y);
        this.inClass = inClass;
        setImg();
        channelCount = specialAs ? 2 : 1;
        
        addMove(Intent.UNKNOWN, mi->{
            addToBot(new VFXAction(new ShockWaveEffect(hb.cX, hb.cY, Color.FOREST, ShockWaveEffect.ShockWaveType.NORMAL), 0.2f));
            if (lastTwoMoves((byte) 0))
                addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM), -channelCount));
            if (p.hasPower(EnergyDownPower.POWER_ID))
                addToBot(new ReducePowerAction(p, this, EnergyDownPower.POWER_ID, 1));
            else
                AbstractDungeon.actionManager.addToTurnStart(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        ModHelper.addEffectAbstract(()-> ModHelper.addEffectAbstract(() -> addToBot(new GainEnergyAction(1))));
                    }
                });
        });
        addMoveA(Intent.ATTACK_DEBUFF, 15, mi->{
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.FAST);
            if (specialAs || !p.hasPower(EnergyDownPower.POWER_ID))
                addToBot(new ApplyPowerAction(p, this, new EnergyDownPower(p, 1)));
            addToBot(new LoseEnergyAction(1));
        });
        addMove(Intent.UNKNOWN, mi->{});
        addMove(Intent.BUFF, mi->{
            respawn();
            addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));
            addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM)));
        });
    }
    
    public FortuneBananAdvisor(float x, float y) {
        this(x, y, AbstractDungeon.monsterRng.randomBoolean());
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, channelCount * (inClass ? -1 : 1), ChannelPower.ChannelType.OFFCLASS_CLASSROOM), 0));
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 2)) {
            setMove(3);
        } else if (halfDead) {
            setMove(2);
        } else if (inClass) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void processChange(boolean isInClass) {
        inClass = isInClass;
        setImg();
        rollMove();
        createIntent();
    }

    @Override
    public void setImg(){
        if (inClass) img = ImageMaster.loadImage(inClassImg);
        else img = ImageMaster.loadImage(outClassImg);
    }

    @Override
    public void die() {
        super.die();
        processDie(this);
    }
}
