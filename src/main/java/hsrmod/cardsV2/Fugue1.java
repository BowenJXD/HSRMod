package hsrmod.cardsV2;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.SearingBlowEffect;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.powers.uniqueDebuffs.CloudflameLusterPower;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.stream.Collectors;

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
            AbstractDungeon.getMonsters().monsters.stream().filter(mo -> !mo.isDeadOrEscaped()).forEach(this::trigger);
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
            ModHelper.addToBotAbstract(() -> ((ToughnessPower) power).alterPower(finalAmt));
        }
        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new CloudflameLusterPower(m, magicNumber), magicNumber));
    }
}
