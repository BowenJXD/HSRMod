package hsrmod.cardsV2.Abundance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Mydei3 extends BaseCard {
    public static final String ID = Mydei3.class.getSimpleName();
    
    public Mydei3() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
        isMultiDamage = true;
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        addToBot(new FollowUpAction(this, null, false));
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SMASH));
        addToBot(new ChangeStanceAction(new NeutralStance()));
    }
}
