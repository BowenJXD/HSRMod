package hsrmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.actions.TriggerDoTAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.breaks.ShockPower;
import hsrmod.powers.misc.DoTPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class Kafka1 extends BaseCard {
    public static final String ID = Kafka1.class.getSimpleName();
    
    public Kafka1() {
        super(ID);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (inHand && !followedUp) {
            if (ModHelper.check(m)
                    && m.powers.stream().anyMatch(power -> power instanceof DoTPower)) {
                followedUp = true;
                addToBot(new FollowUpAction(this, m));
            } else if ((c.target == CardTarget.ALL || c.target == CardTarget.ALL_ENEMY)) {
                Iterator<AbstractMonster> mons = AbstractDungeon.getMonsters().monsters.iterator();
                
                while (mons.hasNext()) {
                    AbstractMonster mon = mons.next();
                    if (ModHelper.check(m)
                            && mon.powers.stream().anyMatch(power -> power instanceof DoTPower)) {
                        followedUp = true;
                        addToBot(new FollowUpAction(this, mon));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            addToBot(new VFXAction(new EntangleEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY)));
        addToBot(new ElementalDamageAction(
                m,
                new ElementalDamageInfo(this), 
                AbstractGameAction.AttackEffect.LIGHTNING
        ));
        addToBot(new TriggerDoTAction(m, p, magicNumber));
        addToBot(new ApplyPowerAction(m, p, new ShockPower(m, p, magicNumber), magicNumber));
        if (!SignatureHelper.isUnlocked(cardID) && m != null)
            ModHelper.addToBotAbstract(() -> {
                if (ModHelper.getPowerCount(m, ShockPower.POWER_ID) >= 7) {
                    SignatureHelper.unlock(cardID, true);
                }
            });
    }
}
