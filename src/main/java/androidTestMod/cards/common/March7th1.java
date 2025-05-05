package androidTestMod.cards.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import androidTestMod.actions.CleanAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.uniqueBuffs.ReinforcePower;

public class March7th1 extends BaseCard {
    public static final String ID = March7th1.class.getSimpleName();
    
    public March7th1() {
        super(ID);
        this.cardsToPreview = new March7th2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FastingEffect(p.hb.cX, p.hb.cY, Color.PINK)));
        addToBot(new GainBlockAction(p, p, block));
        AbstractPower power = p.getPower(ReinforcePower.POWER_ID);
        if (power != null)
            ((ReinforcePower) power).block = upgraded ? block : 0;
        else
            addToBot(new ApplyPowerAction(p, p, new ReinforcePower(p, upgraded ? block : 0)));
        addToBot(new CleanAction(p, 1, false));
    }
}
