package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.powers.uniqueDebuffs.CloudflameLusterPower;
import hsrmod.utils.ModHelper;

public class Fugue1 extends BaseCard {
    public static final String ID = Fugue1.class.getSimpleName();

    public Fugue1() {
        super(ID);
        this.isEthereal = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        this.isEthereal = false;
        this.target = CardTarget.ALL_ENEMY;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            trigger(m);
        } else {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                if (ModHelper.check(monster)) {
                    trigger(monster);
                }
            }
        }
    }

    public void trigger(AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new SearingBlowEffect(m.hb.cX, m.hb.cY, upgraded ? 2 : 1)));
        int amt = magicNumber - ModHelper.getPowerCount(m, ToughnessPower.POWER_ID);
        amt = Math.min(magicNumber, Math.max(amt, -magicNumber));
        AbstractPower power = m.getPower(ToughnessPower.POWER_ID);
        if (power != null) {
            int finalAmt = amt;
            ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    ((ToughnessPower) power).alterPower(finalAmt);
                }
            });
        }
        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new CloudflameLusterPower(m, magicNumber), magicNumber));
    }
}
