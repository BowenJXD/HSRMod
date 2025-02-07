package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;

public class Jiaoqiu1 extends BaseCard {
    public static final String ID = Jiaoqiu1.class.getSimpleName();

    public Jiaoqiu1() {
        super(ID);
        setBaseEnergyCost(100);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
        cardsToPreview = new Jiaoqiu2();
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        float cX = AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).max((a, b) -> (int) (a.hb.cX - b.hb.cX)).map(a -> a.hb.cX).orElse(-400f);
        addToBot(new VFXAction(new IronWaveEffect(p.hb.cX, p.hb.cY, cX), 0.5f));
        for (int i = 0; i < magicNumber; i++) {
            AbstractCard card = new Jiaoqiu2();
            // if (upgraded) card.upgrade();
            addToBot(new MakeTempCardInHandAction(card));
        }
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.FIRE)
                .setCallback(ci -> {
                    addToBot(new ApplyPowerAction(ci.target, p, new VulnerablePower(ci.target, 1, false), 1));
                })
        );
    }
}
