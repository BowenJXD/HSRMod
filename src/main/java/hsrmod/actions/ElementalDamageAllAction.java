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
import hsrmod.utils.ModHelper;

import java.util.Iterator;
import java.util.function.Consumer;

public class ElementalDamageAllAction extends AbstractGameAction {
    public int[] damage;
    private BaseCard card;
    private ElementType elementType;
    private int tr;
    private Consumer<ElementalDamageAction.CallbackInfo> callback;
    private Consumer<ElementalDamageAction.CallbackInfo> modifier;
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
        this(AbstractDungeon.player, (int[]) null, card.damageTypeForTurn, card.elementType, card.tr, effect);
        this.card = card;
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
    }

    public ElementalDamageAllAction(BaseCard card, AttackEffect effect) {
        this(AbstractDungeon.player, card.multiDamage, card.damageTypeForTurn, card.elementType, card.tr, effect);
        this.card = card;
    }

    public ElementalDamageAllAction setCallback(Consumer<ElementalDamageAction.CallbackInfo> callback) {
        this.callback = callback;
        return this;
    }

    public ElementalDamageAllAction setModifier(Consumer<ElementalDamageAction.CallbackInfo> modifier) {
        this.modifier = modifier;
        return this;
    }

    public void update() {
        if (this.firstFrame) {
            if (this.utilizeBaseDamage) {
                this.damage = DamageInfo.createDamageMatrix(this.baseDamage);
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damage);
            }

            for (int i = 0; i < AbstractDungeon.getCurrRoom().monsters.monsters.size(); i++) {
                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (ModHelper.check(m)) {
                    ElementalDamageAction action = new ElementalDamageAction(
                            m,
                            new ElementalDamageInfo(
                                    this.source,
                                    this.damage[i],
                                    this.damageType,
                                    this.elementType,
                                    this.tr,
                                    this.card
                            ),
                            this.attackEffect,
                            this.callback,
                            this.modifier
                    ).setIsFast(true);

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
