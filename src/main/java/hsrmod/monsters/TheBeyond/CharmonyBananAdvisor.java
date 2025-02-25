package hsrmod.monsters.TheBeyond;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.monsters.BaseMonster;
import hsrmod.powers.breaks.BurnPower;
import hsrmod.powers.enemyOnly.ChannelPower;
import hsrmod.powers.enemyOnly.TutoringPower;
import hsrmod.utils.ModHelper;

import java.util.Iterator;

public class CharmonyBananAdvisor extends BaseMonster implements IBanana {
    public static final String ID = CharmonyBananAdvisor.class.getSimpleName();
    
    int channelCount = 1;
    boolean inClass;
    
    public CharmonyBananAdvisor(float x, float y, boolean inClass) {
        super(ID, 300, 384, x, y);
        this.inClass = inClass;
        
        addMove(Intent.ATTACK_DEBUFF, 16, mi->{
            attack(mi, AbstractGameAction.AttackEffect.FIRE, AttackAnim.FAST);
            addToBot(new ApplyPowerAction(p, this, new BurnPower(p, this, 2)));
        });
        addMove(Intent.ATTACK_DEBUFF, 24, mi->{
            attack(mi, AbstractGameAction.AttackEffect.FIRE, AttackAnim.SLOW);
            AbstractCard card = new Burn();
            if (moreDamageAs) card.upgrade();
            addToBot(new MakeTempCardInDiscardAndDeckAction(card));
            if (specialAs) addToBot(new MakeTempCardInHandAction(card));
        });
        addMove(Intent.UNKNOWN, mi->{
            addToBot(new ApplyPowerAction(p, this, new StrengthPower(p, 2)));
        });
        addMove(Intent.UNKNOWN, mi->{});
        addMove(Intent.BUFF, mi->{
            respawn();
            addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));
            addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, -channelCount, ChannelPower.ChannelType.OFFCLASS_CLASSROOM)));
        });
        
        channelCount = specialAs ? 2 : 1;
        bgm = "Bananas";
    }

    @Override
    public void takeTurn() {
        AbstractMonster monster = ModHelper.getRandomMonster(m -> !m.hasPower(TutoringPower.POWER_ID) && m.hasPower(ChannelPower.POWER_ID), true);
        if (monster != null) addToBot(new ApplyPowerAction(monster, this, new TutoringPower(monster)));
        if (!hasPower(TutoringPower.POWER_ID)) addToBot(new ApplyPowerAction(this, this, new TutoringPower(this)));
        super.takeTurn();
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        addToBot(new CannotLoseAction());
        AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
            addToBot(new ApplyPowerAction(m, this, new RegrowPower(m)));
        });
        addToBot(new ApplyPowerAction(this, this, new ChannelPower(this, channelCount * (inClass ? -1 : 1), ChannelPower.ChannelType.OFFCLASS_CLASSROOM), 0));
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte) 3)) {
            setMove(4);
        } else if (halfDead) {
            setMove(3);
        } else if (!inClass) {
            setMove(2);
        } else if ((lastTwoMoves((byte) 0) || lastMove((byte) 0))) {
            setMove(1);
        } else {
            setMove(0);
        }
    }

    @Override
    public void processChange(boolean isInClass) {
        inClass = isInClass;
        setImg();
        rollMove();
        createIntent();
    }

    @Override
    public void setImg() {

    }

    @Override
    public void die() {
        super.die();
        processDie(this);
    }

}
