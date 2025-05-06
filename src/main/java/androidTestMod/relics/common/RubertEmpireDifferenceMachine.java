package androidTestMod.relics.common;

import androidTestMod.relics.BaseRelic;
import androidTestMod.utils.RelicEventHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;

public class RubertEmpireDifferenceMachine extends BaseRelic {
    public static final String ID = RubertEmpireDifferenceMachine.class.getSimpleName();

    public RubertEmpireDifferenceMachine() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();

        AbstractDungeon.topLevelEffectsQueue.add(new ShineSparkleEffect(0, 0));
        RelicEventHelper.gainGold(300);
        RelicEventHelper.gainRelicsAfterwards(3);
        RelicEventHelper.upgradeCards(3);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);

        RelicEventHelper.gainGold(100);
        RelicEventHelper.gainRelicsAfterwards(1);
        RelicEventHelper.upgradeCards(1);
    }
}
