package hsrmod.monsters.TheCity;

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
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.BarrierPower;
import hsrmod.utils.ModHelper;

public class ObedientDracolion extends BaseMonster {
    public static final String ID = ObedientDracolion.class.getSimpleName();
    
    int weakAmount = 1;
    
    public ObedientDracolion(float x, float y) {
        super(ID, 104F, 200F, x, y);
        
        setDamagesWithAscension(9);
        weakAmount = ModHelper.specialAscension(type) ? 2 : 1;
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(p, this, new WeakPower(this, weakAmount, true), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
}
