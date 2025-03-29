package me.antileaf.signature.utils.internal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import me.antileaf.signature.card.AbstractSignatureCard;
import me.antileaf.signature.interfaces.SignatureSubscriber;
import me.antileaf.signature.patches.card.SignaturePatch;
import me.antileaf.signature.patches.card.UnlockConditionPatch;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;

public abstract class SignatureHelperInternal {
	private static final Logger logger = LogManager.getLogger(SignatureHelperInternal.class);

	public static final float FADE_DURATION = 0.3F;
	public static final float FORCED_FADE_DURATION = 0.5F;

	private static final Map<String, SignatureHelper.Info> registered = new HashMap<>();

	private static final Map<String, Boolean> unlocked = new HashMap<>();
	private static final Map<String, Boolean> enabled = new HashMap<>();
	private static final Map<String, Texture> cache = new HashMap<>();

	private static final Set<String> noDebugging = new HashSet<>();
	private static final Set<String> noDebuggingPrefixes = new HashSet<>();

	private static final Map<String, String> parent = new HashMap<>();
	private static final Map<String, Set<String>> children = new HashMap<>();

	private static final Set<SignatureSubscriber> subscribers = new HashSet<>();

	private static final Map<String, Integer> libraryTypeNotice = new HashMap<>();

	public static TextureAtlas.AtlasRegion load(String path) {
		Texture t;

		if (cache.containsKey(path))
			t = cache.get(path);
		else {
			if (Gdx.files.internal(path).exists())
				t = ImageMaster.loadImage(path);
			else {
				t = null;
				logger.error("Image file {} not found. " +
						"This is probably caused by the mod author," +
						"go yell at them to fix it.",
						path);
			}

			cache.put(path, t);
		}

		return t == null ? null :
				new TextureAtlas.AtlasRegion(t, 0, 0, t.getWidth(), t.getHeight());
	}

	public static void setParent(String id, String parentID) {
		parent.put(id, parentID);

		if (!children.containsKey(parentID))
			children.put(parentID, new HashSet<>());

		children.get(parentID).add(id);
	}

	public static void register(String id, SignatureHelper.Info info) {
		AbstractCard card = CardLibrary.getCard(id);

		if (card == null) {
			logger.error("Card with ID {} not found", id);
			return;
		}

		if (card instanceof AbstractSignatureCard) {
			logger.warn("Please do not register a card that already extends AbstractSignatureCard");
			return;
		}

		if (!Gdx.files.internal(info.img.apply(card)).exists()) {
			logger.error("Image file {} not found", info.img.apply(card));
			return;
		}

		registered.put(id, info);

		if (info.parentID != null)
			setParent(id, info.parentID);
	}

	public static void unlock(String id, boolean unlock) {
		boolean alreadyUnlocked = isUnlocked(id);

		ConfigHelper.setSignatureUnlocked(id, unlock);
		unlocked.put(id, unlock);

		AbstractCard card = CardLibrary.getCard(id);
		if (card != null && !hideSCVPanel(card) && alreadyUnlocked != unlock)
			setSignatureNotice(card, unlock);

		publishOnSignatureUnlocked(id, unlock);

		if (children.containsKey(id))
			children.get(id).forEach(child -> unlock(child, unlock));
	}

	public static boolean isUnlocked(String id) {
		if (ConfigHelper.enableDebugging() && !noDebugging.contains(id) &&
				noDebuggingPrefixes.stream().noneMatch(id::startsWith))
			return true;

		if (parent.containsKey(id) && !isUnlocked(parent.get(id)))
			return true;

		if (!unlocked.containsKey(id))
			unlocked.put(id, ConfigHelper.isSignatureUnlocked(id));

		return unlocked.get(id);
	}

	public static boolean isRegistered(String id) {
		return registered.containsKey(id);
	}

	public static SignatureHelper.Info getInfo(String id) {
		return registered.get(id);
	}

	public static SignatureHelper.Style getStyle(AbstractCard card) {
		return card instanceof AbstractSignatureCard ?
				((AbstractSignatureCard) card).style :
				getInfo(card.cardID).style;
	}

	public static boolean hasSignature(AbstractCard card) {
		if (card instanceof AbstractSignatureCard)
			return ((AbstractSignatureCard) card).hasSignature;
		else
			return isRegistered(card.cardID);
	}

	public static void setDebugging(String id, boolean available) {
		if (available)
			noDebugging.add(id);
		else
			noDebugging.remove(id);

		if (children.containsKey(id))
			children.get(id).forEach(child -> setDebugging(child, available));
	}

	public static void noDebugging(String id) {
		noDebugging.add(id);

		if (children.containsKey(id))
			children.get(id).forEach(SignatureHelperInternal::noDebugging);
	}

	public static void noDebuggingPrefix(String prefix) {
		noDebuggingPrefixes.add(prefix);
	}

	public static String getUnlockCondition(String id) {
		return UnlockConditionPatch.Fields.unlockCondition.get(CardCrawlGame.languagePack.getCardStrings(id));
	}

	public static void addUnlockConditions(String jsonFile) {
		Map<String, String> conditions = (new Gson()).fromJson(
				Gdx.files.internal(jsonFile).readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, String>>() {}).getType());

