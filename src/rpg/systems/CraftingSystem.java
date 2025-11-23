public static int craftWeapon(Player player, int crystals, GameState state, String target) {
    if (state.unlockedRecipes.contains(target) && state.recipeItems.getOrDefault(target, false)) {
        switch (target) {
            case "Crystal Sword":
                if (player.getWeapon() != null && player.getWeapon().getName().equals("Pencil Blade")
                        && crystals >= 5) {
                    crystals -= 5;
                    player.equipWeapon(new Weapon("Crystal Sword", 20, 35, 0.10, 2.0));
                    state.stage2WeaponCrafted = true;
                    TextEffect.typeWriter("⚔️ You forged a Crystal Sword! Its edge gleams with power.", 60);
                } else {
                    TextEffect.typeWriter("You need a Pencil Blade and 5 crystals.", 60);
                }
                break;

            case "Flame Axe":
                if (player.getWeapon() != null && player.getWeapon().getName().equals("Crystal Sword")
                        && crystals >= 8) {
                    crystals -= 8;
                    player.equipWeapon(new Weapon("Flame Axe", 25, 40, 0.15, 1.8));
                    state.stage3WeaponCrafted = true;  // ← TRACK THIS
                    TextEffect.typeWriter("🔥 You forged a Flame Axe! It radiates intense heat.", 60);
                } else {
                    TextEffect.typeWriter("You need a Crystal Sword and 8 crystals.", 60);
                }
                break;

            case "Shadow Bow":
                if (player.getWeapon() != null && player.getWeapon().getName().equals("Flame Axe")
                        && crystals >= 10) {
                    crystals -= 10;
                    player.equipWeapon(new Weapon("Shadow Bow", 15, 30, 0.20, 2.5));
                    state.stage4WeaponCrafted = true;  // ← ADD THIS
                    TextEffect.typeWriter("🌑 You forged a Shadow Bow! Shadows bend to your will.", 60);
                } else {
                    TextEffect.typeWriter("You need a Flame Axe and 10 crystals.", 60);
                }
                break;

            default:
                TextEffect.typeWriter("This recipe isn't implemented yet.", 60);
        }
    } else if (state.unlockedRecipes.contains(target) && !state.recipeItems.getOrDefault(target, false)) {
        TextEffect.typeWriter("You know the blueprint, but you haven't found the recipe item yet.", 60);
    } else {
        TextEffect.typeWriter("You don't have the requirements to craft this weapon.", 60);
    }
    return crystals;
}