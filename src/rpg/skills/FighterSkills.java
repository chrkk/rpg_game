package rpg.skills;

public class FighterSkills {

    // 🥊 Basic Skill
    public static Skill powerPunch = new Skill("Power Punch", 8, 20) {
        @Override
        public String useSkill() {
            return "You deliver a bone-crushing punch!";
        }
    };

    // ⚔️ Secondary Skill
    public static Skill warCry = new Skill("War Cry", 12, 0) {
        @Override
        public String useSkill() {
            return "You let out a ferocious war cry, boosting your morale!";
        }
    };

    // 🌋 Ultimate Skill
    public static Skill earthBreaker = new Skill("Earth Breaker", 25, 50) {
        @Override
        public String useSkill() {
            return "You slam the ground, creating a shockwave that devastates enemies!";
        }
    };
}
