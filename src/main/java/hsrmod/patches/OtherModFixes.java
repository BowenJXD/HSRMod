/*
package hsrmod.patches;

import RingOfDestiny.helpers.SoulStoneCustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;

public class OtherModFixes {
    
    @SpirePatch(clz = SoulStoneCustomSavable.class, method = "load", requiredModId = "RingOfDestiny")
    public static class RingOfDestinyFix {
        @SpirePrefixPatch 
        public static void prefix(@ByRef Integer[] integer) {
            if (integer[0] == null) integer[0] = 0;
        }
    }
}
*/
