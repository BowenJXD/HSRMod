package hsrmod.cardsV2.Preservation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.QuakePower;
import hsrmod.utils.ModHelper;

public class TheArchitects extends BaseCard {
    public static final String ID = TheArchitects.class.getSimpleName();

    public TheArchitects() {
        super(ID);
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
        int blockLose = p.currentBlock;
        addToBot(new RemoveAllBlockAction(p, p));
        if (p.hasPower(QuakePower.POWER_ID)) {
            AbstractPower quakePower = p.getPower(QuakePower.POWER_ID);
            ((QuakePower) quakePower).attack(m, p.currentBlock);
        }
        int count = AbstractDungeon.getMonsters().monsters.stream()
                .mapToInt(monster -> ModHelper.check(monster) ? 1 : 0)
                .sum();
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SHIELD));
        
        int blockGain = blockLose / (count + 1);
        if (blockGain > 0) {
            addToBot(new GainBlockAction(p, p, blockGain));
            AbstractDungeon.getMonsters().monsters.stream()
                    .filter(ModHelper::check)
                    .forEach(monster -> {
                        if (monster.currentBlock > 0 && upgraded)
                            addToBot(new DamageAction(monster, new DamageInfo(p, blockGain, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SHIELD));
                        else
                            addToBot(new GainBlockAction(monster, monster, blockGain));
                    });
        }
    }
}
