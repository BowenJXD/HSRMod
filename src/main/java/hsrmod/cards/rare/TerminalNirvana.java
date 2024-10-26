package hsrmod.cards.rare;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.subscribers.SubscriptionManager;

public class TerminalNirvana extends BaseCard implements PostBattleSubscriber {
    public static final String ID = TerminalNirvana.class.getSimpleName();
    
    int magicNumberCache = -1;
    
    public TerminalNirvana() {
        super(ID);
        exhaust = true;
        selfRetain = true;
        energyCost = magicNumber;
        magicNumberCache = magicNumber;
        tags.add(CustomEnums.ENERGY_COSTING);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
        energyCost = magicNumber;
        magicNumberCache = magicNumber;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
        
        if (upgraded) {
            upgradeMagicNumber(magicNumber);
            energyCost *= 2;
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (SubscriptionManager.checkSubscriber(this)) {
            energyCost = magicNumberCache;
            upgradeMagicNumber(magicNumberCache - magicNumber);
        }
    }
}
