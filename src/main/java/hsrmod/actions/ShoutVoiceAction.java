package hsrmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ShoutVoiceAction extends AbstractGameAction {
    private final String soundKey;
    private final float volume;

    public ShoutVoiceAction(String soundKey, float volume) {
        this.soundKey = soundKey;
        this.volume = volume;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playV(soundKey, volume);
        isDone = true;
    }
}
