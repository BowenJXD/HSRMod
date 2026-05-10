package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.defect.LightningOrbPassiveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.card.AbstractSignatureCard;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.signature.utils.internal.SignatureHelperInternal;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Aglaea2 extends BaseCard {
    public static final String ID = Aglaea2.class.getSimpleName();

    public Aglaea2() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        if (m != null)
            addToBot(new VFXAction(new ClashEffect(m.hb.cX, m.hb.cY)));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.NONE));
        for (AbstractOrb orb : p.orbs) {
            if (Objects.equals(orb.ID, Lightning.ORB_ID) || upgraded) {
                ModHelper.triggerPassiveTo(orb, m);
            }
        }
        boolean unlocked = SignatureHelper.isUnlocked(HSRMod.makePath(ID));
        if (!unlocked) {
            for (AbstractCard c : p.drawPile.group) {
                if (c instanceof AbstractSignatureCard && ((AbstractSignatureCard) c).hasSignature && !SignatureHelper.isUnlocked(c.cardID) && c != this) {
                    return;
                }
            }
            for (AbstractCard c : p.hand.group) {
                if (c instanceof AbstractSignatureCard && ((AbstractSignatureCard) c).hasSignature && !SignatureHelper.isUnlocked(c.cardID) && c != this) {
                    return;
                }
            }
            for (AbstractCard c : p.discardPile.group) {
                if (c instanceof AbstractSignatureCard && ((AbstractSignatureCard) c).hasSignature && !SignatureHelper.isUnlocked(c.cardID) && c != this) {
                    return;
                }
            }
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        }
    }
}
