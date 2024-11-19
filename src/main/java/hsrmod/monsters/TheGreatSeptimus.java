package hsrmod.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import hsrmod.modcore.HSRMod;

public class TheGreatSeptimus extends CustomMonster {
    public static final String ID = TheGreatSeptimus.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    private int[] damages = {17, 27, 7};
    
    public TheGreatSeptimus() {
        super(NAME, ID, 777, 0.0F, -15.0F, 400.0F, 400.0F, "HSRModResources/img/monsters/" + ID + ".png", 0.0F, 0.0F);
        this.type = EnemyType.BOSS;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        // TODO: Add difficulty scaling
        /*if (AbstractDungeon.ascensionLevel >= 19) {}
        else if (AbstractDungeon.ascensionLevel >= 4) {}
        else {}*/

        for (int dmg : damages) {
            this.damage.add(new DamageInfo(this, dmg));
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly("TheGreatSeptimus");
        }
    }

    @Override
    public void takeTurn() {
        
    }

    @Override
    protected void getMove(int i) {

    }
}
