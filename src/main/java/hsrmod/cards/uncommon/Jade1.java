package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.PostDrawSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.utils.SubscribeManager;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Jade1 extends BaseCard implements PostDrawSubscriber {
    public static final String ID = Jade1.class.getSimpleName();
    
    boolean isDetecting = false;
    
    public Jade1() {
        super(ID);
        tags.add(FOLLOW_UP);
        BaseMod.subscribe(this);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = AbstractDungeon.player.hand.size();
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        addToBot(new DrawCardAction(p, magicNumber));
        
        addToBot(
                new ElementalDamageAllAction(
                        damage,
                        ElementType.Quantum,
                        2,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                )
        );
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        isDetecting = false;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        isDetecting = true;
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        if (SubscribeManager.checkSubscriber(this)
                && AbstractDungeon.player.hand.contains(this)
                && isDetecting 
                && !this.followedUp) {
            this.followedUp = true;
            addToBot(new FollowUpAction(this));
        }
    }
}
