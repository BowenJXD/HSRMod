package hsrmod.cardsV2.Remembrance;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.TriggerPassiveAction;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import hsrmod.actions.AddToDarkAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;

public class Castorice2 extends BaseCard{
    public static final String ID = Castorice2.class.getSimpleName();

    public Castorice2() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ChannelAction(new Dark()));
        int blockToLose = p.currentBlock * magicNumber / 100;
        if (blockToLose > 0)
            addToBot(new LoseBlockAction(p, p, blockToLose));
        int hpToLose = TempHPField.tempHp.get(p) * magicNumber / 100;
        if (hpToLose > 0)
            addToBot(new LoseHPAction(p, p, hpToLose));
        if (blockToLose + hpToLose > 0) {
            addToBot(new AddToDarkAction(blockToLose + hpToLose));
        }
        if (upgraded) {
            p.orbs.stream().filter(orb -> orb.ID.equals(Dark.ORB_ID)).forEach(orb -> addToBot(new TriggerPassiveAction(orb)));
        }
    }
}