		for (Map.Entry<String, String> entry : conditions.entrySet())
			UnlockConditionPatch.Fields.unlockCondition.set(
					CardCrawlGame.languagePack.getCardStrings(entry.getKey()),
					entry.getValue());
	}

	public static void enable(String id, boolean enable) {
		ConfigHelper.setSignatureEnabled(id, enable);
		enabled.put(id, enable);

		publishOnSignatureEnabled(id, enable);

		if (children.containsKey(id))
			children.get(id).forEach(child -> enable(child, enable));
	}

	public static boolean isEnabled(String id) {
		if (!enabled.containsKey(id))
			enabled.put(id, ConfigHelper.isSignatureEnabled(id));

		if (!isUnlocked(id) && !ConfigHelper.enableDebugging())
			enable(id, false);

		return enabled.get(id);
	}

	public static boolean shouldUseSignature(AbstractCard card) {
		if (!(hasSignature(card) && isUnlocked(card.cardID) && isEnabled(card.cardID)))
			return false;

		if (card instanceof AbstractSignatureCard)
			return ((AbstractSignatureCard) card).shouldUseSignature();
		else
			return getInfo(card.cardID).shouldUseSignature.test(card);
	}

	public static boolean usePatch(AbstractCard card) {
		return SignatureHelperInternal.isRegistered(card.cardID) &&
				SignatureHelperInternal.shouldUseSignature(card);
	}

	public static void forceToShowDescription(AbstractCard card) {
		if (card instanceof AbstractSignatureCard)
			((AbstractSignatureCard) card).forceToShowDescription();
		else
			SignaturePatch.forceToShowDescription(card);
	}

	public static boolean hideSCVPanel(AbstractCard card) {
		if (card instanceof AbstractSignatureCard) {
			if (((AbstractSignatureCard) card).parentID != null)
				return true;
		}
		else if (getInfo(card.cardID).parentID != null)
			return true;

		if (card instanceof AbstractSignatureCard)
			return ((AbstractSignatureCard) card).hideSCVPanel;
		else
			return getInfo(card.cardID).hideSCVPanel;
	}

	public static boolean dontAvoidSCVPanel(AbstractCard card) {
		if (card instanceof AbstractSignatureCard)
			return ((AbstractSignatureCard) card).dontAvoidSCVPanel;
		else
			return getInfo(card.cardID).dontAvoidSCVPanel;
	}

	public static boolean signatureNotice(AbstractCard card) {
		if (!hasSignature(card) || !isUnlocked(card.cardID) || hideSCVPanel(card))
			return false;

		return ConfigHelper.signatureNotice(card.cardID);
	}

	public static void setSignatureNotice(AbstractCard card, boolean notice) {
		if (!hasSignature(card) || !isUnlocked(card.cardID) || hideSCVPanel(card) ||
				notice == signatureNotice(card))
			return;

		ConfigHelper.setSignatureNotice(card.cardID, notice);

		updateLibraryTypeNotice(card.color, notice);
	}

	public static void updateLibraryTypeNotice(AbstractCard.CardColor color, boolean notice) {
		int count = libraryTypeNotice.getOrDefault(color.name(), 0);
		count += notice ? 1 : -1;
		if (count < 0)
			count = 0;

		libraryTypeNotice.put(color.name(), count);
	}

	public static int getLibraryTypeNotice(AbstractCard.CardColor color) {
		return libraryTypeNotice.getOrDefault(color.name(), 0);
	}

	public static boolean hasAnyNotice() {
		return libraryTypeNotice.values().stream().anyMatch(count -> count > 0);
	}

	public static void subscribe(SignatureSubscriber subscriber) {
		subscribers.add(subscriber);
	}

	public static void unsubscribe(SignatureSubscriber subscriber) {
		subscribers.remove(subscriber);
	}

	private static void publishOnSignatureUnlocked(String id, boolean unlock) {
		logger.info("Publishing onSignatureUnlocked: {} {}", id, unlock);

		subscribers.forEach(subscriber ->
				subscriber.receiveOnSignatureUnlocked(id, unlock));
	}

	private static void publishOnSignatureEnabled(String id, boolean enable) {
		logger.info("Publishing onSignatureEnabled: {} {}", id, enable);

		subscribers.forEach(subscriber ->
				subscriber.receiveOnSignatureEnabled(id, enable));
	}

	public static void initLibraryTypeNotice() {
		libraryTypeNotice.clear();

		for (AbstractCard card : CardLibrary.getAllCards())
			if (hasSignature(card) && !hideSCVPanel(card) && ConfigHelper.signatureNotice(card.cardID)) {
				int count = libraryTypeNotice.getOrDefault(card.color.name(), 0);
				libraryTypeNotice.put(card.color.name(), count + 1);
			}

		for (Map.Entry<String, Integer> entry : libraryTypeNotice.entrySet())
			logger.info("Library type notice: {} {}", entry.getKey(), entry.getValue());
	}

	public static void resetNotices() {
		for (AbstractCard card : CardLibrary.getAllCards())
			if (hasSignature(card) && isUnlocked(card.cardID) && !hideSCVPanel(card))
				setSignatureNotice(card, true);

		initLibraryTypeNotice();
	}
}
