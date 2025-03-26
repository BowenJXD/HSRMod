package hsrmod.powers.enemyOnly;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.subscribers.PreBreakSubscriber;
import hsrmod.subscribers.SubscriptionManager;

public class BanPower extends AbstractPower implements PreBreakSubscriber {
    public static final String POWER_ID = HSRMod.makePath(BanPower.class.getSimpleName());
    public String[] DESCRIPTIONS;
    public PowerStrings powerStrings;
    
    BanType banType;
    int thornDamage = 2;
    
    CardBorderGlowManager.GlowInfo glowInfo;
    
    public BanPower(AbstractCreature owner, BanType banType) {
        this.ID = POWER_ID;
        this.name = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).NAME;
        this.DESCRIPTIONS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID).DESCRIPTIONS;
        this.owner = owner;
        this.amount = -1;
        this.type = NeutralPowertypePatch.NEUTRAL;
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        this.banType = banType;
        switch (banType) {
            case ATTACK:
                loadRegion("Ban_AttackPower");
                break;
            case SKILL:
                loadRegion("Ban_SkillPower");
                break;
            case DAMAGE:
                loadRegion("Ban_DamagePower");
                break;
        }
        updateDescription();
        
        glowInfo = new CardBorderGlowManager.GlowInfo() {
            @Override
            public boolean test(AbstractCard abstractCard) {
                return (abstractCard.type == AbstractCard.CardType.ATTACK && BanPower.this.banType == BanType.ATTACK)
                        || (abstractCard.type == AbstractCard.CardType.SKILL && BanPower.this.banType == BanType.SKILL);
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return Color.RED;
            }

            @Override
            public String glowID() {
                return POWER_ID;
            }
        };
    }
    
    public BanPower(AbstractCreature owner){
        this(owner, BanType.values()[AbstractDungeon.aiRng.random(BanType.values().length - 1)]);
    }

    @Override
    public void updateDescription() {
        switch (banType) {
            case ATTACK:
                this.description = DESCRIPTIONS[0];
                break;
            case SKILL:
                this.description = DESCRIPTIONS[1];
                break;
            case DAMAGE:
                this.description = String.format(DESCRIPTIONS[2], thornDamage);
                break;
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        SubscriptionManager.subscribe(this);
        CardBorderGlowManager.addGlowInfo(glowInfo);
    }

    @Override
    public void onRemove() {
        super.onRemove();
        SubscriptionManager.unsubscribe(this);
        CardBorderGlowManager.removeGlowInfo(glowInfo);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);
        if (banType == BanType.ATTACK && card.type == AbstractCard.CardType.ATTACK) {
            flash();
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, true));
        }
        if (banType == BanType.SKILL && card.type == AbstractCard.CardType.SKILL) {
            AbstractCard c = AbstractDungeon.player.hand.getRandomCard(true);
            if (c != null) {
                flash();
                AbstractGameAction actionCache = new AbstractGameAction() {
                    @Override
                    public void update() {
                        c.setCostForTurn(c.costForTurn + 1);
                        isDone = true;
                    }
                };
                addToBot(actionCache);
            }
        }
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (banType == BanType.DAMAGE 
                && info.type != DamageInfo.DamageType.THORNS 
                && info.type != DamageInfo.DamageType.HP_LOSS 
                && info.owner != null 
                && info.owner != this.owner) {
            this.flash();
            this.addToTop(new DamageAction(info.owner, new DamageInfo(this.owner, thornDamage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
        }

        return damageAmount;
    }
    
    @Override
    protected void loadRegion(String fileName) {
        String path128 = String.format("HSRModResources/img/powers/%s128.png", fileName);
        String path48 = String.format("HSRModResources/img/powers/%s48.png", fileName);
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 128, 128);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 48, 48);
    }

    @Override
    public void preBreak(ElementalDamageInfo info, AbstractCreature target) {
        if (SubscriptionManager.checkSubscriber(this)
                && target == owner) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    public static enum BanType {
        ATTACK,
        SKILL,
        DAMAGE,
    }
}
