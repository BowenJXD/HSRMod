package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.ChannelPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;

public class MrDomescreen extends BaseMonster {
    public static final String ID = MrDomescreen.class.getSimpleName();

    int chargeGain = 50;
    int chargeLoss = 80;
    int channelCount = 1;
    boolean isSurprise = false;
    boolean lastMoved1 = false;
    String surpriseImg = PathDefine.MONSTER_PATH + MrDomescreen.ID + ".png";
    String frightImg = PathDefine.MONSTER_PATH + Assistanana.ID + ".png";

    public MrDomescreen(boolean isSurprise, float x, float y) {
        super(ID, 0, 15, 150F, 256F, x, y);
        if (isSurprise) img = ImageMaster.loadImage(frightImg);
        this.isSurprise = isSurprise;
        
        setDamagesWithAscension(20);
        chargeLoss = ModHelper.specialAscension(type) ? 120 : 80;
        channelCount = specialAs ? 2 : 1;
    }

    public MrDomescreen(float x, float y) {
        this(true, x, y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, channelCount * (isSurprise ? 1 : -1), ChannelPower.ChannelType.SURPRISE_FRIGHT), 0));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                isSurprise = ModHelper.getPowerCount(this, ChannelPower.POWER_ID) > 0;
                if (lastMoved1 && isSurprise) {
                    addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.SURPRISE_FRIGHT), -channelCount));
                    ModHelper.addToBotAbstract(this::setSurprise);
                }
                else
                    addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, chargeGain), chargeGain));
                lastMoved1 = true;
                break;
            case 2:
                addToBot(new VFXAction(new ShockWaveEffect(hb.cX, hb.cY, Color.NAVY, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.2f));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, -chargeLoss), -chargeLoss));
                lastMoved1 = false;
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        setSurprise();
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        setSurprise();
        createIntent();
    }

    private void setSurprise() {
        isSurprise = ModHelper.getPowerCount(this, ChannelPower.POWER_ID) > 0;
        if (isSurprise) {
            setMove(MOVES[0], (byte) 1, Intent.UNKNOWN);
            this.img = ImageMaster.loadImage(surpriseImg);
        } else {
            setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
            this.img = ImageMaster.loadImage(frightImg);
        }
    }
}
