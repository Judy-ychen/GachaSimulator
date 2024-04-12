import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class GUI extends JFrame{
    public List<Item> recordItems = new ArrayList<>();
    public List<Item> itemList = new ArrayList<>();
    public static List<Item> currentItems = new ArrayList<>();

    public List<Item> futureItems = new ArrayList<>();
    public List<Item> skipList;
    public List<String> pullRecords = new ArrayList<>();

    private Item skipFive;
    private List<Item> skipFour;
    public static List<Item> fiveItems = new ArrayList<>();
    public static List<Item> fourItems = new ArrayList<>();
    public static List<Item> threeItems = new ArrayList<>();
    private Random random = new Random();
    private JDialog showItemDialog = null;

    public int tickets;
    private int numberOfSkips = 0;
    public JLabel ticketLabel;
    public JLabel currencyLabel;
    public JPanel currentPanel;
    public JPanel futurePanel;
    public JButton endButton;
    public JButton singlePullButton;
    public JButton tenPullsButton;
    public JButton skipButton;

    gachaSimulator gachaSimulator = new gachaSimulator();

    // Default Constructor: Initializes the GUI for the Gacha Simulator, including panels, buttons, and initial item setup.
    public GUI() {
        setTitle("Gacha Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Disable the layout manager
        setSize(800, 800); // Set the frame size

        // Create the "Current" panel with centered title
        currentPanel = new JPanel(null);
        currentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        currentPanel.setBounds(20, 150, 350, 300);
        JLabel currentLabel = new JLabel("Current", JLabel.CENTER);
        currentLabel.setBounds(20, 450, 350, 20);
        add(currentLabel);

        // Create the "Future" panel with centered title
        futurePanel = new JPanel(null);
        futurePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        futurePanel.setBounds(430, 150, 350, 300);
        JLabel futureLabel = new JLabel("Future", JLabel.CENTER);
        futureLabel.setBounds(430, 450, 350, 20);
        add(futureLabel);

        // Button for "1 Pull"
        singlePullButton = new JButton("1 Pull");
        // Set location and size: x, y, width, height
        singlePullButton.setBounds(260, 500, 120, 80);

        // Button for "10 Pulls"
        tenPullsButton = new JButton("10 Pulls");
        // Set location and size: x, y, width, height
        tenPullsButton.setBounds(420, 500, 120, 80);

        // Button for "end"
        endButton = new JButton("End");
        // Set location and size: x, y, width, height
        endButton.setBounds(420, 700, 100, 50);

        skipButton = new JButton("Skip");
        skipButton.setBounds(280, 700, 100, 50);

        // Add action listener to the "End" button
        endButton.addActionListener(e -> calculateAndShowPayoff());

        //Initialize Items
        initializeItems();

        // Add components directly to the frame
        add(currentPanel);
        setCurrentPanel();
        add(futurePanel);
        setFuturePanel();
        add(singlePullButton);
        add(tenPullsButton);
        add(endButton);
        add(skipButton);

        // Initialize tickets method
        currencyLabel = new JLabel("0");
        currencyLabel.setBounds(700, 20, 200, 50);
        add(currencyLabel);
        ticketCustomization();

        //activate skipButton
        skip();

        //active pull buttons
        setupButtonListeners();

        setLocationRelativeTo(null); // Center the frame on the screen
    }
    
    // Customizes the initial ticket amount based on user input or assigns a random value.
    public void ticketCustomization() {
        // Prompt the user with Yes/No options
        int response = JOptionPane.showConfirmDialog(this, "Would you like to customize the number of tickets?", "Customize Tickets", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Attempt to load the image
        URL imageUrl = getClass().getResource("Ticket.png");

        if (imageUrl != null) {
            System.out.println("Get image");
            ImageIcon ticketsIcon = new ImageIcon(imageUrl);
            ticketLabel = new JLabel(ticketsIcon);
            ticketLabel.setBounds(610, 20, 100, 50);
            add(ticketLabel);
        } else {
            ticketLabel = new JLabel("Tickets");
            ticketLabel.setBounds(20, 500, 100, 30);
            System.out.println("Failed to load ticket image.");
            System.out.println("Check if 'Ticket.png' is in the correct directory.");
        }

        // If the user chooses "Yes"
        if (response == JOptionPane.YES_OPTION) {
            String userInput = JOptionPane.showInputDialog(this, "Enter the number of tickets:", "Customize Tickets", JOptionPane.PLAIN_MESSAGE);
            try {
                // Parse the user input to an integer and assign it to tickets
                tickets = Integer.parseInt(userInput);
                System.out.println(tickets);
                updateTicketsDisplay();
            } catch (NumberFormatException e) {
                // Handle invalid input
                JOptionPane.showMessageDialog(this, "Invalid number. Assigning a random number of tickets.", "Error", JOptionPane.ERROR_MESSAGE);
                tickets = updateTickets();
            }
        } else {
            // If the user chooses "No" or closes the dialog, assign a random number of tickets
            tickets = updateTickets();
        }
    }

    // Method to update the currency label with a random value between 0 and 180
    private int updateTickets() {
        int currency = new Random().nextInt(59, 181); // Generate a random number between 60 and 180
        currencyLabel.setText("" + currency); // Update the label text
        return currency;
    }

    // Updates the display to show the current amount of tickets.
    private void updateTicketsDisplay() {
        if (currencyLabel != null) {
            currencyLabel.setText("" + tickets); // Assuming ticketLabel is the JLabel showing ticket count
            currencyLabel.revalidate(); // Ensure the label and GUI are refreshed
            currencyLabel.repaint();
        }
    }

    // Updates the "Current" panel to display selected items with their images and positions.
    private void updateCurrent() {
        // Clear the current panel from previous items
        currentPanel.removeAll();
        currentPanel.setLayout(null);

        // Lists to record selected items information
        List<Item> selectedFiveStarItems = new ArrayList<>();
        List<Item> selectedFourStarItems = new ArrayList<>();
        List<Item> itemsToShow = new ArrayList<>();

        // Randomly select items
        // Select one 5-star item
        Collections.shuffle(fiveItems);
        Item selectedFiveStar = fiveItems.get(0);
        selectedFiveStarItems.add(selectedFiveStar);

        // Select three 4-star items
        Collections.shuffle(fourItems);
        List<Item> sublistFourStar = fourItems.subList(0, Math.min(3, fourItems.size())); // Ensure we don't exceed list size
        selectedFourStarItems.addAll(sublistFourStar);

        //add the all the items in the pool into a list for recording
        itemsToShow.add(selectedFiveStar);
        itemsToShow.addAll(selectedFourStarItems);
        //itemsToShow.addAll(threeItems);

        // Display their images in the current panel
        currentPanel.setLayout(null);
        displayItem(currentPanel, selectedFiveStar, 10, 60, 160, 165);;

        // Manually specify positions for each 4-star item
        int[][] positions = new int[][] {
                {170, 5},  // x, y position for the first item
                {250, 90}, // x, y position for the second item
                {170, 165}  // x, y position for the third item
        };
        // Ensure we have enough positions defined for the number of items we want to display
        if (sublistFourStar.size() > positions.length) {
            throw new IllegalArgumentException("Not enough positions defined for the items.");
        }
        // Display the 4-star items at specified positions
        for (int i = 0; i < sublistFourStar.size(); i++) {
            Item item = sublistFourStar.get(i);
            int x = positions[i][0];
            int y = positions[i][1];
            displayItem(currentPanel, item, x, y, 100, 110); // width and height can be adjusted as needed
        }

        currentPanel.revalidate();
        currentPanel.repaint();
        currentItems = itemsToShow;
    }

    // Displays an item's image on a specified panel with given dimensions and position.
    private void displayItem(JPanel panel, Item item, int x, int y, int width, int height) {
        ImageIcon resizedIcon = Item.resizeImage(item.image, width, height);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setBounds(x, y, width, height);
        panel.add(imageLabel);
    }

    // Updates the "Future" panel to display selected items with their images and positions.
    private void updateFuture() {
        // Clear the current panel from previous items
        futurePanel.removeAll();
        futurePanel.setLayout(null);

        // Lists to record selected items information
        List<Item> selectedFiveStarItems = new ArrayList<>();
        List<Item> selectedFourStarItems = new ArrayList<>();
        List<Item> itemsToShow = new ArrayList<>();

        // Randomly select items
        // Select one 5-star item
        Collections.shuffle(fiveItems);
        Item selectedFiveStar = fiveItems.get(0);
        selectedFiveStarItems.add(selectedFiveStar);

        // Select three 4-star items
        Collections.shuffle(fourItems);
        List<Item> sublistFourStar = fourItems.subList(0, Math.min(3, fourItems.size())); // Ensure we don't exceed list size
        selectedFourStarItems.addAll(sublistFourStar);

        //add the all the items in the pool into a list for recording
        itemsToShow.add(selectedFiveStar);
        itemsToShow.addAll(selectedFourStarItems);
        //itemsToShow.addAll(threeItems);

        skipList = itemsToShow;
        skipFive = selectedFiveStar;
        skipFour = selectedFourStarItems;

        // Display their images in the future panel
        futurePanel.setLayout(null);
        displayItem(futurePanel, selectedFiveStar, 10, 60, 160, 165);;
        // Manually specify positions for each 4-star item
        int[][] positions = new int[][] {
                {170, 5},  // x, y position for the first item
                {250, 90}, // x, y position for the second item
                {170, 165}  // x, y position for the third item
        };
        // Ensure we have enough positions defined for the number of items we want to display
        if (sublistFourStar.size() > positions.length) {
            throw new IllegalArgumentException("Not enough positions defined for the items.");
        }
        // Display the 4-star items at specified positions
        for (int i = 0; i < sublistFourStar.size(); i++) {
            Item item = sublistFourStar.get(i);
            int x = positions[i][0];
            int y = positions[i][1];
            displayItem(futurePanel, item, x, y, 100, 110); // width and height can be adjusted as needed
        }
        futurePanel.revalidate();
        futurePanel.repaint();
        futureItems = itemsToShow;
    }
    
    // Initializes item lists by loading item images and data from specified directories.
    public void initializeItems(){
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        File fiveFolder = new File("src/Items/5 star"); // Path to the images folder
        File fourFolder = new File("src/Items/4 star");
        File threeFolder = new File("src/Items/3 star");
        File[] fiveListOfFiles = fiveFolder.listFiles();
        File[] fourListOfFiles = fourFolder.listFiles();
        File[] threeListOfFiles = threeFolder.listFiles();

        if (fiveListOfFiles != null) {
            for (File file : fiveListOfFiles) {
                if (file.isFile()) {
                    String name = file.getName().replaceAll("[.][^.]+$", ""); // Remove file extension
                    double probability = 0.006;
                    int officialValue = 100;
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file);
                        if (bufferedImage != null) {
                            ImageIcon image = new ImageIcon(bufferedImage);
                            fiveItems.add(new Item(name, probability, officialValue, image));
                            //System.out.println("Successfully loaded image: " + file.getPath());
                        } else {
                            //System.out.println("Failed to load (null Image): " + file.getPath());
                        }
                    } catch (IOException e) {
                        System.out.println("Exception while loading image: " + file.getPath());
                        e.printStackTrace();
                    }
                }
            }
            for (File file : fourListOfFiles) {
                if (file.isFile()) {
                    String name = file.getName().replaceAll("[.][^.]+$", ""); // Remove file extension
                    double probability = 0.051;
                    int officialValue = 50;
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file);
                        if (bufferedImage != null) {
                            ImageIcon image = new ImageIcon(bufferedImage);
                            fourItems.add(new Item(name, probability, officialValue, image));
                            //System.out.println("Successfully loaded image: " + file.getPath());
                        } else {
                            //System.out.println("Failed to load (null Image): " + file.getPath());
                        }
                    } catch (IOException e) {
                        System.out.println("Exception while loading image: " + file.getPath());
                        e.printStackTrace();
                    }
                }
            }
            for (File file : threeListOfFiles) {
                if (file.isFile()) {
                    String name = file.getName().replaceAll("[.][^.]+$", ""); // Remove file extension
                    double probability = 0.943;
                    int officialValue = 30;
                    try {
                        BufferedImage bufferedImage = ImageIO.read(file);
                        if (bufferedImage != null) {
                            ImageIcon image = new ImageIcon(bufferedImage);
                            threeItems.add(new Item(name, probability, officialValue, image));
                            //System.out.println("Successfully loaded image: " + file.getPath()); //test code
                        } else {
                            //System.out.println("Failed to load (null Image): " + file.getPath()); //test code
                        }
                    } catch (IOException e) {
                        System.out.println("Exception while loading image: " + file.getPath());
                        e.printStackTrace();
                    }
                }
            }
            itemList.addAll(fiveItems);
            itemList.addAll(fourItems);
            itemList.addAll(threeItems);
        }
    }

    // Sets up the "Current" panel with items and adds a mouse listener to display item details.
    private void setCurrentPanel() {
        updateCurrent();
        // Add mouse listener to show new window on click
        currentPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showItems(itemList, currentItems);
            }
        });
    }

    // Displays a dialog for displaying all items and inputting preference values for items in a given list.
    private void showItems(List<Item> itemsToShow, List<Item> list) {
        // Close any previously open dialog
        if (showItemDialog != null) {
            showItemDialog.dispose();
        }

        // Create and configure the window (JDialog) for item inputs
        showItemDialog = new JDialog(this, "Input Preferences", true);
        showItemDialog.setLayout(new BorderLayout()); // Use BorderLayout for better control

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        showItemDialog.setSize(600, 600);

        // Label for instruction
        JLabel instructionLabel = new JLabel("Please input your preference value for each item in range(-100 - 100)");
        contentPanel.add(instructionLabel);

        // Map to hold the JTextField reference for each item
        Map<Item, JTextField> inputFields = new LinkedHashMap<>();

        // Add items and text fields dynamically
        for (Item item : itemsToShow) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.LINE_AXIS));

            JLabel itemLabel = new JLabel("Name: " + item.name + ", Official Value: " + item.officialValue);
            if (list.contains(item)) {
                if (item.officialValue == 100) {
                    itemLabel.setForeground(Color.RED); // Selected 5-star in red
                } else if (item.officialValue == 50) {
                    itemLabel.setForeground(Color.BLUE); // Selected 4-star in red
                }
            } else {
                itemLabel.setForeground(Color.BLACK); // All other items in black
            }
            JTextField itemInput = new JTextField(5);  // Size can be adjusted

            itemPanel.add(itemLabel);
            itemPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spacer
            itemPanel.add(itemInput);

            contentPanel.add(itemPanel);

            // Add to map
            inputFields.put(item, itemInput);
        }

        // Add submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        contentPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Process inputs after closing the dialog
                for (Map.Entry<Item, JTextField> entry : inputFields.entrySet()) {
                    Item item = entry.getKey();
                    String userInput = entry.getValue().getText();
                    try {
                        item.preferValue = Double.parseDouble(userInput);  // Parse input as double and store it
                        // Print out the user's input
                        System.out.println("User input for item " + item.name + ": " + item.preferValue);
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input for item " + item.name + ": " + userInput);
                        item.preferValue = 0.0;  // Default or error value
                        // Optionally, show an error message to the user
                    }
                }
                showItemDialog.dispose();  // Close the dialog after processing
            }
        });

        JScrollPane scrollPane = new JScrollPane(contentPanel); // Wrap content in a scroll pane
        showItemDialog.add(scrollPane, BorderLayout.CENTER); // Add scrollPane to dialog

        showItemDialog.pack();
        showItemDialog.setLocationRelativeTo(null);

        // Make sure to set openItemDialog to null when it's closed
        showItemDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                showItemDialog = null;
            }
        });

        showItemDialog.setVisible(true);
    }

    // Sets up the "Future" panel with future items and adds a mouse listener for interaction.
    private void setFuturePanel() {
        updateFuture();
        // Add mouse listener to show new window on click
        futurePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showItems(itemList, futureItems);
            }
        });
    }

    // Adds functionality to the skip button, allowing users to skip current items in favor of future ones.
    private void skip() {
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear current panel items and replace them with future items

                // Close the currently open showItems dialog, if any
                if (showItemDialog != null) {
                    showItemDialog.dispose();
                    showItemDialog = null;
                }

                // Clear the current panel from previous items
                currentPanel.removeAll();
                currentPanel.setLayout(null);
                currentItems.clear();

                currentPanel.setLayout(null);
                displayItem(currentPanel, skipFive, 10, 60, 160, 165);;

                //update currentItems
                currentItems.addAll(skipList);

                // Manually specify positions for each 4-star item
                int[][] positions = new int[][] {
                        {170, 5},  // x, y position for the first item
                        {250, 90}, // x, y position for the second item
                        {170, 165}  // x, y position for the third item
                };
                // Ensure we have enough positions defined for the number of items we want to display
                if (skipFour.size() > positions.length) {
                    throw new IllegalArgumentException("Not enough positions defined for the items.");
                }
                // Display the 4-star items at specified positions
                for (int i = 0; i < skipFour.size(); i++) {
                    Item item = skipFour.get(i);
                    int x = positions[i][0];
                    int y = positions[i][1];
                    displayItem(currentPanel, item, x, y, 100, 110); // width and height can be adjusted as needed
                }
                numberOfSkips++;

                currentPanel.revalidate();
                currentPanel.repaint();
                skipList.clear();
                skipFive = null;
                skipFour.clear();
                updateFuture();
            }
        });
    }

    // Sets up listeners for the single and ten pulls buttons to perform item pulls.
    private void setupButtonListeners() {
        singlePullButton.addActionListener(e -> performPulls(1));
        tenPullsButton.addActionListener(e -> performPulls(10));
    }

    // Performs a specified number of item pulls, updating the tickets and displaying results.
    private void performPulls(int numberOfPulls) {
        if (tickets <= 0 || tickets < numberOfPulls) {
            JOptionPane.showMessageDialog(this, "Not enough tickets :-( Wanna try top-up?", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if not enough tickets
        }

        tickets -= numberOfPulls; // Deduct the number of pulls from the tickets
        updateTicketsDisplay(); // Update the ticket display

        List<Item> pulledItems = new ArrayList<>();
        int startingPullNumber = gachaSimulator.totalPulls + 1; // Calculate starting pull number for the batch
        for (int i = 0; i < numberOfPulls; i++) {
            pulledItems.add(gachaSimulator.pull());
        }
        displayPullResults(pulledItems, startingPullNumber);
        recordItems.addAll(pulledItems);
    }

    // Displays the results of item pulls in a dialog, showing images and names of pulled items.
    private void displayPullResults(List<Item> pulledItems, int startingPullNumber) {
        JDialog resultsDialog = new JDialog(this, "Pull Results", true);
        JPanel mainPanel = new JPanel();
        resultsDialog.setLayout(new BorderLayout());

        if (pulledItems.size() == 1) {
            // For a single item, center it using GridBagLayout
            mainPanel.setLayout(new GridBagLayout());
        } else {
            // For multiple items, align them horizontally using FlowLayout
            mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        int pullNumber = startingPullNumber; // Initialize with the starting pull number for the batch
        for (Item item : pulledItems) {
            ImageIcon resizedIcon = item.resizeImage(item.image, 100, 100); // Adjust size parameters as needed

            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));

            JLabel itemImage = new JLabel(resizedIcon);
            JLabel itemName = new JLabel(item.name);
            itemName.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the name label

            // Adjust color based on the star rating
            if (item.officialValue == 100) { // Assuming 100 is for 5 stars
                itemName.setForeground(Color.RED);
            } else if (item.officialValue == 50) { // Assuming 50 is for 4 stars
                itemName.setForeground(Color.BLUE);
            } else {
                itemName.setForeground(Color.BLACK);
            }

            itemPanel.add(itemImage);
            itemPanel.add(itemName);

            if (pulledItems.size() == 1) {
                mainPanel.add(itemPanel, gbc); // Center the single item
            } else {
                mainPanel.add(itemPanel); // Add to the flow layout for multiple items
            }

            pullRecords.add("Pull " + pullNumber + ": " + item.name + ", Official Value: " + item.officialValue + ", Prefer Value: " + item.preferValue);
            pullNumber++;
        }

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        JButton recordButton = new JButton("Record");

        backButton.addActionListener(e -> resultsDialog.dispose());
        recordButton.addActionListener(e -> displayRecords());

        buttonsPanel.add(backButton);
        buttonsPanel.add(recordButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setPreferredSize(new Dimension(1800, 200)); 
        resultsDialog.add(scrollPane, BorderLayout.CENTER);
        resultsDialog.add(buttonsPanel, BorderLayout.SOUTH);

        resultsDialog.pack();
        resultsDialog.setLocationRelativeTo(null); // Center on screen
        resultsDialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resultsDialog.dispose(); // Close the dialog when clicked
            }
        });
        resultsDialog.setVisible(true);
    }

    // Displays a record of all item pulls, including names, values, and user preference scores.
    private void displayRecords() {
        JDialog recordsDialog = new JDialog(this, "Pull Records", true);
        recordsDialog.setLayout(new BorderLayout());

        JTextArea recordsArea = new JTextArea();
        recordsArea.setEditable(false);
        for (String record : pullRecords) {
            recordsArea.append(record + "\n");
        }

        recordsDialog.add(new JScrollPane(recordsArea), BorderLayout.CENTER);
        recordsDialog.setSize(400, 600); // Adjust size as needed
        recordsDialog.setLocationRelativeTo(null);
        recordsDialog.setVisible(true);
    }
    
    // Calculates and shows the final payoff for the player and the company, considering points from items and actions.
    private void calculateAndShowPayoff() {
        double playerPoints = 0;
        double companyPoints = 0;
        double highestPreferValue = 0;
        int countHighestPreferValue = 0;

        // Sum official and prefer values for player, count for company
        for (Item item : recordItems) { // Assuming pullRecords holds Items or modify accordingly
            playerPoints += item.officialValue + item.preferValue;
            companyPoints += 10; // Assuming one ticket per pull
            highestPreferValue = Math.max(highestPreferValue, item.preferValue);
        }

        // Count the number of items with the highest prefer value
        for (Item item : recordItems) {
            if (item.preferValue == highestPreferValue) {
                countHighestPreferValue++;
            }
        }

        // Adjustments based on remaining tickets
        if (tickets > 0) {
            playerPoints += tickets * 2;
            companyPoints -= tickets * 2;
        } else {
            if (countHighestPreferValue == 0) {
                playerPoints -= 500;
                companyPoints += 1000;
            } else {
                playerPoints -= 100;
                companyPoints += 500;
            }
        }

        // Adjustments for items with the highest prefer value on the company side
        if (countHighestPreferValue > 0 && tickets > 0) {
            companyPoints -= 100 * countHighestPreferValue;
        }
        companyPoints -= numberOfSkips * 10;

        //show these results in a new window
        showPayoffResults(playerPoints, companyPoints);
    }

    // Displays the final payoff results in a dialog, showing points for both the player and the company.
    private void showPayoffResults(double playerPoints, double companyPoints) {
        JDialog payoffDialog = new JDialog(this, "Payoff Calculator", true);
        payoffDialog.setLayout(new GridLayout(3, 2)); // Simple grid layout for demonstration

        // Adding labels and results to the dialog
        payoffDialog.add(new JLabel("Player Points:"));
        payoffDialog.add(new JLabel(String.valueOf(playerPoints)));
        payoffDialog.add(new JLabel("Company Points:"));
        payoffDialog.add(new JLabel(String.valueOf(companyPoints)));

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> {
            System.exit(0);
        });
        payoffDialog.add(closeButton);

        payoffDialog.pack();
        payoffDialog.setLocationRelativeTo(null); // Center on screen
        payoffDialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI frame = new GUI();
            frame.setVisible(true);
        });
    }


}
