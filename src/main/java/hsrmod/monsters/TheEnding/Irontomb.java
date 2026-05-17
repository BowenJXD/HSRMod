package hsrmod.monsters.TheEnding;

import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.cardsV2.Trailblaze.Phainon1;
import hsrmod.cardsV2.Trailblaze.Phainon2;
import hsrmod.misc.VideoManager;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.uniqueBuffs.RuinousIrontombPower;
import hsrmod.relics.special.CoreflameOfWorldbearing;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;
import hsrmod.utils.ModPrefs;

public class Irontomb extends BaseMonster {
    public static final String ID = Irontomb.class.getSimpleName();
    
    public int reincarnationCount = 0;
    
    public Irontomb() {
        super(ID, 444, 50, 200, -160);
        isEscaping = true;
        addMove(Intent.NONE, mi -> {});
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new CannotLoseAction());
        try {
            ModPrefs.load();
            reincarnationCount = ModPrefs.getInteger("IrontombStrength", 0);
            if (reincarnationCount > 0) {
                addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, reincarnationCount), 0));
            }
            ModPrefs.putInteger("IrontombStrength", reincarnationCount + 1);
            ModPrefs.save();
        } catch (Exception e) {
            HSRMod.logger.error("Error while loading IrontombStrength", e);
        }
        ModHelper.addToBotAbstract(() -> ModHelper.addToBotAbstract(() -> {
            if (maxHealth > 33550336) {
                decreaseMaxHealth(maxHealth -  33550336);
            }
        }));
    }

    @Override
    protected void updateHealthBar() {
        isEscaping = false;
        super.updateHealthBar();
        isEscaping = true;
    }

    @Override
    public void update() {
        super.update();
        if (maxHealth > 33550336) {
            decreaseMaxHealth(maxHealth -  33550336);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        isEscaping = false;
        super.damage(info);
        isEscaping = true;
    }

    @Override
    public boolean isDeadOrEscaped() {
        isEscaping = false;
        boolean result = super.isDeadOrEscaped();
        isEscaping = true;
        return result;
    }

    @Override
    protected void getMove(int i) {
        setMove(0);
    }

    @Override
    public void die() {
        super.die();
        currentBlock = 0;
        addToBot(new CanLoseAction());
        ModHelper.addToBotAbstract(() -> {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                addToTop(new SuicideAction(monster));
            }
        });
        if (AbstractDungeon.player.hasPower(RuinousIrontombPower.POWER_ID)) {
            SignatureHelper.unlock(HSRMod.makePath(Phainon1.ID), true);
            SignatureHelper.unlock(HSRMod.makePath(Phainon2.ID), true);
        }
        try {
            ModPrefs.putInteger("IrontombStrength", 0);
            ModPrefs.save();
        } catch (Exception e) {
            HSRMod.logger.error("Error while saving IrontombStrength", e);
        }
        CoreflameOfWorldbearing.addFlame(1);
        VideoManager.play("Ending", 33, true);
    }
}
