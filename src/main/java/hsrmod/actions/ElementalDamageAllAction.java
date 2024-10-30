package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class ElementalDamageAllAction extends AbstractGameAction {
    public int[] damage;
    private BaseCard card; 
    private ElementType elementType;
    private int tr;
    private Consumer<AbstractCreature> callback;
    private Function<AbstractCreature, Integer> modifier;
    private int baseDamage;
    private boolean firstFrame;
    private boolean utilizeBaseDamage;
    
    public ElementalDamageAllAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, ElementType elementType, int tr, 
                                    AbstractGameAction.AttackEffect effect, boolean isFast) {
        this.firstFrame = true;
        this.utilizeBaseDamage = false;
        this.source = source;
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.elementType = elementType;
        this.tr = tr;
        this.attackEffect = effect;
        if (isFast) {
            this.duration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = Settings.ACTION_DUR_FAST;
        }
    }

    public ElementalDamageAllAction(AbstractCreature source, int[] amount, DamageInfo.DamageType type, ElementType elementType, int tr, 
                                    AbstractGameAction.AttackEffect effect) {
        this(source, amount, type, elementType, tr, effect, false);
    }

    public ElementalDamageAllAction(BaseCard card, int baseDamage, AttackEffect effect) {
        this(AbstractDungeon.player, (int[])null, card.damageTypeForTurn, card.elementType, card.tr, effect);
        this.card = card;
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
    }
    
    public ElementalDamageAllAction(BaseCard card, AttackEffect effect) {
        this(AbstractDungeon.player, card.multiDamage, card.damageTypeForTurn, card.elementType, card.tr, effect);
        this.card = card;
    }
    
    public ElementalDamageAllAction setCallback(Consumer<AbstractCreature> callback) {
        this.callback = callback;
        return this;
    }
    
    public ElementalDamageAllAction setModifier(Function<AbstractCreature, Integer> modifier) {
        this.modifier = modifier;
        return this;
    }

    public void update() {
        int temp;
        if (this.firstFrame) {
            if (this.utilizeBaseDamage) {
                this.damage = DamageInfo.createDamageMatrix(this.baseDamage);
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
            Iterator var4 = AbstractDungeon.player.powers.iterator();

            while(var4.hasNext()) {
                AbstractPower p = (AbstractPower)var4.next();
                p.onDamageAllEnemies(this.damage);
            }

            temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for(int i = 0; i < temp; ++i) {
                if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDeadOrEscaped()) {
                    ElementalDamageAction action = new ElementalDamageAction((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i), 
                            new ElementalDamageInfo(this.source, this.damage[i], this.damageType, this.elementType, this.tr, this.card), 
                            this.attackEffect, this.callback, this.modifier).setIsFast(true);
                    
                    action.update();
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
