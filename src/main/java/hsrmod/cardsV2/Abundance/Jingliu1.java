package hsrmod.cardsV2.Abundance;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;
import com.megacrit.cardcrawl.vfx.combat.IceShatterEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.WaterDropEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.signature.utils.SignatureHelper;

public class Jingliu1 extends BaseCard {
    public static final String ID = Jingliu1.class.getSimpleName();

    boolean canUnlock;

    public Jingliu1() {
        super(ID);
        cardsToPreview = new Jingliu2(true);
        exhaust = true;
    }

    public Jingliu1(boolean asPreview) {
        super(ID);
        exhaust = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        if (cardsToPreview != null) cardsToPreview.upgrade();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new RoomTintEffect(Color.CYAN, 0.5F)));
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new LoseHPAction(p, p, magicNumber));
            addToBot(new VFXAction(new WaterDropEffect(p.hb.cX, p.hb.cY)));
            // addToBot(new VFXAction(new IceShatterEffect(m.hb.cX, m.hb.cY)));
            addToBot(new VFXAction(new BlizzardEffect(6, false)));
            addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                    ci -> {
                        if ((ci.target.isDying || ci.target.currentHealth <= 0) && !ci.target.halfDead && canUnlock) {
                            SignatureHelper.unlock(cardID, true);
                        }
                    }));
        }
        shout(0);
        AbstractCard card = new Jingliu2();
        if (upgraded) card.upgrade();
        addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
        addToBot(new ChangeStanceAction(new CalmStance()));

        if ((p.currentHealth + TempHPField.tempHp.get(p) <= magicNumber * magicNumber && !p.hasPower(IntangiblePlayerPower.POWER_ID))
                || (p.currentHealth <= magicNumber))
            canUnlock = !SignatureHelper.isUnlocked(cardID);
    }
}
