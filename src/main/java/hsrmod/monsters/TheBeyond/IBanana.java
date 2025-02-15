package hsrmod.monsters.TheBeyond;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegrowPower;
import hsrmod.monsters.BaseMonster;
import hsrmod.utils.ModHelper;

public interface IBanana {
    void processChange(boolean isInClass);

    default void processDie(AbstractMonster self) {
        if (AbstractDungeon.getMonsters().monsters.stream().anyMatch(m -> m.hasPower(RegrowPower.POWER_ID) && ModHelper.check(m))) {
            self.isDying = false;
            self.halfDead = true;
            self.rollMove();
            self.createIntent();
        } else if (AbstractDungeon.getCurrRoom().cannotLose) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> m.hasPower(RegrowPower.POWER_ID) && m != self).forEach(AbstractMonster::die);
        }
    }

    void setImg();
}
