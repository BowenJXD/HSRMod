package hsrmod.events;

import basemod.abstracts.events.PhasedEvent;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import hsrmod.modcore.HSRMod;

public abstract class BaseEvent extends PhasedEvent {
    
    public BaseEvent(String ID) {
        super(ID, CardCrawlGame.languagePack.getEventString(HSRMod.makePath(ID)).NAME, "HSRModResources/img/events/" + ID + ".png");
    }
}
