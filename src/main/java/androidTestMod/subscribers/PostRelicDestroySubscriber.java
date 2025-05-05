package androidTestMod.subscribers;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public interface PostRelicDestroySubscriber extends IHSRSubscriber {
    void postRelicDestroy(AbstractRelic relic);
}
