package hsrmod.cards.uncommon;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.only.WisemansFollyPower;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class DrRatio3 extends BaseCard {
    public static final String ID = DrRatio3.class.getSimpleName();
    
    public int debuffNum = 0;
    
    public DrRatio3() {
        super(ID);
        tags.add(FOLLOW_UP);
        exhaust = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(
                new ElementalDamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage + debuffNum * magicNumber,
                                damageTypeForTurn
                        ),
                        ElementType.Imaginary,
                        1,
                        // 伤害类型
                        AbstractGameAction.AttackEffect.SLASH_HEAVY
                )
        );
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p,m) && followedUp;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(m -> m.hasPower(WisemansFollyPower.POWER_ID))) {
            addToBot(new MoveCardsAction(AbstractDungeon.player.exhaustPile, AbstractDungeon.player.hand, c -> c.uuid.equals(uuid), 1));
        }
    }
}
