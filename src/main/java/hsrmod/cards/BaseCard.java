package hsrmod.cards;

import basemod.abstracts.CustomCard;
import basemod.interfaces.OnStartBattleSubscriber;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.CommonKeywordIconsField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.CustomEnums;
import hsrmod.utils.DataManager;
import hsrmod.utils.ModHelper;

import java.util.Objects;

import static hsrmod.characters.MyCharacter.PlayerColorEnum.HSR_PINK;

public abstract class BaseCard extends CustomCard {
    protected int upCost;
    protected String upDescription;
    protected int upDamage;
    protected int upBlock;
    protected int upMagicNumber;
    
    public int toughnessReduction = 2;
    public ElementType elementType = ElementType.None;
    public int energyCost = 0;
    public boolean followedUp = false;
    public boolean inBattle = false;
    public boolean inHand = false;
    
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
        
        this.damage = this.baseDamage = DataManager.getInstance().getCardDataInt(id, CardDataCol.Damage);
        this.block = this.baseBlock = DataManager.getInstance().getCardDataInt(id, CardDataCol.Block);
        this.magicNumber = this.baseMagicNumber = DataManager.getInstance().getCardDataInt(id, CardDataCol.MagicNumber);
        
        this.upCost = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeCost);
        this.upDescription = DataManager.getInstance().getCardData(id, CardDataCol.UpgradeDescription);
        this.upDamage = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeDamage);
        this.upBlock = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeBlock);
        this.upMagicNumber = DataManager.getInstance().getCardDataInt(id, CardDataCol.UpgradeMagicNumber);

        CardTags pathTag = getPathTag(DataManager.getInstance().getCardData(id, CardDataCol.Path));
        if (pathTag != null) tags.add(pathTag);
        elementType = getElementType(DataManager.getInstance().getCardData(id, CardDataCol.Element));
        
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
        AbstractPower power = AbstractDungeon.player.getPower(EnergyPower.POWER_ID);
        boolean result = power != null && power.amount >= energyCost;
        if (!result){
            cantUseMessage = "我没有足够的充能。";
        }
        return result;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!checkEnergy()) return;
        if (energyCost != 0) addToBot(new ApplyPowerAction(p, p, new EnergyPower(p, -energyCost), -energyCost));
        onUse(p, m);
        ModHelper.addToBotAbstract( () -> this.followedUp = false);
    }
    
    public abstract void onUse(AbstractPlayer p, AbstractMonster m);

    public static CardTags getPathTag(String path){
        CardTags result = null;
        if (path.contains("欢愉")) result = CustomEnums.ELATION;
        else if (path.contains("毁灭")) result = CustomEnums.DESTRUCTION;
        else if (path.contains("虚无")) result = CustomEnums.NIHILITY;
        else if (path.contains("繁育")) result = CustomEnums.PROPAGATION;
        else if (path.contains("智识")) result = CustomEnums.ERUDITION;
        else if (path.contains("丰饶")) result = CustomEnums.ABUNDANCE;
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
}
