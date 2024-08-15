package hsrmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlackSwan2 extends BaseCard{
    public static final String ID = BlackSwan2.class.getSimpleName();
    
    private int bleedStackNum = 1;
    private int burnStackNum = 1;
    private int shockStackNum = 1;
    private int windShearStackNum = 2;
    
    public BlackSwan2() {
        super(ID);
        energyCost = 120;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new hsrmod.powers.BleedPower(m, p, bleedStackNum), bleedStackNum));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new hsrmod.powers.BurnPower(m, p, burnStackNum), burnStackNum));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new hsrmod.powers.ShockPower(m, p, shockStackNum), shockStackNum));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new hsrmod.powers.WindShearPower(m, p, windShearStackNum), windShearStackNum));
    }
}
