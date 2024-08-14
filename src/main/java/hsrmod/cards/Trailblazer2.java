package hsrmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.modcore.ElementType;
import hsrmod.powers.EnergyCounter;

public class Trailblazer2 extends BaseCard {
    public static final String ID = Trailblazer2.class.getSimpleName();

    public Trailblazer2() {
        super(ID);
        this.isMultiDamage = true;
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
        energyCost = 120;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!checkEnergy()) return;
        AbstractDungeon.actionManager.addToBottom(
                new ElementalDamageAllAction(
                        damage,
                        ElementType.Physical,
                        2,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                        null
                )
        );
    }

}