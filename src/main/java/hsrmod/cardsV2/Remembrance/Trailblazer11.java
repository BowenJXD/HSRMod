package hsrmod.cardsV2.Remembrance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.FuturePower;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

public class Trailblazer11 extends BaseCard {
    public static final String ID = Trailblazer11.class.getSimpleName();
    
    public Trailblazer11() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
        isMultiDamage = true;
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Trailblazer9"));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        ModHelper.addToBotAbstract(() -> {
            GeneralUtil.getRandomElements(p.exhaustPile.group, AbstractDungeon.cardRandomRng, magicNumber).forEach(c -> {
                addToTop(new ExhaustToHandAction(c));
            });
        });
        if (ModHelper.getPowerCount(p, FuturePower.POWER_ID) > 12) {
            addToBot(new TriggerPowerAction(p.getPower(FuturePower.POWER_ID)));
        }
    }
}
