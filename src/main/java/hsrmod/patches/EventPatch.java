package hsrmod.patches;

import basemod.abstracts.events.phases.CombatPhase;
import basemod.patches.com.megacrit.cardcrawl.rooms.AbstractRoom.EventCombatSave;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

public class EventPatch {
    @SpirePatch(clz = EventRoom.class, method = "update")
    public static class EventRoomUpdatePatch {
        public static SpireReturn<Void> Prefix(EventRoom __instance) {
            if (__instance.event != null) {
                return SpireReturn.Continue();
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.dungeonMapScreen.open(false);
            return SpireReturn.Return(null);
        }
    }
    
    @SpirePatch(clz = CombatPhase.class, method = "transition")
    public static class CombatPhaseTransitionPatch {
        public static void Postfix(CombatPhase __instance) {
            EventCombatSave.preventSave();
        }
    }
}
