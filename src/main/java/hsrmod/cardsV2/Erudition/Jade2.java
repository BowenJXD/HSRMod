package hsrmod.cardsV2.Erudition;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.utils.ModHelper;

public class Jade2 extends BaseCard {
    public static final String ID = Jade2.class.getSimpleName();

    public Jade2() {
        super(ID);
        setBaseEnergyCost(140);
        tags.add(CustomEnums.ENERGY_COSTING);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new IntimidateEffect(p.hb.cX, p.hb.cY)));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                final int[] hpLoss = {0};
                for (AbstractCard c : p.hand.group) {
                    if ((c.target == CardTarget.ALL_ENEMY || c.target == CardTarget.ALL) && c.costForTurn > 0) {
                        hpLoss[0] += 2;
                        Jade2.this.addToTop(new ReduceCostForTurnAction(c, 1));
                    }
                }
                Jade2.this.addToTop(new LoseHPAction(p, p, hpLoss[0]));
            }
        });
    }
}
