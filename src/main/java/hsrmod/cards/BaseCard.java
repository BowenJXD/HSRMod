package hsrmod.cards;

import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.relics.starter.*;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.Objects;

import static hsrmod.characters.StellaCharacter.PlayerColorEnum.HSR_PINK;

public abstract class BaseCard extends CustomCard implements SpawnModificationCard {
    protected int upCost;
    protected String upDescription;
    protected int upDamage;
    protected int upBlock;
    protected int upMagicNumber;
    
    public int baseTr;
    public int tr = 2;
    public int upTr;
    public boolean upgradedTr = false;
    
    public boolean isTrModified = false;
    public ElementType elementType = ElementType.None;
    public float versatility = 0;
    public int energyCost = 0;
    public boolean followedUp = false;
    public boolean inBattle = false;
    public boolean inHand = false;
    
    public CardStrings cardStrings;
    
    public BaseCard(String id) {
        super("HSRMod:" + id,
                DataManager.getInstance().getCardData(id, CardDataCol.Name),
                "HSRModResources/img/cards/" + id + ".png",
                DataManager.getInstance().getCardDataInt(id, CardDataCol.Cost),
                DataManager.getInstance().getCardData(id, CardDataCol.Description),
                CardType.valueOf(DataManager.getInstance().getCardData(id, CardDataCol.Type)),
                HSR_PINK,
                CardRarity.valueOf(DataManager.getInstance().getCardData(id, CardDataCol.Rarity)),
                CardTarget.valueOf(DataManager.getInstance().getCardData(id, CardDataCol.Target))
        );
        this.cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);
        
        this.damage = this.baseDamage = DataManager.getInstance().getCardDataInt(id, CardDataCol.Damage);
        this.block = this.baseBlock = DataManager.getInstance().getCardDataInt(id, CardDataCol.Block);
        this.magicNumber = this.baseMagicNumber = DataManager.getInstance().getCardDataInt(id, CardDataCol.MagicNumber);
        this.tr = this.baseTr = DataManager.getInstance().getCardDataInt(id, CardDataCol.ToughnessReduction);
        
        this.upCost = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeCost);
        this.upDescription = DataManager.getInstance().getCardData(id, CardDataCol.UpgradeDescription);
        this.upDamage = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeDamage);
        this.upTr = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeToughnessReduction);
        this.upBlock = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeBlock);
        this.upMagicNumber = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeMagicNumber);

        CardTags pathTag = getPathTag(DataManager.getInstance().getCardData(id, CardDataCol.Path));
        if (pathTag != null) tags.add(pathTag);
        elementType = getElementType(DataManager.getInstance().getCardData(id, CardDataCol.Element));
        versatility = getVersatility(DataManager.getInstance().getCardData(id, CardDataCol.Versatility));
        
/*        this.exhaust = rawDescription.contains("消耗。");
        this.selfRetain = rawDescription.contains("保留。");
        this.isInnate = rawDescription.contains("固有。");
        this.isEthereal = rawDescription.contains("虚无。");*/
        
        CommonKeywordIconsField.useIcons.set(this, true);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (upCost != DataManager.NULL_INT) {
                updateCost(upCost - cost);
            }
            if (!Objects.equals(upDescription, "")) {
                this.rawDescription = upDescription;
                initializeDescription();
            }
            if (upDamage != DataManager.NULL_INT) {
                upgradeDamage(upDamage - baseDamage);
            }
            if (upTr != DataManager.NULL_INT) {
                upgradeTr(upTr - baseTr);
            }
            if (upBlock != DataManager.NULL_INT) {
                upgradeBlock(upBlock - baseBlock);
            }
            if (upMagicNumber != DataManager.NULL_INT) {
                upgradeMagicNumber(upMagicNumber - baseMagicNumber);
            }
        }
        
