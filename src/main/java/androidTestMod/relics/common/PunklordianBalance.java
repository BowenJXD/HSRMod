package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public class PunklordianBalance extends BaseRelic {
    public static final String ID = PunklordianBalance.class.getSimpleName();

    public PunklordianBalance() {
        super(ID);
        setCounter(magicNumber);
    }

    public void atBattleStart() {
        if (AbstractDungeon.getCurrRoom().eliteTrigger) return;
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) return;
        if (AbstractDungeon.getMonsters() == null) return;
        if (AbstractDungeon.getMonsters().monsters == null) return;
        if (usedUp) return;
        
        reduceCounterAndCheckDestroy();
        this.flash();

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.currentHealth > m.maxHealth / 2)
                m.currentHealth = m.maxHealth / 2;
            m.healthBarUpdatedEvent();
        }

        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
