package hsrmod.monsters.TheBeyond;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import basemod.interfaces.OnCardUseSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import hsrmod.cards.base.Danheng0;
import hsrmod.cards.base.Himeko0;
import hsrmod.cards.base.March7th0;
import hsrmod.cards.base.Welt0;
import hsrmod.cards.uncommon.Robin1;
import hsrmod.misc.Encounter;
import hsrmod.misc.PathDefine;
import hsrmod.modcore.HSRMod;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.enemyOnly.*;
import hsrmod.powers.misc.BrokenPower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.List;

public class TheGreatSeptimus extends BaseMonster implements OnCardUseSubscriber {
    public static final String ID = TheGreatSeptimus.class.getSimpleName();
    
    int blockGain = 77;
    int numDamage = 7;
    int powerAmount = 7;
    
    List<String> cardsCache;
    
    public TheGreatSeptimus() {
        super(ID, 400F, 512F, -100F, 0.0F);
        
        if (ModHelper.moreHPAscension(type))
            powerAmount++;
        if (ModHelper.specialAscension(type))
            powerAmount++;

        setDamages(17, 27, 37, 7, 17);
        if (BaseMod.hasModID("spireTogether:")) {
            tv = 27;
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        BaseMod.subscribe(this);
        cardsCache = new ArrayList<>();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            CardCrawlGame.music.unsilenceBGM();
            AbstractDungeon.scene.fadeOutAmbiance();
            AbstractDungeon.getCurrRoom().playBgmInstantly(Encounter.SALUTATIONS_OF_ASHEN_DREAMS);
        }
        addToBot(new ApplyPowerAction(this, this, new WalkInTheLightPower(this, powerAmount), powerAmount));
        addToBot(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day1", 2));
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        
        if (this.nextMove < 7) {
            addToBot(new ShoutAction(this, DIALOG[nextMove], 1.0F, 2.0F));
            ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day" + (this.nextMove + 1), 2));
        }
        
        switch (this.nextMove) {
            case 1:
                // AbstractDungeon.topLevelEffectsQueue.add(new PlayVideoEffect(PathDefine.VIDEO_PATH + ID + ".webm"));
                addToBot(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                addToBot(new ApplyPowerAction(p, this, new AlienDreamPower(p, 1), 1));
                break;
            case 2:
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                if (p.energy.energy - ModHelper.getPowerCount(p, EnergyDownPower.POWER_ID) > 3)
                    addToBot(new ApplyPowerAction(p, this, new EnergyDownPower(p, 1), 1));
                break;
            case 3:
                addToBot(new DamageAction(p, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                addToBot(new ApplyPowerAction(p, this, new WeakPower(p, 2, true), 2));
                addToBot(new ApplyPowerAction(p, this, new FrailPower(p, 2, true), 2));
                break;
            case 4:
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[7], 1), 1));
                break;
            case 5:
                if (hasPower(ChargingPower.POWER_ID)) {
                    for (int i = 0; i < numDamage; i++) {
                        addToBot(new DamageAction(p, this.damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                    }
                }
                break;
            case 6:
                addToBot(new GainBlockAction(this, this, blockGain));
                addToBot(new ApplyPowerAction(this, this, new IfWeLiveInTheLightPower(this, 1), 1));
                addToBot(new ApplyPowerAction(this, this, new ChargingPower(this, MOVES[6], 1), 1));
                break;
            case 7:
                if (AbstractDungeon.miscRng.randomBoolean()) {
                    addToBot(new ShoutAction(this, DIALOG[7], 3.0F, 4.0F));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day8", 3));
                }
                else {
                    addToBot(new ShoutAction(this, DIALOG[8], 3.0F, 4.0F));
                    ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day9", 3));
                }
                
                addToBot(new RemoveSpecificPowerAction(this, this, ChargingPower.POWER_ID));
                for (int i = 0; i < numDamage; i++) {
                    addToBot(new DamageAction(p, this.damage.get(4), AbstractGameAction.AttackEffect.SMASH));
                }
                addToBot(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                break;
        }
        
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        switch (turnCount) {
            case 0: 
                setMove(MOVES[0], (byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
                break;
            case 1:
                setMove(MOVES[1], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                break;
            case 2:
                setMove(MOVES[2], (byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(2).base);
                break;
            case 3:
                setMove(MOVES[3], (byte) 4, Intent.UNKNOWN);
                break;
            case 4:
                setMove(MOVES[4], (byte) 5, Intent.ATTACK, this.damage.get(3).base, numDamage, true);
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != this && !m.isDead && !m.isDying && !m.halfDead && m.currentHealth > 0 && !m.hasPower(BrokenPower.POWER_ID)) {
                        addToBot(new ApplyPowerAction(m, this, new MultiMovePower(m, 1), 1));
                    }
                }
                break;
            case 5:
                setMove(MOVES[5], (byte) 6, Intent.DEFEND_BUFF);
                break;
            case 6:
                setMove(MOVES[6], (byte) 7, Intent.ATTACK, this.damage.get(4).base, numDamage, true);
                break;
        }
        
        turnCount++;
        if (turnCount > 6) { turnCount = 0; }
    }

    @Override
    public void die() {
        BaseMod.unsubscribe(this);
        AbstractDungeon.getCurrRoom().cannotLose = false;
        
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(ID + "_Day10", 3.0F));
        
        super.die();
        this.onBossVictoryLogic();
        this.onFinalBossVictoryLogic();
        ModHelper.killAllMinions();
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        if (!SubscriptionManager.checkSubscriber(this)) return;
        String phrase = null;
        String voice = ID + "_Crew";
        int duration = 3;
        if (cardsCache.contains(abstractCard.cardID)) {
            return;
        }
        else if (abstractCard.cardID.equals(HSRMod.makePath(Welt0.ID))) {
            phrase = DIALOG[9];
            voice += "1";
            duration = 3;
        }
        else if (abstractCard.cardID.equals(HSRMod.makePath(March7th0.ID))) {
            phrase = DIALOG[10];
            voice += "2";
            duration = 3;
        }
        else if (abstractCard.cardID.equals(HSRMod.makePath(Robin1.ID))) {
            phrase = DIALOG[11];
            voice += "3";
            duration = 4;
        }
        else if (abstractCard.cardID.equals(HSRMod.makePath(Himeko0.ID))) {
            phrase = DIALOG[12];
            voice += "4";
            duration = 2;
        }
        else if (abstractCard.cardID.equals(HSRMod.makePath(Danheng0.ID))) {
            phrase = DIALOG[13];
            voice += "5";
            duration = 2;
        }
        else return;
        cardsCache.add(abstractCard.cardID);
        if (phrase != null) addToBot(new TalkAction(true, phrase, duration, duration + 1));
        String finalVoice = voice;
        ModHelper.addToBotAbstract(() -> CardCrawlGame.sound.playV(finalVoice, 3));
    }
}
