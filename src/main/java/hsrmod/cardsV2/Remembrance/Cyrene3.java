package hsrmod.cardsV2.Remembrance;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.RecollectionPower;

public class Cyrene3 extends BaseCard {
    public static final String ID = Cyrene3.class.getSimpleName();

    public Cyrene3() {
        super(ID);
        selfRetain = true;
        MultiCardPreview.add(this, new Cyrene4(), new Demiurge1());
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        addToBot(new ApplyPowerAction(p, p, new RecollectionPower(p, magicNumber)));
    }
}
