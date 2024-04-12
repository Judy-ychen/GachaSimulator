# GachaSimulator
Gacha Simulator is a Java-based application designed to emulate the gacha system mechanics commonly found in video games. In these systems, players can "pull" to receive random virtual items of varying rarity. This simulator aims to provide an interactive and educational experience, allowing users to understand and engage with the concept of gacha without any real-world stakes. 

Also, Gacha Simulator calculates the payoffs of the user and the game company through the user's activities and strategies while performing pulls.

## Installation
### Requirements:

Java Development Kit (JDK) 8 or later.

### Steps:

1. Ensure Java is installed on your system.

2. Download the gachaSimulator.java, GUI.java, Item.java, Ticket.png and Items folder.

3. Change the path to directory on line 120 and 291-293 of GUI.java, make sure the path to the image folder is correct according to it's position in your laptop.

4. Compile the Java files(The main class is in gachaSimulator.java)


## Usage

Launch the Gacha Simulator to access the GUI. 

First, the GUI will ask users if they want to customize the number of tickets for pulls. If they choose not to, users will receive a random number of tickets, ranging from 0 to 180.

Use the main interface to perform gacha pulls, the interface will display two panels, one for the "Current Panel" and one for the "Future Panel." The current pool is the set of items that the game company is currently offering for gacha, while the future pool represents the next set of items that will be available(In this simulator, the items the user can get are the same, only the promoted items are different in different panels). 

Clicking on a panel allows the user to view all the items available for gacha in that panel, with an official value of 100 for 5-star items, 50 for 4-star items, and 30 for 3-star items. Items marked in red or blue are the ones being promoted in the current panel (there's a 50% chance that a drawn 5-star or 4-star item will be one of the promoted items). Additionally, the window will prompt users to input a prefer value for each item (ranging from -100 to 100), with the default value set to 0 if no input is given. After clicking the submit button, the user's inputs are stored.

Then, returning to the main interface, users can decide whether to pull from the current panel or the future panel. If they are not satisfied with the items being promoted in the current panel, they can choose to skip it and pull from the future panel instead. This skipping action can continue until the item the user wants starts being promoted.

On the main interface, users can choose to perform a single pull or ten pulls from the current panel, with each result displayed in a new window. 10 pulls guarantee at least one 4-star item (marked in blue), and 90 pulls guarantee at least one 5-star item (marked in red), as detailed in the Probability Announcement. In the results display interface, users can choose to return to the main interface to continue pulling or click the "record" button to view their pulling history.

When users run out of tickets, the GUI will notify them that they no longer have enough tickets and will inquire if they wish to try the Top-up system (a conceptual system for purchasing game currency with real money that has not yet been implemented). At this point, users can choose to end the gacha by pressing the End button, after which the GUI will calculate and present the returns for both the user and the game company based on the user's input and strategy.

## Probability Announcement
### 5-Star Items
The base probability for 5-star items is 0.600%. The comprehensive probability, including the pity system, is 1.600%. A 5-star item is guaranteed within a maximum of 90 attempts through the pity system.

If a player fails to obtain a 5-star item within 73 attempts, the probability increases by 6% with each subsequent pull until it reaches 100% at the 90th attempt without obtaining a 5-star item. Upon obtaining a 5-star item, the probability resets to 0.6%.

When a 5-star item is obtained, there is a 50% chance it will be the featured 5-star UP item for the current period. If the 5-star character obtained is not the featured UP character, the next 5-star character obtained is guaranteed to be the featured UP character.

### 4-Star Items
The base probability for 4-star items in the current panel is 5.100%. 

A 4-star or higher item is guaranteed within a maximum of 10 pulls through the pity system, with a 99.400% chance of obtaining a 4-star item and a 0.600% chance of obtaining a 5-star item through this system.

When obtaining a 4-star item, there is a 50% chance it will be one of the featured 4-star UP items for the current period.

### 3-Star Items
The base probability for 3-star items in the current panel is 94.300%.


## Future Works
Future enhancements may include:

1. Adding more items and rarity types.
2. Implementing a user account system to track multiple simulator histories and predict real Gacha results.
3. Calculate payoffs in real-time and display them in the "record" interface.

## Reference
This project take a reference from the game Genshin Impact's Wish gacha system and item probabilities.

## Support

For support, inquiries, or suggestions, please email developers at ychen19@conncoll.edu or alopezpat@conncoll.edu.


## Project Status
The project is in active development, with plans for future features and improvements. Community feedback and contributions are highly encouraged to help shape the evolution of this simulator.

