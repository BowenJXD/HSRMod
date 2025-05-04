package hsrmod.relics.interfaces;

/*import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;*/
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface ClickableRelic {

    void onRightClick();

    default void clickUpdate() {
        if (!(this instanceof AbstractRelic)) {
            throw new NotImplementedException();
        } else {
            AbstractRelic relic = (AbstractRelic)this;
            if (/*(Boolean) HitboxRightClick.rightClicked.get(relic.hb)|| */ Settings.isControllerMode && relic.hb.hovered && CInputActionSet.topPanel.isJustPressed()) {
                CInputActionSet.topPanel.unpress();
                this.onRightClick();
            }

        }
    }

    default boolean hovered() {
        if (this instanceof AbstractRelic) {
            AbstractRelic relic = (AbstractRelic)this;
            return relic.hb.hovered;
        } else {
            throw new NotImplementedException();
        }
    }
}
