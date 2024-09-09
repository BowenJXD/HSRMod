package hsrmod.powers.uniqueBuffs;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ReduceToughnessAction;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BrokenPower;

public class Trailblazer5Power extends PowerPower {
    public static final String POWER_ID = HSRMod.makePath(Trailblazer5Power.class.getSimpleName());

    AbstractCard cardCache;
    
    public Trailblazer5Power() {
        super(POWER_ID);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        cardCache = card;
    }

    public void trigger(ReduceToughnessAction action) {
        if (cardCache == null) return;
        if (!action.target.hasPower(BrokenPower.POWER_ID)) return;
        flash();
        cardCache = null;
        addToBot(new BreakDamageAction(action.target, new DamageInfo(owner, action.toughnessReduction)));
    }
}
