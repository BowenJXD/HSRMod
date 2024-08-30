package hsrmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import hsrmod.modcore.ElementType;

import javax.xml.transform.Source;
import java.util.Iterator;
import java.util.function.Consumer;

public class ElementalDamageAllAction extends AbstractGameAction {
    public int[] damage;
    private DamageInfo info;
    private ElementType elementType;
    private int toughnessReduction;
    private Consumer<AbstractCreature> afterEffect;

    public ElementalDamageAllAction(int baseDamage, ElementType elementType, int toughnessReduction, AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> afterEffect) {
        this.elementType = elementType;
        this.toughnessReduction = toughnessReduction;
        this.info = new DamageInfo(AbstractDungeon.player, baseDamage);
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.afterEffect = afterEffect;
    }

    public ElementalDamageAllAction(int baseDamage, ElementType elementType, int toughnessReduction, AbstractGameAction.AttackEffect effect) {
        this(baseDamage, elementType, toughnessReduction, effect, null);
    }

    public void update() {
        isDone = true;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                addToTop(new ElementalDamageAction(m, info, this.elementType, this.toughnessReduction, this.attackEffect, this.afterEffect));
            }
        }
    }
}
