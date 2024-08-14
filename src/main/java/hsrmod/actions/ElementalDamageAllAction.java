package hsrmod.actions;

import com.badlogic.gdx.graphics.Color;
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
    private int baseDamage;
    private boolean firstFrame;
    private boolean utilizeBaseDamage;
    
    private ElementType elementType;
    private int toughnessReduction;
    private Consumer<AbstractCreature> afterEffect;
    
    public ElementalDamageAllAction(int amount, ElementType elementType, int toughnessReduction, AbstractGameAction.AttackEffect effect, Consumer<AbstractCreature> afterEffect) {
        this.firstFrame = true;
        this.source = AbstractDungeon.player;
        this.baseDamage = amount;
        this.utilizeBaseDamage = true;
        this.elementType = elementType;
        this.toughnessReduction = toughnessReduction;
        this.actionType = ActionType.DAMAGE;
        this.damageType = DamageInfo.DamageType.NORMAL;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.afterEffect = afterEffect;
    }

    public void update() {
        int j;
        if (this.firstFrame) {
            boolean playedMusic = false;
            j = AbstractDungeon.getCurrRoom().monsters.monsters.size();
            if (this.utilizeBaseDamage) {
                this.damage = DamageInfo.createDamageMatrix(this.baseDamage);
            }

            for(int i = 0; i < j; ++i) {
                if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDying && ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).currentHealth > 0 && !((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX, ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cX, ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).hb.cY, this.attackEffect));
                    }
                }
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

            int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();

            for(int i = 0; i < temp; ++i) {
                if (!((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.color.set(Color.CHARTREUSE);
                        ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AttackEffect.FIRE) {
                        ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.color.set(Color.RED);
                        ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i)).tint.changeColor(Color.WHITE.cpy());
                    }
                    
                    //
                    AbstractCreature m = ((AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i));
                    
                    m.damage(new DamageInfo(this.source, this.damage[i], this.damageType));
                    
                    this.addToBot(new ReduceToughnessAction(m, toughnessReduction, elementType, new DamageInfo(this.source, this.damage[i])));
                    if (this.afterEffect != null) {
                        this.afterEffect.accept(m);
                    }
                    //
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
