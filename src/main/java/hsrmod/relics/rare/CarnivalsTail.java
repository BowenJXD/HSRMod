package hsrmod.relics.rare;

import basemod.BaseMod;
import basemod.interfaces.RelicGetSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.relics.BaseRelic;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;
import hsrmod.utils.RelicEventHelper;

import java.util.ArrayList;
import java.util.List;

public class CarnivalsTail extends BaseRelic implements RelicGetSubscriber {
    public static final String ID = CarnivalsTail.class.getSimpleName();
    
    List<AbstractRelic> relicsCache;
    
    public CarnivalsTail() {
        super(ID);
        BaseMod.subscribe(this);
        relicsCache = new ArrayList<>();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        relicsCache.clear();
    }

    @Override
    public void reorganizeObtain(AbstractPlayer p, int slot, boolean callOnEquip, int relicAmount) {
        super.reorganizeObtain(p, slot, callOnEquip, relicAmount);
        BaseMod.unsubscribe(this);
        ModHelper.addEffectAbstract(() -> BaseMod.subscribe(this));
    }

    @Override
    public void receiveRelicGet(AbstractRelic abstractRelic) {
        if (abstractRelic != this && SubscriptionManager.checkSubscriber(this) && !relicsCache.contains(abstractRelic)) {
            relicsCache.add(abstractRelic);
            AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    if (abstractRelic.currentY == abstractRelic.targetY && abstractRelic.currentX == abstractRelic.targetX) {
                        flash();
                        ModHelper.addEffectAbstract(() -> RelicEventHelper.loseRelics(abstractRelic));
                        ModHelper.addEffectAbstract(() -> RelicEventHelper.gainRelics(abstractRelic));
                        isDone = true;
                    }
                    if (!abstractRelic.isObtained) {
                        isDone = true;
                    }
                }

                @Override
                public void render(SpriteBatch spriteBatch) {}

                @Override
                public void dispose() {}
            });
        }
    }
}
