package androidTestMod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import androidTestMod.actions.BouncingAction;
import androidTestMod.actions.ElementalDamageAction;
import androidTestMod.actions.FollowUpAction;
import androidTestMod.cards.BaseCard;
import androidTestMod.modcore.CustomEnums;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.powers.misc.EnergyPower;
import androidTestMod.subscribers.SubscriptionManager;
import androidTestMod.utils.ModHelper;

import static androidTestMod.modcore.CustomEnums.FOLLOW_UP;

public class Yunli1 extends BaseCard implements OnPlayerDamagedSubscriber {
    public static final String ID = Yunli1.class.getSimpleName();
    
    boolean canBeUsed = false;
    int count = 0;
    
    public Yunli1() {
        super(ID);
        setBaseEnergyCost(120);
        tags.add(FOLLOW_UP);
        tags.add(CustomEnums.ENERGY_COSTING);
        selfRetain = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && (canBeUsed || followedUp);
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);
        ModHelper.addToBotAbstract(this::execute);
        canBeUsed = false;
        count = 0;
    }

    @Override
    public void onEnterHand() {
        super.onEnterHand();
        BaseMod.subscribe(this);
    }

    @Override
    public void onLeaveHand() {
        super.onLeaveHand();
        BaseMod.unsubscribe(this);
    }
    
    void execute(){
        if (ModHelper.getPowerCount(AbstractDungeon.player, EnergyPower.POWER_ID) >= energyCost) {
            ModHelper.addToTopAbstract(this::execute);
        }
        AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        if (randomMonster == null) return;
        
        count++;
        
        ElementalDamageAction action = new ElementalDamageAction(randomMonster, new ElementalDamageInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        
        addToTop(new BouncingAction(randomMonster, 2, action, this));

        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, 
                new EnergyPower(AbstractDungeon.player, -energyCost), -energyCost));
    }

    @Override
    public void atTurnStartPreDraw() {
        if (canBeUsed) {
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (!SubscriptionManager.checkSubscriber(this)
                || !AbstractDungeon.player.hand.contains(this)
                || damageInfo.type != DamageInfo.DamageType.NORMAL
                || damageInfo.owner == AbstractDungeon.player) 
            return i;
        canBeUsed = true;
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, 10), 10));
        return Math.max(0, i - magicNumber);
    }

}
