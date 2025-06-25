package view.Menu;

import java.util.Scanner;
import java.util.TreeMap;

public class Menu {
    private Scanner input;
    TreeMap<Integer, Operation> menuOptions;

    public Menu(TreeMap<Integer, Operation> menuOptions, Scanner input){
        this.menuOptions = menuOptions;
        this.input = input;
    }

    public void runMenu(){
        int choice = -1;

        do {
            for (var option : menuOptions.entrySet()) {
                System.out.println(String.format("%d - %s", option.getKey(), option.getValue().getText()));;
            }
            try {
                System.out.println("Enter a option: ");
                choice = Integer.parseInt(input.nextLine());
                Operation operation = menuOptions.get(choice);

                if (operation != null)
                    operation.run();
                else
                    System.out.println("Command not found!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, insert a number.");
            }

        } while (choice != menuOptions.lastKey());
    }
}
