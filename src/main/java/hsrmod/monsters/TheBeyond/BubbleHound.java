package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.powers.enemyOnly.SoulGladRevelPower;
import hsrmod.utils.ModHelper;

public class BubbleHound extends BaseMonster {
    public static final String ID = BubbleHound.class.getSimpleName();
    
    int explosionDmg = 5;
    int soulGladAmount = 0;
    
    public BubbleHound(float x, float y) {
        super(ID, 256, 256, x, y);
        
        setDamagesWithAscension(17);
        soulGladAmount = ModHelper.specialAscension(type) ? 1 : 0;
        explosionDmg = ModHelper.moreDamageAscension(type) ? 3 : 4;
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[1], MOVES[2], () -> {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDying && !m.isDead) {
                    addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(m, explosionDmg, DamageInfo.DamageType.THORNS, ElementType.Physical, 2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToBot(new ApplyPowerAction(m, this, new BleedingPower(m, m, 1), 1));
                }
            }
        })));
        addToBot(new ApplyPowerAction(this, this, new SoulGladRevelPower(this, soulGladAmount, 4), soulGladAmount));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK, this.damage.get(0).base);
    }
}
