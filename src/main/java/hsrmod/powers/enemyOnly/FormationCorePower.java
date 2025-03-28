package hsrmod.powers.enemyOnly;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.PreElementalDamageSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.GeneralUtil;
import hsrmod.utils.ModHelper;
import hsrmod.utils.PathDefine;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class FormationCorePower extends StatePower implements PreElementalDamageSubscriber, PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(FormationCorePower.class.getSimpleName());

    List<Tag> tags;
    final boolean hidden;
    float particleTimer = 0;

    public FormationCorePower(AbstractCreature owner, Tag tag, boolean hidden) {
        super(POWER_ID, owner);
        priority = 9;
        this.tags = new ArrayList<>();
        this.tags.add(tag);
        this.hidden = hidden;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (hidden) {
            description = DESCRIPTIONS[1];
        } else {
            StringJoiner sj = new StringJoiner("&");
            for (Tag tag : tags) {
                sj.add(DESCRIPTIONS[tag.ordinal() + 2]);
            }
            description = String.format(DESCRIPTIONS[0], sj);
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
        List<Tag> tempTags = new ArrayList<>();
        AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
            FormationCorePower power = (FormationCorePower) m.getPower(POWER_ID);
            if (power != null) {
                tempTags.addAll(power.tags);
                for (Tag tag : tags) {
                    if (!power.tags.contains(tag)) {
                        power.tags.add(tag);
                    }
                }
                power.updateDescription();
            }
        });
        if (!tempTags.isEmpty()) {
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
                CommonKeywordIconsField.useIcons.set(c, true);
            });
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (owner == null || !owner.hasPower(POWER_ID))
            return;
        particleTimer += Gdx.graphics.getDeltaTime();
        if (particleTimer > 1) {
            particleTimer--;
            String path = "";
            for (int i = 0; i < tags.size(); i++) {
                Tag tag = tags.get(i);
                switch (tag) {
                    case EXHAUST:
                        path = "Exhaust.png";
                        break;
                    case ETHEREAL:
                        path = "Ethereal.png";
                        break;
                    case INNATE:
                        path = "Innate.png";
                        break;
                    case RETAIN:
                        path = "Retain.png";
                        break;
                }
                if (!path.isEmpty()) {
                    AbstractGameEffect effect = new VfxBuilder(new Texture(PathDefine.UI_PATH + "icons/" + path), 
                            owner.hb.cX + owner.hb.width/2,
                            owner.hb.cY + owner.hb.height/2 - (i+1)*owner.hb.height/(tags.size()+1),
                            1.02f)
                            .build();
                    AbstractDungeon.effectList.add(effect);
                }
            }
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
            } else if (card.exhaust && tags.contains(Tag.EXHAUST)) {
                canReduceToughness = true;
            } else if (card.isEthereal && tags.contains(Tag.ETHEREAL)) {
                canReduceToughness = true;
            } else if (card.isInnate && tags.contains(Tag.INNATE)) {
                canReduceToughness = true;
            } else if ((card.retain || card.selfRetain) && tags.contains(Tag.RETAIN)) {
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
