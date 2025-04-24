package hsrmod.cardsV2.Abundance;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.subscribers.SubscriptionManager;

public class Mydei1 extends BaseCard implements OnPlayerDamagedSubscriber {
    public static final String ID = Mydei1.class.getSimpleName();

    int threshold = 5;

    public Mydei1() {
        super(ID);
        selfRetain = true;
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
        cardsToPreview = new Mydei2(true);
    }
    
    public Mydei1(boolean asPreview) {
        super(ID);
        selfRetain = true;
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        cardsToPreview.upgrade();
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        BaseMod.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new VerticalAuraEffect(Color.RED, p.hb.cX, p.hb.cY)));
        addToBot(new ChangeStanceAction(new WrathStance()));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractCard card = new Mydei2();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
        baseMagicNumber = magicNumber = 0;
    }
    
    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (SubscriptionManager.checkSubscriber(this) 
                && i > 0 
                && !AbstractDungeon.actionManager.turnHasEnded) {
            baseMagicNumber = magicNumber += i;
            initializeDescription();
            if (magicNumber >= threshold && !followedUp) {
                addToBot(new FollowUpAction(this));
                followedUp = true;
            }
        }
        return i;
    }
}
