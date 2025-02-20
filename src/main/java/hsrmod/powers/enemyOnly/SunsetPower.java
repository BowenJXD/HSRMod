package hsrmod.powers.enemyOnly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GiantEyeEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import hsrmod.modcore.HSRMod;
import hsrmod.powers.DebuffPower;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SunsetPower extends DebuffPower {
    public static final String POWER_ID = HSRMod.makePath(SunsetPower.class.getSimpleName());

    public Supplier<AbstractMonster> monsterSupplier;
    
    float duration;
    private float particleTimer;

    public SunsetPower(AbstractCreature owner, int amount, Supplier<AbstractMonster> monsterSupplier) {
        super(POWER_ID, owner, amount);
        this.monsterSupplier = monsterSupplier;
        this.particleTimer = 0.0F;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        super.onAfterUseCard(card, action);
        if (amount == 0) return;
        reducePower(1);
        if (amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            addToBot(new VFXAction(new GiantEyeEffect(owner.hb.cX, owner.hb.cY, Color.PURPLE)));
            AbstractMonster monster = monsterSupplier.get();
            if (monsterSupplier != null && monster != null)
                addToBot(new MultiStasisAction(monster, AbstractDungeon.player.hand));
            addToBot(new VFXAction(new TimeWarpTurnEndEffect()));
            addToBot(new PressEndTurnButtonAction());
        }
        else {
            updateDescription();
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        this.particleTimer -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer < 0.0F) {
            this.particleTimer = 0.2F;
            AbstractDungeon.effectsQueue.add(new DivinityParticleEffect());
        }
    }
    
    /*@Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        super.atEndOfTurnPreEndTurnCards(isPlayer);
        if (amount > 0) {
            addToTop(new MultiStasisAction(monsterSupplier.get(), AbstractDungeon.player.hand));
            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }*/

    public static class MultiStasisAction extends AbstractGameAction {
        AbstractCreature owner;
        CardGroup source;
        List<AbstractCard> cards;
        
        public MultiStasisAction(AbstractCreature owner, CardGroup source) {
            this.source = source;
            this.owner = owner;
        }
        
        @Override
        public void update() {
            if (source.isEmpty()) {
                isDone = true;
                return;
            }
            if (this.duration == this.startDuration) {
                AbstractPlayer p = AbstractDungeon.player;
                cards = new ArrayList<>(source.group);
                source.clear();
                
                for (AbstractCard c : cards) {
                    p.limbo.addToBottom(c);
                    c.setAngle(0.0F);
                    c.targetDrawScale = 0.75F;
                    c.target_x = (float) Settings.WIDTH / 2.0F;
                    c.target_y = (float) Settings.HEIGHT / 2.0F;
                    c.lighten(false);
                    c.unfadeOut();
                    c.unhover();
                    c.untip();
                    c.stopGlowing();
                }
            }
            
            tickDuration();
            if (isDone) {
                this.addToTop(new ApplyPowerAction(this.owner, this.owner, new SlumberAwakeningPower(this.owner, new ArrayList<>(cards))));
                for (AbstractCard c : source.group) {
                    this.addToTop(new ShowCardAction(c));
                }
            }
        }
    }
}
