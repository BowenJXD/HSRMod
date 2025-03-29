package me.antileaf.signature.utils.internal;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class MiscHelper {
	public static String getLangShort() {
		if (Settings.language == Settings.GameLanguage.ZHS ||
				Settings.language == Settings.GameLanguage.ZHT)
			return "zhs";
		else if (Settings.language == Settings.GameLanguage.JPN)
			return "jpn";
		else
			return "eng";
	}

	public static boolean isInBattle() {
		return CardCrawlGame.dungeon != null &&
				AbstractDungeon.isPlayerInDungeon() &&
				AbstractDungeon.currMapNode != null &&
				AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
	}
}
