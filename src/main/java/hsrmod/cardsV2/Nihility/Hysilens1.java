package hsrmod.cardsV2.Nihility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.powers.uniqueDebuffs.SoulstruckPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.PreDoTDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class Hysilens1 extends BaseCard implements PreDoTDamageSubscriber {
    public static final String ID = Hysilens1.class.getSimpleName();

    public Hysilens1() {
        super(ID);
        isEthereal = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (upgraded) {
            addToBot(new ApplyPowerAction(m, p, new BleedingPower(m, p, magicNumber), magicNumber));
        }
        addToBot(new ApplyPowerAction(m, p, new SoulstruckPower(m, 1), 1));
    }

    @Override
    public float preDoTDamage(ElementalDamageInfo info, AbstractCreature target, DoTPower power) {
        if (SubscriptionManager.checkSubscriber(this) && power instanceof BleedingPower && info.output >= 40) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        }
        return info.output;
    }
}
