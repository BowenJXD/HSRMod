package me.antileaf.signature.patches.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import me.antileaf.signature.card.AbstractSignatureCard;
import me.antileaf.signature.utils.internal.SignatureHelperInternal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class ForceToShowDescriptionPatch {
	private static final Logger logger = LogManager.getLogger(ForceToShowDescriptionPatch.class.getName());

	public static class GridSelectionScreenPatches {
		@SpirePatch(clz = GridCardSelectScreen.class, method = "update", paramtypez = {})
		public static class UpdatePatch {
			@SpirePostfixPatch
			public static void Postfix(GridCardSelectScreen _inst, AbstractCard ___hoveredCard) {
				if (_inst.confirmScreenUp) {
					if (SignatureHelperInternal.shouldUseSignature(___hoveredCard))
						SignatureHelperInternal.forceToShowDescription(___hoveredCard);

					if (_inst.upgradePreviewCard != null &&
							SignatureHelperInternal.shouldUseSignature(_inst.upgradePreviewCard))
						SignatureHelperInternal.forceToShowDescription(_inst.upgradePreviewCard);
				}
			}
		}
	}

	public static class HandSelectScreenPatches {
		@SpirePatch(clz = HandCardSelectScreen.class, method = "update", paramtypez = {})
		public static class SelectHoveredCardPatch {
			@SpirePostfixPatch
			public static void Postfix(HandCardSelectScreen _inst) {
				_inst.selectedCards.group.forEach(c -> {
					if (SignatureHelperInternal.shouldUseSignature(c))
						SignatureHelperInternal.forceToShowDescription(c);
				});

				if (_inst.upgradePreviewCard != null &&
						SignatureHelperInternal.shouldUseSignature(_inst.upgradePreviewCard))
					SignatureHelperInternal.forceToShowDescription(_inst.upgradePreviewCard);
			}
		}
	}

	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class})
	public static class ShowCardBrieflyEffectPatch1 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard card) {
			if (SignatureHelperInternal.shouldUseSignature(card))
				SignatureHelperInternal.forceToShowDescription(card);
		}
	}

	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, float.class, float.class})
	public static class ShowCardBrieflyEffectPatch2 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard card, float x, float y) {
			if (SignatureHelperInternal.shouldUseSignature(card))
				SignatureHelperInternal.forceToShowDescription(card);
		}
	}

	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = "update", paramtypez = {})
	public static class ShowCardBrieflyEffectPatch3 {
		@SpirePrefixPatch
		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard ___card) {
			if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F &&
					SignatureHelperInternal.shouldUseSignature(___card))
				SignatureHelperInternal.forceToShowDescription(___card);
		}
	}

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
	public static class ShowCardAndObtainEffectPatch1 {
		@SpirePostfixPatch
		public static void Postfix(ShowCardAndObtainEffect _inst, AbstractCard card,
								   float x, float y, boolean convergeCards) {
			if (SignatureHelperInternal.shouldUseSignature(card))
				SignatureHelperInternal.forceToShowDescription(card);
		}
	}

	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update", paramtypez = {})
	public static class ShowCardAndObtainEffectPatch2 {
		@SpirePrefixPatch
		public static void Prefix(ShowCardAndObtainEffect _inst, AbstractCard ___card) {
			if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F &&
					SignatureHelperInternal.shouldUseSignature(___card))
				SignatureHelperInternal.forceToShowDescription(___card);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "renderInLibrary",
			paramtypez = {SpriteBatch.class})
	public static class RenderInLibraryPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractCard.class, "render"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"copy"})
		public static void Insert(AbstractCard _inst, SpriteBatch sb, AbstractCard copy) {
			if (SignatureHelperInternal.shouldUseSignature(_inst) && SignatureHelperInternal.shouldUseSignature(copy)) {
				if (_inst instanceof AbstractSignatureCard && copy instanceof AbstractSignatureCard) {
					AbstractSignatureCard signatureCard = (AbstractSignatureCard) _inst;
					AbstractSignatureCard signatureCopy = (AbstractSignatureCard) copy;

					signatureCopy.signatureHoveredTimer = signatureCard.signatureHoveredTimer;
					signatureCopy.forcedTimer = signatureCard.forcedTimer;
				}
				else {
					SignaturePatch.Fields.signatureHoveredTimer.set(copy,
							SignaturePatch.Fields.signatureHoveredTimer.get(_inst));
					SignaturePatch.Fields.forcedTimer.set(copy,
							SignaturePatch.Fields.forcedTimer.get(_inst));
				}
			}
		}
	}
}
