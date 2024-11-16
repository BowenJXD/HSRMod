package hsrmod.modcore;

import basemod.devcommands.relic.Relic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import hsrmod.patches.PathSelectScreen;
import hsrmod.relics.starter.WaxRelic;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
