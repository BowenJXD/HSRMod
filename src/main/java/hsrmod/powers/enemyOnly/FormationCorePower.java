package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormationCorePower extends StatePower implements PreElementalDamageSubscriber, PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(FormationCorePower.class.getSimpleName());

    Tag tag;
    boolean hidden = false;

    public FormationCorePower(AbstractCreature owner, Tag tag, boolean hidden) {
        super(POWER_ID, owner);
        priority = 9;
        this.tag = tag;
        this.hidden = hidden;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (hidden) {
            description = DESCRIPTIONS[1];
        } else {
            description = String.format(DESCRIPTIONS[0], DESCRIPTIONS[tag.ordinal() + 2]);
        }
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
        onSpecificTrigger();
    }

    @Override
    public void onDeath() {
        super.onDeath();
        onSpecificTrigger();
    }

    @Override
    public void onSpecificTrigger() {
        super.onSpecificTrigger();
        List<Tag> tags = AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).map(m -> {
            FormationCorePower power = (FormationCorePower) m.getPower(POWER_ID);
            if (power != null) {
                return power.tag;
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (!tags.isEmpty()) {
            AbstractDungeon.player.hand.group.forEach(c -> {
                Tag t = GeneralUtil.getRandomElement(tags, AbstractDungeon.aiRng);
                if (t != null) {
                    switch (t) {
                        case EXHAUST:
                            c.exhaust = true;
                            break;
                        case ETHEREAL:
                            c.isEthereal = true;
                            break;
                        case INNATE:
                            c.isInnate = true;
                            break;
                        case RETAIN:
                            c.selfRetain = true;
                            break;
                    }
                }
            });
        }
    }

    @Override
    public float preElementalDamage(ElementalDamageAction action, float dmg) {
        if (SubscriptionManager.checkSubscriber(this)
                && action.target == owner) {
            boolean canReduceToughness = false;
            AbstractCard card = action.info.card;
            if (card == null) {
                canReduceToughness = false;
            } else if (card.exhaust && tag == Tag.EXHAUST) {
                canReduceToughness = true;
            } else if (card.isEthereal && tag == Tag.ETHEREAL) {
                canReduceToughness = true;
            } else if (card.isInnate && tag == Tag.INNATE) {
                canReduceToughness = true;
            } else if ((card.retain || card.selfRetain) && tag == Tag.RETAIN) {
                canReduceToughness = true;
            }
            if (!canReduceToughness) {
                action.info.tr = 0;
            }
        }
        return dmg;
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this) && target == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
            addToBot(new InstantKillAction(owner));
        }
    }

    public enum Tag {
        EXHAUST,
        ETHEREAL,
        INNATE,
        RETAIN,
    }
}
