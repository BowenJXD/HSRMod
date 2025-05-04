package hsrmod.relics.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import hsrmod.relics.BaseRelic;
import hsrmod.utils.ModHelper;

import java.util.function.Consumer;

public class Revitalization310 extends BaseRelic {
    public static final String ID = Revitalization310.class.getSimpleName();

    public boolean canTrigger = true;
    
    public Revitalization310() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        canTrigger = true;
    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
        if (!canTrigger) 
            return;
        flash();
        ModHelper.addToBotAbstract(new ModHelper.Lambda() {
            @Override
            public void run() {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    c.isEthereal = true;
                }
                canTrigger = false;
            }
        });
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        super.onPlayCard(c, m);
        if (isObtained && !c.isEthereal)
            return;
        flash();
        if (m == null) m = ModHelper.betterGetRandomMonster();
        if (m == null) return;
        addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new StrengthPower(m, -1), -1));
    }
}
