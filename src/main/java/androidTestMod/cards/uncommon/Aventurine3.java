package androidTestMod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.ElementalDamageInfo;

public class Aventurine3 extends BaseCard {
    public static final String ID = Aventurine3.class.getSimpleName();
    
    public int dmg = 0;
    
    public Aventurine3() {
        super(ID);
        exhaust = true;
        selfRetain = true;
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void upgrade() {
        super.upgrade();
    }

    @Override
    public void atTurnStartPreDraw() {
        super.atTurnStartPreDraw();
        if (!followedUp) addToBot(new FollowUpAction(this));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        if (!followedUp) addToBot(new FollowUpAction(this));
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null) addToBot(new VFXAction(new FlickCoinEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.5f));
        if (damage > 1) {
            dmg = AbstractDungeon.cardRandomRng.random(1, damage);
            addToBot(new ElementalDamageAction(
                    m, 
                    new ElementalDamageInfo(this, dmg), 
                    AbstractGameAction.AttackEffect.NONE
            ));
        }
        if (block > 1) {
            int blk = AbstractDungeon.cardRandomRng.random(1, block);
            addToBot(new GainBlockAction(p, p, blk));
        }
    }
}
