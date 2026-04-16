package hsrmod.cardsV2.Trailblaze;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.DivinityStance;
import hsrmod.cards.BaseCard;
import hsrmod.cardsV2.Remembrance.Pollux1;
import hsrmod.cardsV2.Remembrance.Pollux2;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.RuinousIrontombPower;
import hsrmod.utils.ModHelper;

public class Phainon2 extends BaseCard {
    public static final String ID = Phainon2.class.getSimpleName();
    
    public Phainon2() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
        MultiCardPreview.add(this, new Khaslana1(), new Khaslana2(), new Khaslana3(), new Khaslana4());
        MultiCardPreview.horizontalOnly(this);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        
        addToBot(new ApplyPowerAction(p, p, new RuinousIrontombPower(p, 8)));
        ModHelper.addToBotAbstract(() -> {
            ModHelper.addToBotAbstract(() -> {
                ModHelper.findCards(c -> c==this).forEach(r -> {
                    r.group.removeCard(r.card);
                });
            });
        });
        addToBot(new MakeTempCardInHandAction(new Khaslana1()));
        addToBot(new MakeTempCardInHandAction(new Khaslana2()));
        addToBot(new MakeTempCardInHandAction(new Khaslana3()));
        addToBot(new ChangeStanceAction(DivinityStance.STANCE_ID));
    }
}
