package androidTestMod.cardsV2.Preservation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;
import androidTestMod.cards.BaseCard;
import androidTestMod.utils.ModHelper;

public class DayOneOfMyNewLife extends BaseCard {
    public static final String ID = DayOneOfMyNewLife.class.getSimpleName();
    
    public DayOneOfMyNewLife() {
        super(ID);
        isInnate = true;
        exhaust = true;
    }
    
    @Override
    public void upgrade() {
        super.upgrade();
    }
    
    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FastingEffect(p.hb.cX, p.hb.cY, Color.WHITE)));
        int plate = magicNumber - ModHelper.getPowerCount(p, PlatedArmorPower.POWER_ID);
        if (plate > 0) addToBot(new ApplyPowerAction(p, p, new PlatedArmorPower(p, plate), plate));

        addToBot(new GainBlockAction(p, block));
        addToBot(new GainBlockAction(m, 4));
    }
}
