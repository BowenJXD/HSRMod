package hsrmod.monsters.TheBeyond;

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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.breaks.BleedingPower;
import hsrmod.powers.enemyOnly.DeathExplosionPower;
import hsrmod.powers.enemyOnly.SoulGladRevelPower;

public class BubbleHound extends AbstractMonster {
    public static final String ID = BubbleHound.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;
    
    int turnCount = 0;
    int dmg = 6;
    int explosionDmg = 5;
    
    public BubbleHound(float x, float y) {
        super(NAME, HSRMod.makePath(ID), 42, 0F, -15.0F, 256, 256, PathDefine.MONSTER_PATH + ID + ".png", x, y);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;
        
        if (AbstractDungeon.ascensionLevel >= 19) {
            dmg = 20;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            dmg = 18;
        } else {
            dmg = 16;
        }
        
        this.damage.add(new DamageInfo(this, dmg));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new DeathExplosionPower(this, MOVES[1], MOVES[2], () -> {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != this && !m.isDying && !m.isDead) {
                    addToBot(new ElementalDamageAction(m, new ElementalDamageInfo(m, explosionDmg, DamageInfo.DamageType.THORNS, ElementType.Physical, 2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToBot(new ApplyPowerAction(m, this, new BleedingPower(m, m, 1), 1));
                }
            }
        })));
        addToBot(new ApplyPowerAction(this, this, new SoulGladRevelPower(this, 0, 4), 0));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 0, Intent.ATTACK, this.damage.get(0).base);
    }
}
