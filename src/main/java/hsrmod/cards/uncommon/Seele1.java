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
    
    public static int increaseAmount = 1;
    
    public Seele1() {
        super(ID);
        this.misc = damage;
        increaseAmount = magicNumber;
        energyCost = 120;
        DamageModifierManager.addModifier(this, new SeeleDamage(this));
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractGameAction action  = new ElementalDamageAction(m, new DamageInfo(p, damage), ElementType.Quantum, magicNumber);
        BindingHelper.bindAction(this, action);
        addToBot(action);
    }
    
    public static class SeeleDamage extends AbstractDamageModifier {
        AbstractCard card;
        
        public SeeleDamage(AbstractCard card){
            this.card = card;
        }
        
        @Override
        public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
            ModHelper.addToBotAbstract(() -> {
                if ((target.isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower("Minion")) {
                    Iterator var1 = AbstractDungeon.player.masterDeck.group.iterator();

                    AbstractCard c;
                    while (var1.hasNext()) {
                        c = (AbstractCard) var1.next();
                        if (c.uuid == card.uuid) {
                            c.misc += Seele1.increaseAmount;
                            c.applyPowers();
                            c.baseDamage = c.misc;
                            c.isDamageModified = false;
                        }
                    }

                    for (var1 = GetAllInBattleInstances.get(card.uuid).iterator(); var1.hasNext(); c.baseDamage = c.misc) {
                        c = (AbstractCard) var1.next();
                        c.misc += Seele1.increaseAmount;
                        c.applyPowers();
                    }
                    
                    addToBot(new GainEnergyAction(1));
                    addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.limbo, (q) -> q.uuid == card.uuid));
                }
            });
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new SeeleDamage(card);
        }

        @Override
        public boolean isInherent() {
            return true;
        }
    }
}
