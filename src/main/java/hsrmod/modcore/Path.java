package hsrmod.modcore;

import com.megacrit.cardcrawl.cards.AbstractCard;

public enum Path {
    ELATION,
    DESTRUCTION,
    NIHILITY,
    PRESERVATION,
    PROPAGATION,
    THE_HUNT,
    ERUDITION,
    ABUNDANCE,
    REMEMBRANCE,
    TRAILBLAZE;
    
    public static AbstractCard.CardTags toTag(Path path) {
        return AbstractCard.CardTags.valueOf(path.name());
    }
    
    public static Path fromTag(AbstractCard.CardTags tag) {
        return Path.valueOf(tag.name());
    }
}
