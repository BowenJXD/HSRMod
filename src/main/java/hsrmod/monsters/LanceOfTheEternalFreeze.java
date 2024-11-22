package hsrmod.monsters;

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

public class LanceOfTheEternalFreeze extends AbstractMonster {
    public static final String ID = LanceOfTheEternalFreeze.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    public LanceOfTheEternalFreeze(float x, float y, int picIndex){
        super(NAME, HSRMod.makePath(ID), 26, 0.0F, -15.0F, 188F, 243.0F, PathDefine.MONSTER_PATH + ID + "_" + picIndex + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        this.damage.add(new DamageInfo(this, 6));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new MakeTempCardInDrawPileAction(new Frozen(), 1, true, true));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK, this.damage.get(0).base);
    }
}
