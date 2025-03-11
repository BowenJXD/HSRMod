package hsrmod.minion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import java.util.ArrayList;

public class MinionGroup {
    private final ArrayList<AbstractPlayerMinion> minions = new ArrayList();

    public MinionGroup() {
    }

    public void addMinion(AbstractPlayerMinion minion) {
        this.minions.add(minion);
    }

    public void showIntent() {
        if (!this.minions.isEmpty()) {
            for(AbstractPlayerMinion minion : this.minions) {
                minion.createIntent();
            }
        }

    }

    public void init() {
        if (!this.minions.isEmpty()) {
            for(AbstractPlayerMinion minion : this.minions) {
                minion.init();
            }
        }

    }

    public void usePreBattleAction() {
    }

    public void updateAnimations() {
    }

    public void applyPreTurnLogic() {
        if (!this.minions.isEmpty()) {
            for(AbstractPlayerMinion minion : this.minions) {
                if (!minion.isDeadOrEscaped()) {
                    if (!minion.hasPower("Barricade")) {
                        minion.loseBlock();
                    }

                    minion.applyStartOfTurnPowers();
                }
            }
        }

    }

    public void update() {
        if (!this.minions.isEmpty()) {
            for(AbstractPlayerMinion minion : this.minions) {
                minion.update();
            }
        }

    }

    public void render(SpriteBatch sb) {
        if (!this.minions.isEmpty()) {
            for(AbstractPlayerMinion minion : this.minions) {
                if (minion.hb.hovered && !AbstractDungeon.player.isDead && (!AbstractDungeon.isScreenUp || PeekButton.isPeeking)) {
                    minion.renderTip(sb);
                }

                minion.render(sb);
            }
        }

    }

    public void applyEndOfTurnPowers() {
        if (!this.minions.isEmpty()) {
            for(final AbstractPlayerMinion minion : this.minions) {
                if (!minion.isDying && !minion.isEscaping) {
                    for(AbstractPower power : minion.powers) {
                        power.atEndOfRound();
                    }

                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        public void update() {
                            if (minion.targetMonster == null) {
                                minion.refreshTargetMonster();
                            } else if (minion.targetMonster.isDeadOrEscaped()) {
                                minion.refreshTargetMonster();
                            }

                            this.isDone = true;
                        }
                    });
                }
            }
        }

    }

    public void applyEndOfTurnTriggers() {
        if (!this.minions.isEmpty()) {
            for(final AbstractPlayerMinion minion : this.minions) {
                if (!minion.isDying && !minion.isEscaping) {
                    AbstractDungeon.actionManager.addToBottom(new MinionIntentFlashAction(minion));
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0F));
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        public void update() {
                            minion.takeTurn();
                            minion.applyEndOfTurnTriggers();
                            this.isDone = true;
                        }
                    });
                }
            }
        }

    }

    public void renderReticle(SpriteBatch sb) {
        if (!this.minions.isEmpty()) {
            for(AbstractPlayerMinion minion : this.minions) {
                if (!minion.isDeadOrEscaped()) {
                    minion.renderReticle(sb);
                }
            }
        }

    }

    public static ArrayList<AbstractPlayerMinion> getMinions() {
        return (MinionPatch.AddFields.playerMinions.get(AbstractDungeon.player)).minions;
    }

    public void onVictory() {
        for(AbstractPlayerMinion minion : this.minions) {
            for(AbstractPower power : minion.powers) {
                power.onVictory();
            }

            minion.dispose();
        }

        this.minions.clear();
    }

    public static boolean areMinionsBasicallyDead() {
        if (getMinions().isEmpty()) {
            return true;
        } else {
            for(AbstractPlayerMinion m : getMinions()) {
                if (!m.halfDead && !m.isDying && !m.isEscaping) {
                    return false;
                }
            }

            return true;
        }
    }

    public static AbstractPlayerMinion getCurrentMinion() {
        for(AbstractPlayerMinion m : getMinions()) {
            if (!m.halfDead && !m.isDying && !m.isEscaping) {
                return m;
            }
        }

        return null;
    }
}

