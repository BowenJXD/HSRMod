package hsrmod.cardsV2.Remembrance;

import basemod.BaseMod;
import basemod.interfaces.MaxHPChangeSubscriber;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import hsrmod.cards.BaseCard;
import hsrmod.effects.PortraitDisplayEffect;
import hsrmod.misc.VideoManager;
import hsrmod.modcore.CustomEnums;
import hsrmod.powers.uniqueBuffs.LostNetherlandPower;
import hsrmod.signature.patches.card.SignaturePatch;
import hsrmod.subscribers.PostHPUpdateSubscriber;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

public class Castorice3 extends BaseCard implements PostHPUpdateSubscriber {
    public static final String ID = Castorice3.class.getSimpleName();

    boolean moved = false;
    
    public Castorice3() {
        super(ID);
        exhaust = true;
        MultiCardPreview.add(this, new Pollux1(), new Pollux3());
        MultiCardPreview.horizontalOnly(this);
        tags.add(CustomEnums.CHRYSOS_HEIR);
    }

    @Override
    public void upgrade() {
        super.upgrade();
        MultiCardPreview.add(this, new Pollux2());
        MultiCardPreview.horizontalOnly(this);
    }

    @Override
    public void onMove(CardGroup group, boolean in) {
        super.onMove(group, in);
        if (group.type == CardGroup.CardGroupType.DRAW_PILE && in && !moved) {
            addToTop(new ExhaustSpecificCardAction(this, group));
            SubscriptionManager.subscribe(this);
            moved = true;
        } else if (group.type == CardGroup.CardGroupType.EXHAUST_PILE && in) {
            ModHelper.addToBotAbstract(() -> {
                hpCache = AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player);
            });
        } else if (group.type == CardGroup.CardGroupType.HAND && in) {
            baseMagicNumber = magicNumber;
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean result = super.canUse(p, m);
        if (result && p.filledOrbCount() == 0) {
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return result;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        if (!VideoManager.play(ID, 4, true)) {
            shout(0);
            addToBot(new SFXAction(ID));
        }
        ModHelper.addToBotAbstract(() -> {
            addToBot(new ApplyPowerAction(p, p, new LostNetherlandPower(p)));
            if (upgraded) addToBot(new MakeTempCardInHandAction(new Pollux2()));
            SubscriptionManager.getInstance().extraChecks.add(this);
        });
    }
    
    int hpCache = 0;

    @Override
    public void postHPUpdate(AbstractCreature creature) {
        if (!SubscriptionManager.checkSubscriber(this) || !AbstractDungeon.player.exhaustPile.contains(this)) {
            return;
        }
        int change = hpCache - (AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player));
        hpCache = AbstractDungeon.player.currentHealth + TempHPField.tempHp.get(AbstractDungeon.player);
        if (change <= 0) {
            return;
        }
        baseMagicNumber -= change;
        initializeDescription();
        if (baseMagicNumber > 0) {
            return;
        }
        baseMagicNumber = 0;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                addToTop(new ExhaustToHandAction(Castorice3.this));
                baseMagicNumber = magicNumber;
                isDone = true;
            }
        });
    }
}
