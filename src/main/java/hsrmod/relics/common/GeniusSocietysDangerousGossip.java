package hsrmod.relics.common;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

public class GeniusSocietysDangerousGossip extends BaseRelic implements CustomSavable<Integer> {
    public static final String ID = GeniusSocietysDangerousGossip.class.getSimpleName();

    static int goldRequired = 80;
    int cachedGold = 0;
    int goldGained = 0;
    
    public GeniusSocietysDangerousGossip() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        cachedGold = AbstractDungeon.player.gold;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        cachedGold = AbstractDungeon.player.gold;
    }

    @Override
    public void onGainGold() {
        super.onGainGold();
        if (usedUp) return;
        int tmp = AbstractDungeon.player.gold - cachedGold;
        goldGained += tmp;
        while (goldGained > goldRequired && counter > 0) {
            goldGained -= goldRequired;
            flash();
            RelicEventHelper.gainRelicsAfterwards(1);
            if (reduceCounterAndCheckDestroy()) {
                tmp -= goldGained;
                break;
            }
        }
        int finalTmp = tmp;
        ModHelper.addEffectAbstract(() -> AbstractDungeon.player.loseGold(finalTmp));
    }

    @Override
    public void onLoseGold() {
        super.onLoseGold();
        cachedGold = AbstractDungeon.player.gold;
    }

    @Override
    public Integer onSave() {
        cachedGold = AbstractDungeon.player.gold;
        return goldGained;
    }

    @Override
    public void onLoad(Integer integer) {
        cachedGold = AbstractDungeon.player.gold;
        goldGained = integer;
    }
}
