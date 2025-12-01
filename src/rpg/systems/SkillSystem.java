package rpg.systems;

import java.util.Scanner;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.skills.Skill;

public class SkillSystem {

    // public static void useSkill(Player player, Enemy enemy, Scanner scanner) {
    //     try {
    //         Skill[] skills = player.getSkills();
    //         if (skills == null || skills.length == 0) {
    //             TextEffect.typeWriter("You have no skills available!", 40);
    //             return;
    //         }

    //         // Show list of available skills
    //         TextEffect.typeWriter("\nChoose a skill to use:", 30);
    //         for (int i = 0; i < skills.length; i++) {
    //             System.out.println((i + 1) + ". " + skills[i].getName() +
    //                                " (Mana Cost: " + skills[i].getManaCost() + ")");
    //         }
    //         System.out.print("> ");
    //         String skillChoice = scanner.nextLine();

    //         int chosenIndex;
    //         try {
    //             chosenIndex = Integer.parseInt(skillChoice) - 1;
    //         } catch (NumberFormatException e) {
    //             TextEffect.typeWriter("Invalid choice! You fumble your spellbook...", 40);
    //             System.err.println("Skill input error -> " + e.getMessage());
    //             return;
    //         }

    //         if (chosenIndex < 0 || chosenIndex >= skills.length) {
    //             TextEffect.typeWriter("That skill doesn’t exist!", 40);
    //             return;
    //         }

    //         Skill chosenSkill = skills[chosenIndex];

    //         // Check mana
    //         if (player.getMana() < chosenSkill.getManaCost()) {
    //             TextEffect.typeWriter("Not enough mana to use " + chosenSkill.getName() + "!", 40);
    //             return;
    //         }

    //         try {
    //             // Consume mana and apply skill effect
    //             player.useMana(chosenSkill.getManaCost());
    //             int skillDamage = chosenSkill.getPower() + (player.getIntelligence() / 2);
    //             enemy.takeDamage(skillDamage);

    //             // Print skill text
    //             TextEffect.typeWriter(chosenSkill.useSkill(), 40);
    //             TextEffect.typeWriter("You dealt " + skillDamage + " damage using " + chosenSkill.getName() + "!", 40);
    //         } catch (Exception e) {
    //             TextEffect.typeWriter("Something went wrong while casting your skill.", 40);
    //             System.err.println("Skill execution error -> " + e.getMessage());
    //         }

    //     } catch (Exception e) {
    //         TextEffect.typeWriter("An unexpected error occurred while using a skill.", 40);
    //         System.err.println("SkillSystem error -> " + e.getMessage());
    //     } finally {
    //         // Always runs after skill attempt
    //         // Could be used for logging or state checks
    //     }
    // }

public static void useSkill(Player player, Enemy enemy, Scanner scanner) {
        try {
            Skill[] skills = player.getSkills();
            if (skills == null || skills.length == 0) {
                TextEffect.typeWriter("You have no skills available!", 40);
                return;
            }

            // ✅ UPDATED: Display skills with damage in a fancy UI box
            displaySkillMenu(skills, player);
            
            System.out.print("> ");
            String skillChoice = scanner.nextLine();

            int chosenIndex;
            try {
                chosenIndex = Integer.parseInt(skillChoice) - 1;
            } catch (NumberFormatException e) {
                TextEffect.typeWriter("Invalid choice! You fumble your spellbook...", 40);
                System.err.println("Skill input error -> " + e.getMessage());
                return;
            }

            if (chosenIndex < 0 || chosenIndex >= skills.length) {
                TextEffect.typeWriter("That skill doesn't exist!", 40);
                return;
            }

            Skill chosenSkill = skills[chosenIndex];

            // Check mana
            if (player.getMana() < chosenSkill.getManaCost()) {
                TextEffect.typeWriter("Not enough mana to use " + chosenSkill.getName() + "!", 40);
                return;
            }

            try {
                // Consume mana and apply skill effect
                player.useMana(chosenSkill.getManaCost());
                int skillDamage = chosenSkill.getPower() + (player.getIntelligence() / 2);
                enemy.takeDamage(skillDamage);

                // Print skill text
                TextEffect.typeWriter(chosenSkill.useSkill(), 40);
                TextEffect.typeWriter("You dealt " + skillDamage + " damage using " + chosenSkill.getName() + "!", 40);
            } catch (Exception e) {
                TextEffect.typeWriter("Something went wrong while casting your skill.", 40);
                System.err.println("Skill execution error -> " + e.getMessage());
            }

        } catch (Exception e) {
            TextEffect.typeWriter("An unexpected error occurred while using a skill.", 40);
            System.err.println("SkillSystem error -> " + e.getMessage());
        } finally {
            // Always runs after skill attempt
        }
    }

    // ✅ NEW: Fancy skill menu with damage display and available mana
private static void displaySkillMenu(Skill[] skills, Player player) {
    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
    System.out.println("║                   🔮  SKILL MENU  🔮                     ║");
    System.out.println("╠══════════════════════════════════════════════════════════╣");
    System.out.printf("║  💧 Available Mana: %-36s ║%n", 
        player.getMana() + "/" + player.getMaxMana());
    // System.out.println("╠══════════════════════════════════════════════════════════╣");
    System.out.println("║                                                          ║");
    
    for (int i = 0; i < skills.length; i++) {
        Skill skill = skills[i];
        int baseDamage = skill.getPower();
        int totalDamage = baseDamage + (player.getIntelligence() / 2);
        
        // Check if player has enough mana for this skill
        boolean canAfford = player.getMana() >= skill.getManaCost();
        String affordIndicator = canAfford ? "" : " ❌";
        
        String skillLine = String.format(
            "║  [%d] %-20s MC: %-3d  DMG: %-4d%s             ║",
            i + 1,
            truncate(skill.getName(), 20),
            skill.getManaCost(),
            totalDamage,
            affordIndicator
        );
        System.out.println(skillLine);
    }
    
    System.out.println("║                                                          ║");
    System.out.println("║  💡 Damage = Base + (Intelligence / 2)                   ║");
    // System.out.println("║  ❌ = Not enough mana to cast                            ║");
    System.out.println("║                                                          ║");
    System.out.println("╚══════════════════════════════════════════════════════════╝");
}

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }



}
