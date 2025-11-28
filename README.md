# 🎮 Java Text-Based RPG

## 📌 Overview
A story-forward **terminal RPG** built for our **Object-Oriented Programming 1 (OOP1)** final project.  
Development started on **September 16, 2025** and is **still ongoing**, now featuring a continuous loop that covers multiple narrative zones, safe-zone downtime, and cinematic boss encounters.  
The project remains a living lab for **OOP fundamentals** (inheritance, composition, modular systems) while we polish the script and gameplay pacing.

⚠ **Note:** The build is still pre-release, but the internal demo now plays through the current story arc end-to-end.

### ✨ Current Build Highlights
- Zone narration features bespoke intros, boss monologues, and an epilogue-style twist ending.
- Exploration, combat, loot, crafting, and the safe-zone hub are linked, letting testers loop between farming, gearing, and story beats.
- Supporter revival statues, loadout management, and bag/shop systems are active—mentors can now rejoin mid-run.
---

## 🕹 Gameplay Pillars
- **Cinematic Text Flow:** Typewriter narration frames every zone transition, boss gate, and ending reveal.
- **Exploration vs. Safe Zone:** Players push forward, discover statues/events, then fall back to recharge, craft, or manage supporters.
- **Supporter & Bag Systems:** Revived allies grant buffs once equipped in safe zones, while the bag/shop track shards, crystals, and loot.
- **Boss Gates & Crafting:** Each region includes gate checks, blueprint-driven weapons, and escalating encounters to keep progression paced.

---

## 📝 Core Concepts & Systems
- **Shards & Crystals:** Dual resources that fund shops, blueprints, and the rare revives tied to the narrative.
- **Revival Potions:** Drop from key fights and let players awaken statues so supporters can rejoin the run.
- **Fractures & Boss Gates:** Enemy factions that patrol each zone, capped by cinematic gate checks before every boss.
- **Blueprint Crafting:** Weapons evolve via recipe unlocks plus crystal infusions; upgraded gear is required to crack new gates.
- **Supporter Statues:** Story characters frozen in stone; once revived and equipped, they bring passive buffs and combat flavor.

## 🛠 How to Run (For Later)
Once the game reaches a runnable state, you will be able to compile and run it from the project root:
```bash
# Compile
javac -d out $(find src -name "*.java") or
javac -d out (Get-ChildItem -Recurse -Path src -Filter *.java).FullName

# Run
java -cp out rpg.Main or

# Or
We will just make it an executable

```
## 👥 Team
[@chrkk](https://github.com/chrkk)  
[@Cayl-06](https://github.com/Cayl-06)  
[@EthannnJohnnn](https://github.com/EthannnJohnnn)

## 📚 Educational Purpose
This project is for learning and academic purposes only, created as part of our OOP1 final project