/*        this.exhaust = upDescription.contains("消耗。");
        this.selfRetain = upDescription.contains("保留。");
        this.isInnate = upDescription.contains("固有。");
        this.isEthereal = upDescription.contains("虚无。");*/
    }
    
    public void upgradeTr(int amount) {
        this.baseTr += amount;
        this.tr = this.baseTr;
        this.upgradedTr = true;
    }

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedTr) {
            tr = baseTr;
            isTrModified = true;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        tr = SubscriptionManager.getInstance().triggerSetToughnessReduction(mo, baseTr);
        if (tr != baseTr) isTrModified = true;
        super.calculateCardDamage(mo);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        tr = SubscriptionManager.getInstance().triggerSetToughnessReduction(null, baseTr);
        if (tr != baseTr) isTrModified = true;
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isPlayerInDungeon()
                || AbstractDungeon.player == null 
                || AbstractDungeon.player.hand == null 
                || AbstractDungeon.player.hand.isEmpty()){
            inHand = false;
            inBattle = false;
            return;
        }
        if (!inHand && AbstractDungeon.player.hand.contains(this)) {
            inHand = true;
            onEnterHand();
        }
        else if (inHand && !AbstractDungeon.player.hand.contains(this)) {
            inHand = false;
            onLeaveHand();
        }
    }
    
    public void onEnterHand() { }
    public void onLeaveHand() { }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && checkEnergy();
    }

    protected boolean checkEnergy() {
        if (ModHelper.getPowerCount(EnergyPower.POWER_ID) >= energyCost 
                || SubscriptionManager.getInstance().triggerCheckUsable(this)) {
            return true;
        }
        cantUseMessage = Settings.language == Settings.GameLanguage.ZHS ? "我没有足够的充能。" : "I don't have enough charge.";
        return false;
    }

    @Override
    public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
        return checkSpawnable();
    }

    @Override
    public boolean canSpawnShop(ArrayList<AbstractCard> currentShopCards) {
        return checkSpawnable();
    }

    private boolean checkSpawnable() {
        int count = AbstractDungeon.player.masterDeck.group.stream().mapToInt(c -> c.cardID.equals(this.cardID) ? 1 : 0).sum();

        if (AbstractDungeon.cardRng.random(99) < ((checkPath() ? 100 : 50 * versatility) / (count + 1))) {
            return true;
        }
        return false;
    }

    boolean checkPath() {
        if (AbstractDungeon.player.hasRelic(WaxOfElation.ID) && tags.contains(CustomEnums.ELATION) 
                || AbstractDungeon.player.hasRelic(WaxOfDestruction.ID) && tags.contains(CustomEnums.DESTRUCTION)
                || AbstractDungeon.player.hasRelic(WaxOfNihility.ID) && tags.contains(CustomEnums.NIHILITY) 
                || AbstractDungeon.player.hasRelic(WaxOfPropagation.ID) && tags.contains(CustomEnums.PROPAGATION)
                || AbstractDungeon.player.hasRelic(WaxOfPreservation.ID) && tags.contains(CustomEnums.PRESERVATION) 
                || AbstractDungeon.player.hasRelic(WaxOfTheHunt.ID) && tags.contains(CustomEnums.THE_HUNT) 
                || AbstractDungeon.player.hasRelic(WaxOfErudition.ID) && tags.contains(CustomEnums.ERUDITION)
                /*|| AbstractDungeon.player.hasRelic(WaxOfAbundance.ID) && tags.contains(CustomEnums.ABUNDANCE) 
                || AbstractDungeon.player.hasRelic(WaxOfRemembrance.ID) && tags.contains(CustomEnums.REMEMBRANCE)*/) {
            return true;
        }
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!checkEnergy()) return;
        if (energyCost != 0) addToTop(new ApplyPowerAction(p, p, new EnergyPower(p, -energyCost), -energyCost));
        onUse(p, m);
        ModHelper.addToBotAbstract( () -> this.followedUp = false);
    }
    
    public abstract void onUse(AbstractPlayer p, AbstractMonster m);
    
    public static CardTags getPathTag(String path){
        CardTags result = null;
        if (path.contains("开拓")) result = CustomEnums.TRAILBLAZE;
        else if (path.contains("欢愉")) result = CustomEnums.ELATION;
        else if (path.contains("毁灭")) result = CustomEnums.DESTRUCTION;
        else if (path.contains("虚无")) result = CustomEnums.NIHILITY;
        else if (path.contains("繁育")) result = CustomEnums.PROPAGATION;
        else if (path.contains("存护")) result = CustomEnums.PRESERVATION;
        else if (path.contains("巡猎")) result = CustomEnums.THE_HUNT;
        else if (path.contains("智识")) result = CustomEnums.ERUDITION;
        else if (path.contains("丰饶")) result = CustomEnums.ABUNDANCE;
        else if (path.contains("记忆")) result = CustomEnums.REMEMBRANCE;
        return result;
    }
    
    public static ElementType getElementType(String element){
        ElementType result = ElementType.None;
        if (element.isEmpty()) return result;
        else if (element.contains("冰")) result = ElementType.Ice;
        else if (element.contains("物理")) result = ElementType.Physical;
        else if (element.contains("火")) result = ElementType.Fire;
        else if (element.contains("雷")) result = ElementType.Lightning;
        else if (element.contains("风")) result = ElementType.Wind;
        else if (element.contains("量子")) result = ElementType.Quantum;
        else if (element.contains("虚数")) result = ElementType.Imaginary;
        return result;
    }
    
    public static float getVersatility(String versatility){
        float result = 1;
        if (versatility.isEmpty()) return result;
        if (versatility.contains("低")) result = 1 / 2f;
        else if (versatility.contains("高")) result = 2f;
        return result;
    } 
}
