package hsrmod.relics.common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RubertEmpireDifferenceMachine extends BaseRelic {
    public static final String ID = RubertEmpireDifferenceMachine.class.getSimpleName();

    public RubertEmpireDifferenceMachine() {
        super(ID);
    }

    @Override
    public void onEquip() {
        super.onEquip();

        AbstractDungeon.topLevelEffects.add(new ShineSparkleEffect(0, 0));
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
