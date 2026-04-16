package hsrmod.cardsV2.Remembrance;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.TransformCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;
import com.megacrit.cardcrawl.vfx.combat.ThirdEyeEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.uniqueBuffs.NewbudPower;
import hsrmod.utils.ModHelper;

import java.awt.*;

public class Pollux1 extends BaseCard {
    public static final String ID = Pollux1.class.getSimpleName();

    public Pollux1() {
        super(ID);
        returnToHand = true;
        tags.add(CustomEnums.TERRITORY);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean result = super.canUse(p, m);
        if (result && p.filledOrbCount() == 0) {
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return result;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (p.filledOrbCount() <= 0 || p.orbs.get(0) == null) {
            return;
        }
        addToBot(new VFXAction(new BorderLongFlashEffect(Color.PURPLE)));
        addToBot(new VFXAction(new RedFireballEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY, 10 * (timesUpgraded+1))));
        addToBot(new VFXAction(new ThirdEyeEffect(m.hb.cX, m.hb.cY)));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.FIRE));
        ModHelper.evokeTo(p.orbs.get(0), m);
        int newbudAmount = p.orbs.get(0).evokeAmount;
        if (newbudAmount > 0) {
            addToBot(new ApplyPowerAction(p, p, new NewbudPower(p, newbudAmount), newbudAmount));
        }
        ModHelper.addToBotAbstract(() -> {
            ModHelper.addToBotAbstract(() -> {
                if (p.filledOrbCount() == 0 && p.hand.contains(this)) {
                    addToTop(new TransformCardInHandAction(p.hand.group.indexOf(this), new Pollux3()));
                }
            });
        });
    }
}
