package androidTestMod.powers.misc;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import androidTestMod.actions.ReduceToughnessAction;
import androidTestMod.characters.StellaCharacter;
import androidTestMod.misc.IMultiToughness;
import androidTestMod.modcore.ElementType;
import androidTestMod.modcore.ElementalDamageInfo;
import androidTestMod.modcore.AndroidTestMod;
import androidTestMod.modcore.HSRModConfig;
import androidTestMod.powers.BuffPower;

public class ToughnessPower extends BuffPower {
    public static final String POWER_ID = AndroidTestMod.makePath(ToughnessPower.class.getSimpleName());
    static float HEALTH_BAR_OFFSET_Y = -30F * Settings.scale;
    static float HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
    static float HEALTH_BG_OFFSET_X = 31.0F * Settings.scale;
    static Color positiveColor = new Color(0.8F, 0.8F, 0.8F, 0.8F);
    static Color negativeColor = new Color(1.0F, 0.6F, 0.6F, 0.8F);
    static float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;
    static float HB_Y_OFFSET_DIST = 12.0F * Settings.scale;
    static float hbYOffsetMonster = HB_Y_OFFSET_DIST * 2.0F;
    static float hbYOffsetPlayer = HB_Y_OFFSET_DIST * 4.0F;

    int stackLimit = 0;

    public ToughnessPower(AbstractCreature owner, int Amount, int stackLimit) {
        super(POWER_ID, owner, Amount);
        this.priority = 20;
        this.stackLimit = stackLimit;
        this.amount = Math.min(Math.max(-stackLimit, Amount), stackLimit);
        this.canGoNegative = true;

        this.updateDescription();
    }

    public ToughnessPower(AbstractCreature owner, int Amount) {
        this(owner, Amount, getStackLimit(owner));
    }

    public ToughnessPower(AbstractCreature owner) {
        this(owner, getStackLimit(owner));
    }

    @Override
    public void updateDescription() {
        if (amount > 0)
            this.description = String.format(DESCRIPTIONS[0], this.amount, stackLimit);
        else
            this.description = String.format(DESCRIPTIONS[1], -this.amount, stackLimit);
    }

    @Override
    public void atEndOfRound() {
        super.atEndOfRound();
        if (amount <= 0 && !owner.hasPower(LockToughnessPower.POWER_ID)) {
            alterPower(stackLimit * 2);
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL)
            return damage * (1 - (this.amount / 100.0F));
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!(info instanceof ElementalDamageInfo)
                && !(AbstractDungeon.player instanceof StellaCharacter)
                && info.type != DamageInfo.DamageType.HP_LOSS) {
            addToTop(new ReduceToughnessAction(owner, info.owner, 2, ElementType.None));
        }
        return damageAmount;
    }

    @Override
    public void stackPower(int stackAmount) {
        if (stackAmount == stackLimit && owner != AbstractDungeon.player && BaseMod.hasModID("spireTogether:")) return;
        alterPower(stackAmount);
    }

    public void alterPower(int i) {
        if (owner.hasPower(LockToughnessPower.POWER_ID)) return;
        this.fontScale = 8.0F;
        this.amount += i;

        if (this.amount < -stackLimit) {
            this.amount = -stackLimit;
        } else if (this.amount > stackLimit) {
            this.amount = stackLimit;
        }

        // addToTop(new TalkAction(owner, String.format("Amount before: %d, Amount: %d, stack limit: %d!", temp, stackAmount, stackLimit), 1.0F, 2.0F));
        type = this.amount > 0 ? PowerType.BUFF : PowerType.DEBUFF;
    }

    @Override
    public void reducePower(int reduceAmount) {
        alterPower(-reduceAmount);
    }

    public static int getStackLimit(AbstractCreature c) {
        int result = 0;
        if (c.hasPower(ToughnessPower.POWER_ID)) {
            ToughnessPower power = (ToughnessPower) c.getPower(ToughnessPower.POWER_ID);
            if (power != null) result = power.stackLimit;
        } else if (c instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) c;
            switch (m.type) {
                case NORMAL:
                    result = AbstractDungeon.ascensionLevel < 7 ? 4 : 5;
                    break;
                case ELITE:
                    result = AbstractDungeon.ascensionLevel < 8 ? 6 : 7;
                    break;
                case BOSS:
                    result = AbstractDungeon.ascensionLevel < 9 ? 10 : 12;
                    break;
            }
            result *= Math.min(AbstractDungeon.actNum, 4);
            if (BaseMod.hasModID("spireTogether:")) {
                result += result / 2;
            }
            int count = HSRModConfig.getActiveTPCount();
            if (count > 0) {
                result += (int) (result * HSRModConfig.getTVInc());
            }
        } else if (c instanceof AbstractPlayer) {
            result = 50;
        }
        return result;
    }

    @Override
    public void renderIcons(SpriteBatch sb, float ix, float iy, Color c) {
        super.renderIcons(sb, ix, iy, c);
        float x = owner.hb.cX - owner.hb.width / 2.0F;
        float y = owner.hb.cY - owner.hb.height / 2.0F + (owner instanceof AbstractPlayer ? hbYOffsetPlayer : hbYOffsetMonster);
        float ratio = Math.max(0, 1.0F * Math.abs(amount) / ToughnessPower.getStackLimit(owner));

        try {
            sb.setColor(new Color(0.1F, 0.1F, 0.1F, owner.hbAlpha));

            // Draw the bar background
            sb.draw(ImageMaster.HB_SHADOW_L, x - HEALTH_BAR_HEIGHT, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_B, x, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, owner.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HB_SHADOW_R, x + owner.hb.width, y - HEALTH_BG_OFFSET_X + 3.0F * Settings.scale, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);

            // Draw the toughness bar from left to right
            if (owner.hasPower(LockToughnessPower.POWER_ID)) {
                sb.setColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g, Color.DARK_GRAY.b, Color.DARK_GRAY.a * owner.hbAlpha);
            } else if (amount <= 0) {
                sb.setColor(negativeColor.r, negativeColor.g, negativeColor.b, negativeColor.a * owner.hbAlpha);
            } else {
                sb.setColor(positiveColor.r, positiveColor.g, positiveColor.b, positiveColor.a * owner.hbAlpha);
            }

            sb.draw(ImageMaster.HEALTH_BAR_L, x - HEALTH_BAR_HEIGHT, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_B, x, y + HEALTH_BAR_OFFSET_Y, ratio * owner.hb.width, HEALTH_BAR_HEIGHT);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + ratio * owner.hb.width, y + HEALTH_BAR_OFFSET_Y, HEALTH_BAR_HEIGHT, HEALTH_BAR_HEIGHT);
        } catch (Exception exception) {
            AndroidTestMod.logger.error(exception.getMessage());
        }
        
        String text = amount + "/" + ToughnessPower.getStackLimit(owner);
        int barCount = 0;
        for (AbstractPower p : owner.powers) {
            int i = p instanceof IMultiToughness ? ((IMultiToughness) p).getToughnessBarCount() : 0;
            barCount += i;
        }
        if (barCount > 1) {
            text += "×" + barCount;
        }
        FontHelper.renderFontCentered(sb, FontHelper.healthInfoFont, text,
                owner.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale,
                new Color(0.8F, 0.8F, 0.8F, 1.0F));
    }
}
