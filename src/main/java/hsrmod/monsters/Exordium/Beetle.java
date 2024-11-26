package hsrmod.monsters.Exordium;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.enemyOnly.BarrierPower;

public class Beetle extends AbstractMonster {
    public static final String ID = Beetle.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int dmg = 6;
    
    public Beetle(float x, float y) {
        super(NAME, HSRMod.makePath(ID), 15, 0F, -15.0F, 165F, 197F, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        if (AbstractDungeon.ascensionLevel >= 4) {
            dmg = 8;
        } else {
            dmg = 7;
        }
        
        this.damage.add(new DamageInfo(this, dmg));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        if (AbstractDungeon.ascensionLevel >= 19) {
            addToBot(new ApplyPowerAction(this, this, new BarrierPower(this, 1), 1));
        }
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        addToBot(new ApplyPowerAction(this, this, new BarrierPower(this, 1), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base);
    }
}
