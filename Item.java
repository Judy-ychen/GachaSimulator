import javax.swing.ImageIcon;
import java.awt.*;
import java.util.List;

public class Item {
    public double probability;
    public String name;
    public double officialValue;
    public double preferValue;
    public ImageIcon image;

    // Constructs an Item with specified name, probability, official value, and image.
    public Item(String name, double probability, int officialValue, ImageIcon image){
        this.name = name;
        this.probability = probability;
        this.officialValue = officialValue;
        this.image = image;
    }
    
    // Static method to resize an ImageIcon to the specified width and height.
    static ImageIcon resizeImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    // Method to check if this item is selected
    public boolean isSelected(List<Item> selectedItems) {
        for (Item selectedItem : selectedItems) {
            if (this.equals(selectedItem)) {
                return true;
            }
        }
        return false;
    }
}
