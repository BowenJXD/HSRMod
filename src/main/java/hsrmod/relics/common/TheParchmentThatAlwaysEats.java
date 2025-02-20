package hsrmod.relics.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import hsrmod.relics.BaseRelic;

import java.util.Iterator;

public class TheParchmentThatAlwaysEats extends BaseRelic {
    public static final String ID = TheParchmentThatAlwaysEats.class.getSimpleName();
    
    public TheParchmentThatAlwaysEats() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.getCurrRoom().monsters != null
                && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            this.flash();
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster)var1.next();
                if (m.currentHealth > (int)((float)m.maxHealth * (1.0F - magicNumber / 100.0F))) {
                    addToTop(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY, Color.PURPLE)));
                    m.currentHealth = (int)((float)m.maxHealth * (1.0F - magicNumber / 100.0F));
                    m.healthBarUpdatedEvent();
                }
            }

            this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }

    }
}
