package hsrmod.cardsV2.TheHunt;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.utils.ModHelper;

import java.util.Objects;

public class Feixiao1 extends BaseCard {
    public static final String ID = Feixiao1.class.getSimpleName();

    public Feixiao1() {
        super(ID);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
        addToBot(
                new ElementalDamageAction(
                        m,
                        new ElementalDamageInfo(this),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL
                )
        );
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        super.triggerOnOtherCardPlayed(c);
        ModHelper.addToBotAbstract(() -> {
            if (c.type == CardType.ATTACK
                    && !Objects.equals(c.cardID, cardID)
                    && (AbstractDungeon.player.drawPile.isEmpty() || !Objects.equals(AbstractDungeon.player.drawPile.getTopCard().cardID, cardID))) {
                flash();
                addToTop(new MakeTempCardInDrawPileAction(new Feixiao1(), 1, false, true));
            }
        });
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        glowColor = AbstractDungeon.player.drawPile.findCardById(cardID) != null ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
}
