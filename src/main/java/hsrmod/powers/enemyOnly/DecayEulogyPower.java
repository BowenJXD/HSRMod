package hsrmod.powers.enemyOnly;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.misc.IMultiToughness;
import hsrmod.modcore.ElementType;
import hsrmod.modcore.ElementalDamageInfo;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.StatePower;
import hsrmod.powers.misc.ToughnessPower;
import hsrmod.utils.ModHelper;

public class DecayEulogyPower extends StatePower {
    public static final String POWER_ID = HSRMod.makePath(DecayEulogyPower.class.getSimpleName());

    public DecayEulogyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount, amount, amount, amount);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        addToBot(new VFXAction(new ExplosionSmallEffect(owner.hb.cX, owner.hb.cY)));
        addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, amount));
        AbstractDungeon.getMonsters().monsters.stream().filter(ModHelper::check).forEach(m -> {
            int dmg = m.maxHealth * amount / 100;
            int tr = ToughnessPower.getStackLimit(m) * amount / 100;
            int multiToughness = m.powers.stream().mapToInt(p -> p instanceof IMultiToughness ? ((IMultiToughness) p).getToughnessBarCount() : 0).sum();
            if (multiToughness > 0) {
                tr *= multiToughness;
            }
            // int dmg = amount;
            // int tr = amount;
            addToBot(
                    new ElementalDamageAction(
                            m,
                            new ElementalDamageInfo(owner, dmg, DamageInfo.DamageType.NORMAL, ElementType.None, tr),
                            AbstractGameAction.AttackEffect.NONE)
                            .setIsSourceNullable(true)
                            .setDoApplyPower(true)
            );
            addToBot(new ApplyPowerAction(m, m, new DefensePower(m, -amount)));
        });
    }

    @Override
    protected void loadRegion(String fileName) {
        fileName = "explosive";
        this.region48 = atlas.findRegion("48/" + fileName);
        this.region128 = atlas.findRegion("128/" + fileName);
    }
}
