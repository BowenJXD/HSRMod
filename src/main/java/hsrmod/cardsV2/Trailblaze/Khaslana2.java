package hsrmod.cardsV2.Trailblaze;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import hsrmod.actions.BouncingAction;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.cards.BaseCard;
import hsrmod.modcore.CustomEnums;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.powers.enemyOnly.DefensePower;
import hsrmod.powers.uniqueBuffs.ScourgePower;
import hsrmod.utils.ModHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class Khaslana2 extends BaseCard {
    public static final String ID = Khaslana2.class.getSimpleName();

    int defenseCount = 50;
    float aoeDmgP = 0.25f;
    float bounceDmgP = 0.25f;

    public Khaslana2() {
        super(ID);
        tags.add(CustomEnums.CHRYSOS_HEIR);
        tags.add(CustomEnums.TERRITORY);
        isMultiDamage = true;
        returnToHand = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        shout(0, 1);

        int mCount = (int) AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).count();
        if (mCount > 0) {
            addToBot(new ApplyPowerAction(p, p, new ScourgePower(p, mCount)));
        }
        addToBot(new ApplyPowerAction(p, p, new DefensePower(p, defenseCount)));
        addToBot(new MonsterStartTurnAction());
        AtomicInteger dmgCache = new AtomicInteger(damage);
        ModHelper.addToBotAbstract(() -> {
            AbstractDungeon.getMonsters().queueMonsters();
            AbstractDungeon.effectsQueue.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    if (!AbstractDungeon.actionManager.monsterQueue.isEmpty()) return;
                    isDone = true;
                    ModHelper.addToBotAbstract(() -> AbstractDungeon.getMonsters().monsters.stream()
                            .filter(ModHelper::check).forEach(AbstractMonster::createIntent));
                    
                    if (ModHelper.getPowerCount(p, DefensePower.POWER_ID) == defenseCount)
                        addToBot(new RemoveSpecificPowerAction(p, p, DefensePower.POWER_ID));
                    else
                        addToBot(new ReducePowerAction(p, p, DefensePower.POWER_ID, defenseCount));

                    addToBot(new VFXAction(new CleaveEffect()));

                    int[] damageMatrix = DamageInfo.createDamageMatrix(Math.round(dmgCache.get() * aoeDmgP), true);
                    addToBot(new ElementalDamageAllAction(p, damageMatrix, damageTypeForTurn, elementType, 
                            Math.round(tr * aoeDmgP), AbstractGameAction.AttackEffect.SLASH_VERTICAL)
                            .setBaseCard(Khaslana2.this));
                    ModHelper.addToBotAbstract(() -> {
                        AbstractCreature target = ModHelper.betterGetRandomMonster();
                        ElementalDamageAction action = new ElementalDamageAction(
                                target, 
                                new ElementalDamageInfo(
                                        p, 
                                        Math.round(dmgCache.get() * bounceDmgP), 
                                        elementType, 
                                        Math.round(tr * aoeDmgP)
                                ).setBaseCard(Khaslana2.this), 
                                AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                        addToTop(new BouncingAction(target, magicNumber, action));
                    });
                }

                @Override
                public void render(SpriteBatch spriteBatch) {

                }

                @Override
                public void dispose() {

                }
            });
        });
    }
}
