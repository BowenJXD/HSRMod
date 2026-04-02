package hsrmod.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import hsrmod.cards.BaseCard;
import hsrmod.cards.uncommon.Acheron1;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;

import java.util.Objects;

public class CardSubsPatch {

    @SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update", paramtypez = {})
    public static class ShowCardAndObtainEffectPatch {
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
    
    @SpirePatch(clz = FastCardObtainEffect.class, method = "update", paramtypez = {})
    public static class FastCardObtainEffectPatch {
        @SpirePrefixPatch
        public static void Prefix(FastCardObtainEffect _inst, AbstractCard ___card) {
            if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F) {
                String id = ___card.cardID;
                if (Objects.equals(id, HSRMod.makePath(Acheron1.ID))) {
                    SignatureHelper.unlock(id, true);
                }
            }
        }
    }
    
    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy", paramtypez = {})
    public static class CanUseCardOutsideTurn {
        static Boolean turnHasEnded = null;
        
        @SpirePrefixPatch
        public static void Prefix(AbstractCard _inst) {
            if (_inst instanceof BaseCard) {
                BaseCard card = (BaseCard) _inst;
                if (card.followedUp) {
                    turnHasEnded = AbstractDungeon.actionManager.turnHasEnded;
                    AbstractDungeon.actionManager.turnHasEnded = false;
                }
            }
        }
        
        @SpireInsertPatch(rloc = 5)
        public static void Insert(AbstractCard _inst) {
            if (turnHasEnded != null) {
                AbstractDungeon.actionManager.turnHasEnded = turnHasEnded;
                turnHasEnded = null;
            }
        }
    }
}
