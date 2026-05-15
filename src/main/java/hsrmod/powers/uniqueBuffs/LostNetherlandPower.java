package hsrmod.powers.uniqueBuffs;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.actions.FollowUpAction;
import hsrmod.cardsV2.Remembrance.Pollux3;
import hsrmod.effects.PersistentImageEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.modcore.Path;
import hsrmod.powers.TerritoryPower;
import hsrmod.utils.PathDefine;

import java.util.List;

public class LostNetherlandPower extends TerritoryPower {
    public static final String POWER_ID = HSRMod.makePath(LostNetherlandPower.class.getSimpleName());

    boolean triggered = false;
    AbstractGameEffect imgEffect;
    
    public LostNetherlandPower(AbstractCreature owner) {
        super(POWER_ID, owner);
        updateDescription();
        backgroundPath = "HSRModResources/img/scene/LostNetherland1.png";
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        imgEffect = new PersistentImageEffect(PathDefine.EFFECT_PATH + "Memosprite/Pollux.png", 0.1f * Settings.WIDTH, 0.61f * Settings.HEIGHT);
        AbstractDungeon.effectList.add(imgEffect);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        imgEffect.dispose();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount >= owner.currentHealth && !triggered) {
            triggered = true;
            AbstractCard card = new Pollux3();
            addToBot(new MakeTempCardInHandAction(card, false, true));
            addToBot(new FollowUpAction(card));
            return 0;
        }
        return damageAmount;
    }
}
