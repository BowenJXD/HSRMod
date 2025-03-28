package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChannelPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.PathDefine;

public class Assistanana extends BaseMonster implements IBanana {
    public static final String ID = Assistanana.class.getSimpleName();
    
    int chargeGain = 50;
    int chargeLoss = 80;
    int channelCount = 1;
    boolean inClass = true;
    String outClassImg = PathDefine.MONSTER_PATH + MrDomescreen.ID + ".png";
    String inClassImg = PathDefine.MONSTER_PATH + Assistanana.ID + ".png";
    
    public Assistanana(float x, float y, boolean inClass) {
        super(ID, 0, 15, 150F, 256F, x, y);
        this.inClass = inClass;
        setImg();
        chargeLoss = specialAs ? 160 : 80;
        channelCount = specialAs ? 2 : 1;
        
        addMove(Intent.UNKNOWN, mi->{
            if (lastTwoMoves((byte) 0))
                addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM), -channelCount));
            addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, chargeGain)));
        });
        addMoveA(Intent.ATTACK_DEBUFF, 17, mi->{
            addToBot(new VFXAction(new ShockWaveEffect(hb.cX, hb.cY, Color.NAVY, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.2f));
            attack(mi, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, -chargeLoss), -chargeLoss));
        });
        addMove(Intent.UNKNOWN, mi->{});
        addMove(Intent.BUFF, mi->{
            respawn();
            addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));
            addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM)));
        });
    }
    
    public Assistanana(float x, float y) {
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
