package hsrmod.cardsV2.Propagation;

import basemod.interfaces.PostBattleSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.uniqueBuffs.MilitaryMeritPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.SubscriptionManager;

public class Cerydra1 extends BaseCard implements PostBattleSubscriber {
    public static final String ID = Cerydra1.class.getSimpleName();
    
    int turn = 0;

    public Cerydra1() {
        super(ID);
        setBaseEnergyCost(130);
        tags.add(CustomEnums.ENERGY_COSTING);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isMultiDamage = true;
        target = CardTarget.ALL_ENEMY;
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        turn++;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        if (m != null) {
            addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        } else {
            addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
        addToBot(new ApplyPowerAction(p, p, new MilitaryMeritPower(p, 2, magicNumber), 2));
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (SubscriptionManager.checkSubscriber(this) && turn <= 1) {
            SignatureHelper.unlock(HSRMod.makePath(ID), true);
        }
    }
}
