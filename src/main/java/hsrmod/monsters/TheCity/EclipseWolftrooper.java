package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.MoonRagePower;

public class EclipseWolftrooper extends BaseMonster {
    public static final String ID = EclipseWolftrooper.class.getSimpleName();
    
    public EclipseWolftrooper(float x, float y) {
        super(ID, 169F, 169F, x, y);
        
        setDamages(2);
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (hasPower(MoonRagePower.POWER_ID)) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                addToBot(new GainBlockAction(m, this, 4));
            }
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK_DEFEND, this.damage.get(0).base, 2, true);
    }
}
