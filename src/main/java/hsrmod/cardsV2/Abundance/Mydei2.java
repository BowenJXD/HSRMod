package hsrmod.cardsV2.Abundance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;

public class Mydei2 extends BaseCard {
    public static final String ID = Mydei2.class.getSimpleName();

    public Mydei2() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
        cardsToPreview = new Mydei3(true);
    }
    
    public Mydei2(boolean asPreview) {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        addToBot(new FollowUpAction(this, null, false));
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new VFXAction(new IronWaveEffect(p.hb.cX, p.hb.cY, m.hb.cX)));
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractCard card = new Mydei3();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInDrawPileAction(card, 1, false, true, false));
        addToBot(new ChangeStanceAction(new DivinityStance()));
    }
}
