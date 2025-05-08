package com.megacrit.cardcrawl.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HandCheckAction extends AbstractGameAction {
    private AbstractPlayer player;

    public HandCheckAction() {
        this.player = AbstractDungeon.player;
    }

    public void update() {
        this.player.hand.applyPowers();
        this.player.hand.glowCheck();
        AbstractDungeon.player.hand.refreshHandLayout();
        this.isDone = true;
    }
}

