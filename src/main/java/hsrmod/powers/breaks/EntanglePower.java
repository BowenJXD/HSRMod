package hsrmod.powers.breaks;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

public class EntanglePower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(EntanglePower.class.getSimpleName());

    int damage = 2;
    private final int stackLimit = 5;

    public EntanglePower(AbstractCreature owner, int Amount) {
        super(POWER_ID, owner, Amount);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], stackLimit, damage, getDamage());
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= stackLimit) {
            amount = stackLimit;
        }
    }

    int getDamage(){
        return amount * damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL)
            stackPower(1);
        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new PoisonLoseHpAction(this.owner, this.owner, getDamage(), AbstractGameAction.AttackEffect.POISON));
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
