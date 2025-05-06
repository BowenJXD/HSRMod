package androidTestMod.cards.common;

import androidTestMod.actions.BreakDamageAction;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.ElementalDamageAllAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.powers.misc.BreakEffectPower;
import androidTestMod.utils.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.Consumer;

public class Rappa1 extends BaseCard {
    public static final String ID = Rappa1.class.getSimpleName();

    int count = 0;
    boolean canRepeat = false;

    public Rappa1() {
        super(ID);
        isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0);
        count = 0;
        execute();
    }

    void execute() {
        canRepeat = true;
        
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;

        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BreakEffectPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToBot(new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL).setCallback(new Consumer<ElementalDamageAction.CallbackInfo>() {
            @Override
            public void accept(ElementalDamageAction.CallbackInfo c) {
                if (c.didBreak) {
                    Rappa1.this.addToBot(new BreakDamageAction(c.target, new DamageInfo(AbstractDungeon.player, tr)));
                    if (canRepeat) {
                        canRepeat = false;
                        ModHelper.addToTopAbstract(new ModHelper.Lambda() {
                            @Override
                            public void run() {
                                Rappa1.this.execute();
                            }
                        });
                    }
                }
            }
        }));
    }
}
