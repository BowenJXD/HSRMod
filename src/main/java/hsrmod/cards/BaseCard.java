package hsrmod.cards;

import basemod.abstracts.CustomCard;
import hsrmod.utils.CardDataCol;
import hsrmod.utils.DataManager;
import java.util.Objects;

import static hsrmod.characters.MyCharacter.PlayerColorEnum.HSR_PINK;

public abstract class BaseCard extends CustomCard {
    protected int upCost;
    protected String upDescription;
    protected int upDamage;
    protected int upBlock;
    protected int upMagicNumber;
    
    
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
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (upCost != DataManager.NULL_INT) {
                cost = upCost;
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
    }
}
