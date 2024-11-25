package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.BarrierPower;
import hsrmod.powers.enemyOnly.ChannelPower;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;

public class MrDomescreen extends AbstractMonster {
    public static final String ID = MrDomescreen.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int dmg = 6;
    int chargeGain = 50;
    int chargeLoss = 80;
    boolean isSurprise = false;
    boolean lastMoved1 = false;
    
    public MrDomescreen(boolean isSurprise, float x, float y) {
        super(NAME, HSRMod.makePath(ID), 52, 0F, -15.0F, 200F, 256F, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        this.isSurprise = isSurprise;
        
        if (AbstractDungeon.ascensionLevel >= 19) {
            dmg = 22;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            dmg = 20;
        } else {
            dmg = 18;
        }
        
        this.damage.add(new DamageInfo(this, dmg));
    }
    
    public MrDomescreen(float x, float y) {
        this(true, x, y);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, isSurprise), 0));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                isSurprise = ModHelper.getPowerCount(this, ChannelPower.POWER_ID) > 0;
                if (lastMoved1 && isSurprise)
                    addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, isSurprise), -2));
                else
                    addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, chargeGain), chargeGain));
                lastMoved1 = true;
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(p, this, new EnergyPower(p, -chargeLoss), -chargeLoss));
                lastMoved1 = false;
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        isSurprise = ModHelper.getPowerCount(this, ChannelPower.POWER_ID) > 0;
        
        if (isSurprise) {
            setMove(MOVES[0], (byte) 1, Intent.UNKNOWN);
        } else {
            setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        isSurprise = ModHelper.getPowerCount(this, ChannelPower.POWER_ID) > 0;
        if (isSurprise) {
            setMove(MOVES[0], (byte) 1, Intent.UNKNOWN);
        } else {
            setMove(MOVES[1], (byte) 2, Intent.ATTACK, this.damage.get(0).base);
        }
        createIntent();
    }
}
