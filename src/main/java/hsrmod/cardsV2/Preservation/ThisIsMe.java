package hsrmod.cardsV2.Preservation;

import hsrmod.cards.BaseCard;
import hsrmod.utils.ModHelper;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;

public class ThisIsMe extends BaseCard {
    public static final String ID = ThisIsMe.class.getSimpleName();
    
    public ThisIsMe() {
        super(ID);
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FastingEffect(p.hb.cX, p.hb.cY, Color.WHITE)));
        int dex = magicNumber;
        if (!upgraded) dex -= ModHelper.getPowerCount(p, DexterityPower.POWER_ID);
        if (dex > 0) addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, dex), dex));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, 4));
    }
}
