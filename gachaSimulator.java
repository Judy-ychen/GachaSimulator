import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class gachaSimulator {

    public int totalPulls = 0;
    private boolean getSelectedFive = false;
    private int pullsSinceLastFourStar = 0; //Tracks the pull number of the last 4-star item obtained
    private int lastFiveStarPull = 0; // Tracks the pull number of the last 5-star item obtained

    // Simulates a single item pull from the gacha system, adjusting probabilities for rarity based on game mechanics.
    public Item pull() {
        totalPulls++;
        pullsSinceLastFourStar++;
        double prob = Math.random();
        double fiveStarProb = calculateFiveStarProb();
        double fourStarProb = calculateFourStarProb();

        System.out.println("Current 5-star drop rate: " + (fiveStarProb * 100) + "%");

        Item result;
        if (prob < fiveStarProb) {
            pullsSinceLastFourStar = 0; // Reset the counter for 4-star pity system
            // Check if the pull is at the 90th or it's a guarantee promotion pull due to the last one not being promotion
            boolean guaranteePromotion = totalPulls % 90 == 0 || !getSelectedFive;
            result = selectItem(GUI.fiveItems, guaranteePromotion);
            // Update lastFiveStarWasPromotion based on whether the result is a promotion item
            lastFiveStarPull = totalPulls;
            // If this was a guaranteed promotion pull, reset the flag based on whether the item was a promotion item
            if (guaranteePromotion) {
                getSelectedFive = result.isSelected(GUI.currentItems); // Assume this checks if the item is a promotion
            }
            return result;
        } else if (prob < fourStarProb + fiveStarProb) {
            boolean isPityPull = pullsSinceLastFourStar >= 10;
            pullsSinceLastFourStar = 0; // Reset 4-star counter
            // If it's a pity pull, there's a 50% chance the 4-star will be a promotion item
            return selectItem(GUI.fourItems, isPityPull);
        } else {
            return selectItem(GUI.threeItems, false);
        }
    }

    // Calculates the probability of pulling a 5-star item, incorporating a pity system that increases the probability closer to the 90th pull.
    private double calculateFiveStarProb() {
        int cyclePull = (totalPulls - lastFiveStarPull) % 90;

        // The 90th pull should return a guaranteed 5-star probability
        if (cyclePull == 0) { // This handles the case immediately after a 5-star pull
            return 0.006; // Base probability of 0.6%
        } else if (cyclePull >= 74) {
            // Adjusted probability as the player approaches the 90th pull without a 5-star
            return 0.006 + (cyclePull - 73) * 0.06;
        } else {
            return 0.006; // Base probability for other pulls
        }
    }
    
    // Selects an item from a given list, potentially guaranteeing a promotion item based on the passed flag.
    private Item selectItem(List<Item> items, boolean guaranteeSelected) {
        List<Item> promotionItems = items.stream()
                .filter(item -> item.isSelected(GUI.currentItems))
                .collect(Collectors.toList());
        List<Item> nonPromotionItems = items.stream()
                .filter(item -> !item.isSelected(GUI.currentItems))
                .collect(Collectors.toList());

        if (guaranteeSelected) {
            if (Math.random() < 0.5 && !promotionItems.isEmpty()) {
                // Explicitly choose from promotion items
                return promotionItems.get((int) (Math.random() * promotionItems.size()));
            } else if (!nonPromotionItems.isEmpty()) {
                // Explicitly choose from non-promotion items
                return nonPromotionItems.get((int) (Math.random() * nonPromotionItems.size()));
            }
        }
        // Default to random selection if not a pity pull or if specific lists are empty
        return items.get((int) (Math.random() * items.size()));
    }

    // Calculates the probability of pulling a 4-star item, guaranteeing one at least every 10 pulls.
    private double calculateFourStarProb() {
        if (pullsSinceLastFourStar >= 9) {
            return 1.0; // Guarantees a 4-star item on the 10th pull
        }
        return 0.051; // Base probability of 5.1%
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI frame = new GUI();
            frame.setVisible(true);
        });
    }
}
