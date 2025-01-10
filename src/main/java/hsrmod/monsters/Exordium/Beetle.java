package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
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
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.BarrierPower;
import hsrmod.utils.ModHelper;

public class Beetle extends BaseMonster {
    public static final String ID = Beetle.class.getSimpleName();
    
    public Beetle(float x, float y) {
        super(ID, 0F, -15.0F, 165F, 197F, x, y);
        
        this.setDamagesWithAscension(6);
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (ModHelper.specialAscension(type)) {
            addToBot(new ApplyPowerAction(this, this, new BarrierPower(this, 1), 1));
        }
    }

    @Override
    public void takeTurn() {
        addToBot(new AnimateHopAction(this));
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(this, this, new BarrierPower(this, 1), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base);
    }
}
