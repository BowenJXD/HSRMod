package hsrmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hsrmod.actions.ElementalDamageAction;
import hsrmod.actions.ElementalDamageAllAction;
import hsrmod.actions.FollowUpAction;
import hsrmod.cards.BaseCard;
import hsrmod.effects.BackFlipEffect;
import hsrmod.utils.ModHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import static hsrmod.modcore.CustomEnums.FOLLOW_UP;

public class Herta1 extends BaseCard {
    public static final String ID = Herta1.class.getSimpleName();
    List<AbstractCreature> moreThanHalfMonsters;
    boolean canRepeat = false;

    public Herta1() {
        super(ID);
        moreThanHalfMonsters = new ArrayList<>();
        this.tags.add(FOLLOW_UP);
        this.isMultiDamage = true;
    }

    @Override
    public void onUse(AbstractPlayer p, AbstractMonster m) {
        updateMoreThanHalfMonsters();
        execute();
    }

    void execute() {
        canRepeat = true;

        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) return;
        addToBot(new VFXAction(new BackFlipEffect(AbstractDungeon.player, false)));
        addToBot(
                new ElementalDamageAllAction(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL).setCallback(
                        new Consumer<ElementalDamageAction.CallbackInfo>() {
                            @Override
                            public void accept(ElementalDamageAction.CallbackInfo ci) {
                                if (moreThanHalfMonsters.contains(ci.target) && ci.target.currentHealth <= ci.target.maxHealth / 2) {
                                    moreThanHalfMonsters.remove(ci.target);
                                    if (canRepeat) {
                                        canRepeat = false;
                                        ModHelper.addToTopAbstract(new ModHelper.Lambda() {
                                            @Override
                                            public void run() {
                                                Herta1.this.execute();
                                            }
                                        });
                                    }
                                }
                            }
                        })
        );
    }

    @Override
    public void onEnterHand() {
        updateMoreThanHalfMonsters();
    }

    @Override
    public void triggerAtStartOfTurn() {
        updateMoreThanHalfMonsters();
    }
    
    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        if (!AbstractDungeon.player.hand.contains(this)) return;
        if (!followedUp) {
            ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                @Override
                public void run() {
                    ModHelper.addToBotAbstract(new ModHelper.Lambda() {
                        @Override
                        public void run() {
                            if (Herta1.this.updateMoreThanHalfMonsters()) {
                                followedUp = true;
                                Herta1.this.addToBot(new FollowUpAction(Herta1.this));
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * @return true if changed
     */
    public boolean updateMoreThanHalfMonsters() {
        boolean result = false;
        List<AbstractCreature> temp = new ArrayList<>();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster.currentHealth > monster.maxHealth / 2) {
                temp.add(monster);
            }
        }
        if (!new HashSet<>(temp).containsAll(moreThanHalfMonsters)) result = true;
        moreThanHalfMonsters = temp;
        return result;
    }
}
