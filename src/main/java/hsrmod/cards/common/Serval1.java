package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.breaks.ShockPower;

public class Serval1 extends BaseCard {
    public static final String ID = Serval1.class.getSimpleName();

    public Serval1() {
        super(ID);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new AOEAction((q) -> new ElementalDamageAction(q, new DamageInfo(p, damage),
                        ElementType.Lightning, 2,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                        this::onElementalDamageDealt))
        );
    }
    
    void onElementalDamageDealt(AbstractCreature m) {
        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new ShockPower(m, AbstractDungeon.player, 1), 1));
    }
}
