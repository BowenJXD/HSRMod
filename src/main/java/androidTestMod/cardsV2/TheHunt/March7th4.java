package androidTestMod.cardsV2.TheHunt;

import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightBulbEffect;

public class March7th4 extends BaseCard {
    public static final String ID = March7th4.class.getSimpleName();

    int magicNumberCache;
    
    public March7th4() {
        super(ID);
        tags.add(CustomEnums.FOLLOW_UP);
        magicNumberCache = magicNumber;
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        exhaust = false;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        if (c != this && c.upgraded) {
            upgradeMagicNumber(1);
            if (magicNumber >= 7 && !followedUp) {
                followedUp = true;
                addToBot(new FollowUpAction(this));
            }
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new LightBulbEffect(p.hb)));
        addToBot(new DrawCardAction(magicNumber));
        upgradeMagicNumber(magicNumberCache - magicNumber);
    }
}
