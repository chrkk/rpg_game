package rpg.skills;

public class FighterSkills {

    // 🥊 Basic Skill
    public static Skill powerPunch = new Skill("Pacman Punch", 8, 20) {
        @Override
        public String useSkill() {
            return "You deliver a bone-crushing punch!";
        }
    };

    // ⚔️ Secondary Skill
    public static Skill warCry = new Skill("Deadly Roar", 12, 25) {
        @Override
        public String useSkill() {
            return "You let out a ferocious rawr!";
        }
    };

    // 🌋 Ultimate Skill
    public static Skill earthBreaker = new Skill("Earth Shaker", 25, 50) {
        @Override
        public String useSkill() {
            return "You slam the ground, creating a shockwave that devastates enemies!";
        }
    };
}
