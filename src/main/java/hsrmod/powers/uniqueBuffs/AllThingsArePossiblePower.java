package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.BasePower;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.SuspicionPower;

public class AllThingsArePossiblePower extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(AllThingsArePossiblePower.class.getSimpleName());
    
    public AllThingsArePossiblePower(boolean upgraded) {
        super(POWER_ID, upgraded);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY 
                || card.target == AbstractCard.CardTarget.ALL) {
            flash();
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!monster.isDeadOrEscaped() && monster.hasPower(BrokenPower.POWER_ID)) {
                    if (upgraded)
                        addToBot(new ApplyPowerAction(monster, owner, new SuspicionPower(monster, 1), 1));
                    addToBot(new TriggerDoTAction(monster));
                }
            }
        }
        else if (card.target == AbstractCard.CardTarget.ENEMY) {
            AbstractMonster monster = (AbstractMonster) action.target;
            if (!monster.isDeadOrEscaped() && monster.hasPower(BrokenPower.POWER_ID)) {
                flash();
                addToBot(new TriggerDoTAction(monster));
            }
        }
    }
}
