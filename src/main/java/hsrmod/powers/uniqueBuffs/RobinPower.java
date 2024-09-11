package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BuffPower;
import hsrmod.powers.misc.EnergyPower;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class RobinPower extends BuffPower {
    public static final String POWER_ID = HSRMod.makePath(RobinPower.class.getSimpleName());

    public RobinPower(AbstractCreature owner, boolean upgraded) {
        super(POWER_ID, owner, upgraded);
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) reducePower(1);
        if (amount == 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {        
        if (card.hasTag(FOLLOW_UP)) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
            addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, 1), 1));
            if (upgraded) addToBot(new ApplyPowerAction(owner, owner, new EnergyPower(owner, 20), 20));
        }
        else if (owner.hasPower(LoseStrengthPower.POWER_ID)) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -1), -1));
            addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, -1), -1));
        }
    }
}
