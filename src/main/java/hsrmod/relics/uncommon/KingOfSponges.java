package hsrmod.relics.uncommon;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RelicAboveCreatureEffect;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;
import hsrmod.relics.BaseRelic;

public class KingOfSponges extends BaseRelic {
    public static final String ID = KingOfSponges.class.getSimpleName();

    public KingOfSponges() {
        super(ID);
        setCounter(magicNumber);
    }

    @Override
    public void onEnterRestRoom() {
        super.onEnterRestRoom();
        if (usedUp) return;
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.effectList.add(new RelicAboveCreatureEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, this));
        p.damage(new DamageInfo(p, p.currentHealth / 10, DamageInfo.DamageType.HP_LOSS));
        increaseMaxHealth(p, p.maxHealth / 10);
        reduceCounterAndCheckDestroy();
    }
    
    void increaseMaxHealth(AbstractPlayer p, int amount) {
        p.maxHealth += amount;
        AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(p.hb.cX - p.animX, p.hb.cY, AbstractCreature.TEXT[2] + Integer.toString(amount), Settings.GREEN_TEXT_COLOR));
        p.healthBarUpdatedEvent();
    }
}
