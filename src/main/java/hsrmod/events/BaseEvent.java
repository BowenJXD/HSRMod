package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import hsrmod.modcore.HSRMod;
import hsrmod.utils.PathDefine;

@Deprecated
public abstract class BaseEvent extends PhasedEvent {
    
    public BaseEvent(String ID) {
        super(ID, CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID)).NAME, PathDefine.EVENT_PATH + ID + ".png");
    }
}
