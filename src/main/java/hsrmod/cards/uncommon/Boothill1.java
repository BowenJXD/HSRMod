package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BreakEffectPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class Boothill1 extends BaseCard {
    public static final String ID = Boothill1.class.getSimpleName();

    int costCache = -1;

    public Boothill1() {
        super(ID);
        costCache = cost;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        AtomicBoolean didBreak = new AtomicBoolean(false);
        for (int i = 0; i < magicNumber; i++) {
            addToBot(
                    new ElementalDamageAction(
                            m,
                            new ElementalDamageInfo(this),
                            AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                            ci -> {
                                if (ci.didBreak) {
                                    didBreak.set(true);
                                    int val = ToughnessPower.getStackLimit(m);
                                    addToBot(new BreakDamageAction(m, new DamageInfo(p, val)));
                                }
                            }
                    )
            );
        }
        ModHelper.addToBotAbstract(() -> {
            if (!didBreak.get()) {
                returnToHand = true;
                retain = true;
                setCostForTurn(costCache);
            }
        });
    }
}
