package hsrmod.modcore;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.breaks.*;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.powers.uniqueBuffs.Trailblazer5Power;
import hsrmod.subscribers.SubscriptionManager;

import static hsrmod.modcore.ElementType.*;

public class ElementalDamageInfo extends DamageInfo {
    public int tr;
    public ElementType elementType;
    public AbstractCard card;
    
    public ElementalDamageInfo(AbstractCreature damageSource, int base, DamageType type, ElementType elementType, int tr, AbstractCard card) {
        super(damageSource, base, type);
        this.tr = tr;
        this.elementType = elementType;
        this.card = card;
    }
    
    public ElementalDamageInfo(AbstractCreature damageSource, int base, DamageType type, ElementType elementType, int tr) {
        this(damageSource, base, type, elementType, tr, null);
    }
    
    public ElementalDamageInfo(AbstractCreature damageSource, int base, ElementType elementType, int tr) {
        this(damageSource, base, DamageType.NORMAL, elementType, tr);
    }
    
    public ElementalDamageInfo(AbstractCreature source, BaseCard card){
        super(source, card.damage, card.damageTypeForTurn);
        this.tr = card.tr;
        this.elementType = card.elementType;
        this.card = card;
    }
    
    public ElementalDamageInfo(BaseCard card, int dmg){
        super(AbstractDungeon.player, dmg, card.damageTypeForTurn);
        this.tr = card.tr;
        this.elementType = card.elementType;
        this.card = card;
    }
    
    public ElementalDamageInfo(BaseCard card){
        super(AbstractDungeon.player, card.damage, card.damageTypeForTurn);
        this.tr = card.tr;
        this.elementType = card.elementType;
        this.card = card;
    }

    public ApplyPowerAction applyBreakingPower(AbstractCreature target){
        if (elementType == null) return null;
        AbstractPower power = null;
        int stackNum = 1;
        switch (elementType){
            case Ice:
                power = new FrozenPower(target, stackNum);
                break;
            case Physical:
                power = new BleedingPower(target, owner, stackNum);
                break;
            case Fire:
                power = new BurnPower(target, owner, stackNum);
                break;
            case Lightning:
                power = new ShockPower(target, owner, stackNum);
                break;
            case Wind:
                AbstractMonster monster = (AbstractMonster)target;
                if (monster != null) {
                    switch (monster.type) {
                        case NORMAL:
                            break;
                        case ELITE:
                            stackNum = 2;
                            break;
                        case BOSS:
                            stackNum = 3;
                            break;
                        default:
                            break;
                    }
                }
                power = new WindShearPower(target, owner, stackNum);
                break;
            case Quantum:
                power = new EntanglePower(target, owner, stackNum);
                break;
            case Imaginary:
                power = new ImprisonPower(target, stackNum);
                break;
            default:
                break;
        }
        if (power != null){
            return new ApplyPowerAction(target, AbstractDungeon.player, power, stackNum);
        }
        return null;
    }
    
    public static ElementalDamageInfo makeInfo(AbstractCreature damageSource, int base, DamageType type, ElementType elementType, int tr, AbstractCard card) {
        BindingPatches.directlyBoundInstigator = card;
        BindingPatches.directlyBoundDamageMods.addAll(DamageModifierManager.modifiers(card));
        ElementalDamageInfo info = new ElementalDamageInfo(damageSource, base, type, elementType, tr, card);
        BindingPatches.directlyBoundDamageMods.clear();
        BindingPatches.directlyBoundInstigator = null;
        return info;
    }
    
    public static ElementalDamageInfo makeInfo(BaseCard card) {
        BindingPatches.directlyBoundInstigator = card;
        BindingPatches.directlyBoundDamageMods.addAll(DamageModifierManager.modifiers(card));
        ElementalDamageInfo info = new ElementalDamageInfo(card);
        BindingPatches.directlyBoundDamageMods.clear();
        BindingPatches.directlyBoundInstigator = null;
        return info;
    }
    
    public Color getColor() {
        if (elementType == null) return Color.WHITE;
        return elementType.getColor();
    }
    
    public int getBreakDamage() {
        if (elementType == null) return 0;
        return elementType.getBreakDamage();
    }
}
