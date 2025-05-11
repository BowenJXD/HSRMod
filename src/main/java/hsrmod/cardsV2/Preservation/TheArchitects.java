package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.AOEAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.QuakePower;
import hsrmod.utils.ModHelper;

public class TheArchitects extends BaseCard {
    public static final String ID = TheArchitects.class.getSimpleName();

    public TheArchitects() {
        super(ID);
        isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        super.upgrade();
        isInnate = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = AbstractDungeon.player.currentBlock;
        int count = AbstractDungeon.getMonsters().monsters.stream()
                .mapToInt(monster -> ModHelper.check(monster) ? 1 : 0)
                .sum();
        baseDamage /= count;
        super.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        QuakePower quakePower = (QuakePower) p.getPower(QuakePower.POWER_ID);
        if (quakePower.amount >= magicNumber) {
            for (int i = 0; i < magicNumber; i++) {
                quakePower.attack(m, p.currentBlock);
            }
        } else {
            int amt = (p.currentBlock > 0 ? 1 : 0) + AbstractDungeon.getMonsters().monsters.stream().mapToInt(mo -> mo.currentBlock > 0 ? 1 : 0).sum();
            addToBot(new ApplyPowerAction(p, p, new QuakePower(p, amt)));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        glowColor = ModHelper.getPowerCount(AbstractDungeon.player, QuakePower.POWER_ID) >= magicNumber ? GOLD_BORDER_GLOW_COLOR : BLUE_BORDER_GLOW_COLOR;
    }
}
