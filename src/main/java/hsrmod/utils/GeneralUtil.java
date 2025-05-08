package hsrmod.utils;

import hsrmod.Hsrmod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class GeneralUtil {
    public static <T> T getWeightedRandomElement(List<T> items, List<Integer> weights) {
        if (items == null || weights == null || items.size() != weights.size() || items.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: items and weights must be non-null and have the same size.");
        }

        // Calculate the total sum of the weights
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        // Generate a random number between 0 and totalWeight
        int randomValue = AbstractDungeon.cardRandomRng.random(totalWeight);

        // Iterate over the weights to find the corresponding item
        for (int i = 0; i < items.size(); i++) {
            randomValue -= weights.get(i);
            if (randomValue < 0) {
                return items.get(i);
            }
        }

        // Fallback (should never happen if input is valid)
        return null;
    }

    public static <T extends Enum<T>> T getRandomEnumValue(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        int randomIndex = new Random().random(values.length - 1);
        return values[randomIndex];
    }

    public static <T> T getRandomElement(List<T> list, Random rand) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(rand.random(list.size() - 1));
    }

    public static <T> T getRandomElement(List<T> list, Random random, Predicate<T> predicate) {
        List<T> filtered = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                filtered.add(element);
            }
        }
        if (filtered.isEmpty()) {
            return null;
        }
        return getRandomElement(filtered, random);
    }

    public static <T> List<T> getRandomElements(List<T> list, Random random, int count) {
        count = Math.min(count, list.size());

        List<T> shuffledList = new ArrayList<>(list);
        Collections.shuffle(shuffledList, random.random);  // Randomly shuffle the list
        return shuffledList.subList(0, count);  // Return the first x elements
    }
    
    public static <T> List<T> unpackSaveData(String string, Function<? super String, ? extends T> mapper){
        // Remove square brackets and split the string by commas
        String trimmed = string.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1); // Remove the brackets
        }
        else return new ArrayList<>();

        // Handle empty or null input
        if (trimmed.isEmpty()) return new ArrayList<>();

        // Split the string by commas and map each element using the mapper
        String[] elements = trimmed.split(", ");
        List<T> list = new ArrayList<>();
        for (String element : elements) {
            T t = mapper.apply(element);
            list.add(t);
        }
        return list;
    }
    
    public static String tryFormat(String text, Object... args) {
        try {
            return String.format(text, args);
        } catch (Exception e) {
            Hsrmod.logger.warn("Failed to format string: {}", text);
            return text;
        }
    }
}
