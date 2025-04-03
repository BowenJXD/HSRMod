package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.signature.utils.SignatureHelper;
import hsrmod.subscribers.SubscriptionManager;
import hsrmod.utils.ModHelper;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

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
        if (count == 4) {
            SignatureHelper.unlock(cardID, true);
        }
        
        ElementalDamageAction action = new ElementalDamageAction(randomMonster, ElementalDamageInfo.makeInfo(this), AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        
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
