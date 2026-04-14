package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;

public class Demiurge1 extends BaseCard {
    public static final String ID = Demiurge1.class.getSimpleName();

    public Demiurge1() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.CHRYSOS_HEIR);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (upgraded) {
            for (AbstractOrb orb : p.orbs) {
                addToBot(new TriggerPassiveAction(orb));
            }
        } else if (p.filledOrbCount() > 0) {
            addToBot(new TriggerPassiveAction(p.orbs.get(0)));
        }
    }
}
