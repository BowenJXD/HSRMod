package hsrmod.powers.uniqueBuffs;

import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.BreakDamageAction;
import hsrmod.cards.uncommon.Firefly1;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.PowerPower;
import hsrmod.powers.misc.BreakEfficiencyPower;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;
import hsrmod.subscribers.SubscribeManager;

import java.util.List;

public class FireflyPower extends PowerPower implements PostPowerApplySubscriber {
    public static final String POWER_ID = HSRMod.makePath(FireflyPower.class.getSimpleName());

    boolean canTrigger = true;
    
    public FireflyPower() {
        super(POWER_ID);
    }

    @Override
    public void onInitialApplication() {
        BaseMod.subscribe(this);
    }

    @Override
    public void onRemove() {
        BaseMod.unsubscribe(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        canTrigger = true;
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        if (SubscribeManager.checkSubscriber(this) 
                && abstractPower.ID.equals(BrokenPower.POWER_ID)
                && canTrigger) {
            this.flash();
            canTrigger = false;
            addToBot(new BreakDamageAction(target, new DamageInfo(this.owner, ToughnessPower.getStackLimit(target))));
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new BreakEfficiencyPower(this.owner, 1), 1));

            List<ModHelper.FindResult> fireFiles = ModHelper.findCards(c -> c instanceof Firefly1);
            for (ModHelper.FindResult result : fireFiles) {
                if (result.group == AbstractDungeon.player.hand) continue;
                addToBot(new MoveCardsAction(result.group, AbstractDungeon.player.hand, card -> card == result.card));
            }
            
            if (fireFiles.isEmpty()) {
                addToBot(new MakeTempCardInHandAction(new Firefly1()));
            }
        }
    }
}
