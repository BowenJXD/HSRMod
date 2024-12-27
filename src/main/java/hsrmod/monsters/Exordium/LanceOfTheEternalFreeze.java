package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.cardsV2.Curse.Frozen;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;

public class LanceOfTheEternalFreeze extends BaseMonster {
    public static final String ID = LanceOfTheEternalFreeze.class.getSimpleName();
    
    public LanceOfTheEternalFreeze(float x, float y, int picIndex){
        super(ID, PathDefine.MONSTER_PATH + ID + "_" + picIndex + ".png", 0.0F, -15.0F, 188F, 243.0F, x, y);
        
        setDamagesWithAscension(6);
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new MakeTempCardInDrawPileAction(new Frozen(), 1, true, true));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK, this.damage.get(0).base);
    }
}
