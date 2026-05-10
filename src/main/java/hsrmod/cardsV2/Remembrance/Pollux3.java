package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ThirdEyeEffect;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.uniqueBuffs.LostNetherlandPower;
import hsrmod.powers.uniqueBuffs.NewbudPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.signature.utils.internal.SignatureHelperInternal;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.List;

public class Pollux3 extends BaseCard {
    public static final String ID = Pollux3.class.getSimpleName();

    public Pollux3() {
        super(ID);
        tags.add(CustomEnums.TERRITORY);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = ModHelper.getPowerCount(AbstractDungeon.player, NewbudPower.POWER_ID);
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new VFXAction(new BorderLongFlashEffect(Color.PURPLE)));
        int dmg = damage / magicNumber;
        ElementalDamageAction action = new ElementalDamageAction(m, new ElementalDamageInfo(p, dmg, DamageInfo.DamageType.NORMAL, elementType, tr / magicNumber), AbstractGameAction.AttackEffect.FIRE, result -> {
            if (ModHelper.check(result.target))
                AbstractDungeon.topLevelEffects.add(new ThirdEyeEffect(result.target.hb.cX, result.target.hb.cY));
        });
        action.setCallback(ci -> {
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    SignatureHelper.unlock(HSRMod.makePath(Castorice3.ID), true);
                }
        });
        addToBot(new BouncingAction(m, magicNumber, action));
        addToBot(new RemoveSpecificPowerAction(p, p, NewbudPower.POWER_ID));
        addToBot(new RemoveSpecificPowerAction(p, p, LostNetherlandPower.POWER_ID));
    }
}
