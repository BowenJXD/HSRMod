package hsrmod.monsters.TheCity;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import hsrmod.cardsV2.Curse.Imprison;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.Exordium.Spider;
import hsrmod.monsters.TheBeyond.BubbleHound;
import hsrmod.powers.enemyOnly.ChargingPower;
import hsrmod.powers.enemyOnly.SanctionRatePower;
import hsrmod.utils.ModHelper;

public class AurumatonGatekeeper extends AbstractMonster {
    public static final String ID = AurumatonGatekeeper.class.getSimpleName();
    private static final MonsterStrings eventStrings = CardCrawlGame.languagePack.getMonsterStrings(HSRMod.makePath(ID));
    public static final String NAME = eventStrings.NAME;
    public static final String[] MOVES = eventStrings.MOVES;
    public static final String[] DIALOG = eventStrings.DIALOG;

    int turnCount = 0;
    int[] damages = {6, 12, 6};

    public AurumatonGatekeeper() {
        super(NAME, HSRMod.makePath(ID), 82, 0F, -15.0F, 300, 384, PathDefine.MONSTER_PATH + ID + ".png", -150, 0);
        this.type = EnemyType.NORMAL;
        this.dialogX = -150.0F * Settings.scale;
        this.dialogY = -70.0F * Settings.scale;

        if (AbstractDungeon.ascensionLevel >= 19) {
            damages[1] = 18;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            damages[1] = 14;
        } else {
            damages[1] = 10;
        }

        for (int j : damages) {
            this.damage.add(new DamageInfo(this, j));
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new ApplyPowerAction(this, this, new SanctionRatePower(this, 0), SanctionRatePower.stackCount));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        switch (nextMove) {
            case 1:
                int r = AbstractDungeon.miscRng.random(1);
                addToBot(new ShoutAction(this, DIALOG[r]));
                ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_" + r, 3.0F));
                spawnDragonfishes();
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 1, true), 1));
                addToBot(new ApplyPowerAction(this, this, new SanctionRatePower(this, SanctionRatePower.stackCount), SanctionRatePower.stackCount));
                break;
            case 3:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[4], 1), 1));
                break;
            case 4:
                if (hasPower(ChargingPower.POWER_ID)) {
                    for (int i = 0; i < 3; i++) {
                        addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }
                    addToBot(new MakeTempCardInDrawPileAction(new Imprison(), 1, true, true));
                    addToBot(new ApplyPowerAction(this, this, new SanctionRatePower(this, 0), -SanctionRatePower.stackLimit));
                    turnCount = 0;
                }
                break;
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        AbstractPower sanctionPower = getPower(SanctionRatePower.POWER_ID);
        int sanctionRate = sanctionPower != null ? sanctionPower.amount : 0;
        if (sanctionRate < SanctionRatePower.stackLimit) {
            setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            switch (turnCount % 3) {
                case 0:
                    if (AbstractDungeon.getMonsters().monsters.stream().noneMatch(c -> c instanceof IlluminationDragonfish && !c.isDead)) {
                        setMove(MOVES[0], (byte) 1, AbstractMonster.Intent.UNKNOWN);
                        break;
                    } else {
                        turnCount++;
                    }
                case 1:
                    setMove(MOVES[2], (byte) 3, Intent.ATTACK, this.damage.get(1).base);
                    break;
                case 2:
                    setMove(MOVES[3], (byte) 4, Intent.ATTACK, this.damage.get(2).base, 3, true);
                    break;
            }
            turnCount++;
        }
    }

    void spawnDragonfishes() {
        IlluminationDragonfish fish1 = new IlluminationDragonfish(-450, 0);
        addToBot(new SpawnMonsterAction(fish1, true));
        ModHelper.addToBotAbstract(fish1::usePreBattleAction);
        IlluminationDragonfish fish2 = new IlluminationDragonfish(150, 0);
        addToBot(new SpawnMonsterAction(fish2, true));
        ModHelper.addToBotAbstract(fish2::usePreBattleAction);
    }

    @Override
    public void die() {
        super.die();
        ModHelper.killAllMinions();
    }
}
