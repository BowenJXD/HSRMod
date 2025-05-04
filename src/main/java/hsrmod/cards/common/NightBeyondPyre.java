package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.utils.ModHelper;

public class NightBeyondPyre extends BaseCard {
    public static final String ID = NightBeyondPyre.class.getSimpleName();
    
    public NightBeyondPyre() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
        exhaust = false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasPower(BreakEfficiencyPower.POWER_ID))
            addToBot(new ApplyPowerAction(p, p, new BreakEfficiencyPower(p, magicNumber), magicNumber));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                if (p.hand.isEmpty())
                    NightBeyondPyre.this.addToBot(new DrawCardAction(magicNumber));
            }
        });
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        glowColor = !AbstractDungeon.player.hasPower(BreakEfficiencyPower.POWER_ID) ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
}
