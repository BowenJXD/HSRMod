package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

public class Boothill2 extends BaseCard {
    public static final String ID = Boothill2.class.getSimpleName();

    int costCache = -1;
    int unlockCount = 0;
    int cachedTurn = -1;
    
    public Boothill2() {
        super(ID);
        costCache = cost;
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        if (cachedTurn == -1 || GameActionManager.turn != cachedTurn) {
            cachedTurn = GameActionManager.turn;
            unlockCount = 0;
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        returnToHand = false;
        addToBot(new ElementalDamageAction(m,
                new ElementalDamageInfo(this),
                AbstractGameAction.AttackEffect.SLASH_HEAVY));
        ModHelper.addToBotAbstract(() -> {
            if (!m.hasPower(BrokenPower.POWER_ID)) {
                returnToHand = true;
                setCostForTurn(costCache);
            }
        });
        
        if (!SignatureHelper.isUnlocked(cardID)) {
            unlockCount++;
            if (unlockCount >= 5) {
                SignatureHelper.unlock(cardID, true);
            }
        }
    }
}
