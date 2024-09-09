package hsrmod.cards.uncommon;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerDamagedSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.ElementType;
import hsrmod.powers.misc.EnergyPower;
import hsrmod.utils.ModHelper;
import hsrmod.subscribers.SubscribeManager;

import static hsrmod.utils.CustomEnums.FOLLOW_UP;

public class Yunli1 extends BaseCard implements OnPlayerDamagedSubscriber {
    public static final String ID = Yunli1.class.getSimpleName();
    
    boolean canBeUsed = false;
    
    int energyExhaust = 120;
    
    public Yunli1() {
        super(ID);
        tags.add(FOLLOW_UP);
        selfRetain = true;
        
        BaseMod.subscribe(this);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && canBeUsed;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {        
        ModHelper.addToBotAbstract(this::execute);
    }
    
    void execute(){
        if (AbstractDungeon.player.getPower(EnergyPower.POWER_ID).amount < energyExhaust) return;
        ModHelper.addToTopAbstract(this::execute);
        for (int i = 0; i < 2; i++) {
            AbstractMonster randomMonster = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
            addToTop(new ElementalDamageAction(randomMonster, new DamageInfo(AbstractDungeon.player, damage), ElementType.Physical, 1, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        }
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, -energyExhaust), -energyExhaust));
    }

    @Override
    public void atTurnStartPreDraw() {
        if (canBeUsed) {
            addToBot(new FollowUpAction(this));
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if (!SubscribeManager.checkSubscriber(this)
                || !AbstractDungeon.player.hand.contains(this)) 
            return i;
        canBeUsed = true;
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnergyPower(AbstractDungeon.player, 10), 10));
        return Math.max(0, i - magicNumber);
    }

}
