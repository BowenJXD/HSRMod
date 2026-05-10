package hsrmod.cardsV2.Preservation;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.HSRMod;
import hsrmod.signature.utils.SignatureHelper;

public class PermansorTerrae1 extends BaseCard implements PostBattleSubscriber, OnPlayerDamagedSubscriber, OnObtainCard {
    public static final String ID = PermansorTerrae1.class.getSimpleName();

    int floorToPersist = 3;
    int count;
    boolean failed = false;
    
    public PermansorTerrae1() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        for (int i = 0; i < magicNumber; i++)
            addToBot(new ChannelAction(new Frost()));
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new GainBlockAction(m, p, magicNumber));
    }

    @Override
    public void onObtainCard() {
        BaseMod.subscribe(this);
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (AbstractDungeon.player.masterDeck.contains(this) && i > 0) {
            failed = true;
        }
        return i;
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        if (AbstractDungeon.player.masterDeck.contains(this)) {
            if (failed) count = 0;
            else count++;
            if (count >= floorToPersist) {
                SignatureHelper.unlock(HSRMod.makePath(ID), true);
            }
        }
    }
}
