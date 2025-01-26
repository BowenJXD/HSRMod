package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

public class Feixiao2 extends BaseCard {
    public static final String ID = Feixiao2.class.getSimpleName();
    
    public int baseDamageCache = 0;
    
    public Feixiao2() {
        super(ID);
        selfRetain = true;
        baseDamageCache = baseDamage;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (c.type == CardType.ATTACK) baseDamage += magicNumber;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        if (!followedUp) addToBot(new FollowUpAction(this));
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.topLevelEffects.add(new PortraitDisplayEffect("Feixiao"));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.play(ID));
        
        addToBot(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        baseDamage = baseDamageCache;
    }
}
