package hsrmod.powers.uniqueDebuffs;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.utils.GeneralUtil;

public class SoulstruckPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(SoulstruckPower.class.getSimpleName());
    
    public SoulstruckPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = GeneralUtil.tryFormat(DESCRIPTIONS[0], amount);
    }

    @Override
    public void atStartOfTurn() {
        super.atStartOfTurn();
        remove(1);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (m == owner) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, DoTPower.getRandomDoTPower(owner, AbstractDungeon.player, amount), amount));
        }
    }
}
