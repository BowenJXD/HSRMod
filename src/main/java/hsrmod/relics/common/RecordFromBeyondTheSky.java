package hsrmod.relics.common;

import hsrmod.relics.BaseRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.BufferPower;

public class RecordFromBeyondTheSky extends BaseRelic {
    public static final String ID = RecordFromBeyondTheSky.class.getSimpleName();
    
    public RecordFromBeyondTheSky() {
        super(ID);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, magicNumber), magicNumber));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BufferPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
