package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.defect.LightningOrbPassiveAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
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
    }
}
