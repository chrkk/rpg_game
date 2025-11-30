package rpg.skills;

public class ArchmageSkills {

    // 🔥 Basic Skill
    public static Skill fireBolt = new Skill("Fire Bolt", 10, 30) {
        @Override
        public String useSkill() {
            return "You conjure a blazing bolt of fire!";
        }
    };

    // 🧿 Secondary Skill
    public static Skill arcaneShield = new Skill("Arcane Pulse", 12, 0) {
        @Override
        public String useSkill() {
            return "You summon an ethereal arcane!";
        }
    };

    // ☄️ Ultimate Skill
    public static Skill meteorStorm = new Skill("Meteor Storm", 35, 70) {
        @Override
        public String useSkill() {
            return "You summon a cataclysmic storm of meteors!";
        }
    };
}
