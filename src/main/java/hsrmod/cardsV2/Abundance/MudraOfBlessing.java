package hsrmod.cardsV2.Abundance;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.TriggerPowerAction;
import hsrmod.cards.BaseCard;
import hsrmod.powers.misc.DewDropPower;
import hsrmod.powers.misc.NecrosisPower;
import hsrmod.utils.ModHelper;

public class MudraOfBlessing extends BaseCard {
    public static final String ID = MudraOfBlessing.class.getSimpleName();
    
    public MudraOfBlessing(){
        super(ID);
        selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        
    }

    @Override
    public void onRetained() {
        super.onRetained();
        trigger();
    }

    @Override
    public void onMoveToDiscard() {
        super.onMoveToDiscard();
        if (upgraded) trigger();
    }

    void trigger() {
        int tempHp = TempHPField.tempHp.get(AbstractDungeon.player);
        if (tempHp > 0) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DewDropPower(AbstractDungeon.player, tempHp)));
            addToBot(new TriggerPowerAction(AbstractDungeon.player.getPower(NecrosisPower.POWER_ID)));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NecrosisPower(AbstractDungeon.player, 1)));
        }
    }

    @Override
    protected boolean checkSpawnable() {
        if (AbstractDungeon.player.masterDeck.findCardById(cardID) != null) return false;
        return super.checkSpawnable();
    }
}
