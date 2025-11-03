package rpg.skills;

public class ScientistSkills {

    // 🧪 Basic Skill
    public static Skill chemicalStrike = new Skill("Chemical Strike", 10, 25) {
        @Override
        public String useSkill() {
            return "You hurl a vial of corrosive chemicals at the enemy!";
        }
    };

    // ⚡ Secondary Skill
    public static Skill plasmaField = new Skill("Plasma Field", 15, 35) {
        @Override
        public String useSkill() {
            return "You unleash a field of charged plasma around you!";
        }
    };

    // 💥 Ultimate Skill
    public static Skill nuclearBlast = new Skill("Nuclear Blast", 30, 60) {
        @Override
        public String useSkill() {
            return "A blinding explosion engulfs everything in your path!";
        }
    };
}