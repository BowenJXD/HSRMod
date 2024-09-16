package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class Seele1 extends BaseCard {
    public static final String ID = Seele1.class.getSimpleName();
    
    public Seele1() {
        super(ID);
        this.misc = damage;
        energyCost = 120;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction action  = new ElementalDamageAction(m, new DamageInfo(p, damage), ElementType.Quantum, magicNumber, AbstractGameAction.AttackEffect.SLASH_HEAVY, (q) -> {
            if (q.isDying || q.currentHealth <= 0) {
                Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

                AbstractCard c;
                while (var1.hasNext()) {
                    c = (AbstractCard) var1.next();
                    if (c.uuid == this.uuid) {
                        c.misc += magicNumber;
                        c.applyPowers();
                        c.baseDamage = c.misc;
                        c.isDamageModified = false;
                    }
                }

                for (var1 = GetAllInBattleInstances.get(this.uuid).iterator(); var1.hasNext(); c.baseDamage = c.misc) {
                    c = (AbstractCard) var1.next();
                    c.misc += magicNumber;
                    c.applyPowers();
                }
                
                addToBot(new GainEnergyAction(1));
                addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.discardPile, card -> card.uuid == this.uuid));
            }
        });
        addToBot(action);
    }
}
