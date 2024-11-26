package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.enemyOnly.MoonRagePower;

public class SableclawWolftrooper extends AbstractMonster {
    public static final String ID = SableclawWolftrooper.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    public SableclawWolftrooper(float x, float y) {
        super(NAME, HSRMod.makePath(ID), 24, 0F, -15.0F, 169F, 169F, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        this.damage.add(new DamageInfo(this, 2));
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (hasPower(MoonRagePower.POWER_ID))
            addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new BleedingPower(AbstractDungeon.player, this, 1), 1));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, AbstractMonster.Intent.ATTACK, this.damage.get(0).base, 2, true);
    }
}
