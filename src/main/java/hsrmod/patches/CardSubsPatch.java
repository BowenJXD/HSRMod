package hsrmod.patches;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.cards.uncommon.Acheron1;
import hsrmod.modcore.HSRMod;
import me.antileaf.signature.utils.SignatureHelper;

import java.util.Objects;

public class CardSubsPatch {

    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update", paramtypez = {})
    public static class ShowCardAndObtainEffectPatch2 {
        @SpirePrefixPatch
        public static void Prefix(ShowCardAndObtainEffect _inst, AbstractCard ___card) {
            if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F) {
                String id = ___card.cardID;
                if (Objects.equals(id, HSRMod.makePath(Acheron1.ID))) {
                    SignatureHelper.unlock(id, true);
                }
            }
        }
    }
}
