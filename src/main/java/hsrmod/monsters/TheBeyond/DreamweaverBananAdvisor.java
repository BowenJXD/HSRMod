package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hsrmod.misc.PathDefine;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChannelPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class DreamweaverBananAdvisor extends BaseMonster implements IBanana {
    public static final String ID = DreamweaverBananAdvisor.class.getSimpleName();
    
    int drawGain = 2;
    int drawLoss = 2;
    int channelCount = 1;
    boolean inClass = true;
    String outClassImg = PathDefine.MONSTER_PATH + "WinderGoon" + ".png";
    String inClassImg = PathDefine.MONSTER_PATH + DreamweaverBananAdvisor.ID + ".png";
    
    public DreamweaverBananAdvisor(float x, float y, boolean inClass) {
        super(ID, 200F, 256F, x, y);
        this.inClass = inClass;
        setImg();
        int drawLoss = specialAs ? 3 : 2;
        channelCount = specialAs ? 2 : 1;
        
        addMove(Intent.UNKNOWN, mi->{
            addToBot(new VFXAction(new ShockWaveEffect(hb.cX, hb.cY, Color.FOREST, ShockWaveEffect.ShockWaveType.NORMAL), 0.2f));
            if (lastTwoMoves((byte) 0))
                addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM), -channelCount));
            if (p.hasPower(DrawReductionPower.POWER_ID))
                addToBot(new ReducePowerAction(p, this, DrawReductionPower.POWER_ID, drawGain));
            else
                addToBot(new DrawCardAction(p, drawGain));
        });
        addMoveA(Intent.ATTACK_DEBUFF, 15, mi->{
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AttackAnim.SLOW);
            addToBot(new ApplyPowerAction(p, this, new DrawReductionPower(p, drawLoss)));
        });
        addMove(Intent.UNKNOWN, mi->{});
        addMove(Intent.BUFF, mi->{
            respawn();
            addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));
            addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM)));
        });
    }
    
    public DreamweaverBananAdvisor(float x, float y) {
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
