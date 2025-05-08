package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

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
                            new Consumer<ElementalDamageAction.CallbackInfo>() {
                                @Override
                                public void accept(ElementalDamageAction.CallbackInfo ci) {
                                    if (ci.didBreak) {
                                        didBreak.set(true);
                                        int val = ToughnessPower.getStackLimit(m);
                                        Boothill1.this.addToBot(new BreakDamageAction(m, new DamageInfo(p, val)));
                                    }
                                }
                            }
                    )
            );
        }
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                if (!didBreak.get()) {
                    returnToHand = true;
                    retain = true;
                    Boothill1.this.setCostForTurn(costCache);
                }
            }
        });
    }
}
