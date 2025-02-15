package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.LockToughnessAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.stream.Collectors;

public class ResonatePower extends StatePower implements PreElementalDamageSubscriber, PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(ResonatePower.class.getSimpleName());
    
    AbstractCard card;
    float particleTimer = 0;
    ResonateType type;
    
    public ResonatePower(AbstractCreature owner, int amount, ResonateType type) {
        super(POWER_ID, owner, amount);
        particleTimer = 0.0F;
        this.type = type;
        updateDescription();
    }
    
    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[type.ordinal()], amount);
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        this.card = null;
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        this.particleTimer -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer < 0.0F) {
            this.particleTimer = 0.33F;
            AbstractDungeon.getMonsters().monsters.stream().filter(m -> ModHelper.check(m) && m.hasPower(POWER_ID) && m != owner).forEach(m -> {
                AbstractDungeon.effectsQueue.add(new SmallLaserEffect(owner.hb.cX, owner.hb.cY, m.hb.cX, m.hb.cY));
            });
        }
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this) 
                && action.target.hasPower(POWER_ID)
                && action.info.type == DamageInfo.DamageType.NORMAL
                && action.info.card != null
                && action.info.card != card) {
            card = action.info.card;
            if (action.target != owner) {
                ElementalDamageInfo info = new ElementalDamageInfo(action.info.card);
                info.tr = 0;
                AbstractGameAction copy = new ElementalDamageAction(owner, info, action.attackEffect);
                addToTop(copy);
            } else {
                action.info.tr += this.amount;
            }
        }
        return dmg;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            AbstractMonster monster = (AbstractMonster) owner;
            if (monster != null) {
                if (monster.type == AbstractMonster.EnemyType.NORMAL && type == ResonateType.FEIXIAO) {
                    addToTop(new RemoveSpecificPowerAction(owner, owner, this));
                    addToBot(new LockToughnessAction(owner, owner));
                }
                addToBot(new RollMoveAction(monster));
                ModHelper.addToBotAbstract(monster::createIntent);
            }
        }
    }
    
    public enum ResonateType {
        FEIXIAO,
        PAST_PRESENT_AND_ETERNAL,
    }
}
