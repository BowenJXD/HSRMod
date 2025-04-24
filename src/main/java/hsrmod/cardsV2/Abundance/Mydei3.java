package hsrmod.cardsV2.Abundance;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;

public class Mydei3 extends BaseCard {
    public static final String ID = Mydei3.class.getSimpleName();
    
    public Mydei3() {
        super(ID);
        exhaust = true;
        tags.add(CustomEnums.FOLLOW_UP);
        isMultiDamage = true;
        cardsToPreview = new Mydei1(true);
    }
    
    public Mydei3(boolean asPreview) {
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
        shout(0);
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (ModHelper.check(mon))
                addToBot(new VFXAction(new InflameEffect(mon)));
        }
        addToBot(new LoseHPAction(p, p, magicNumber));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SMASH));
        addToBot(new ChangeStanceAction(new NeutralStance()));
        AbstractCard card = new Mydei1();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInDiscardAction(card, 1));
    }
}
