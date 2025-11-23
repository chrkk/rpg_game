# 🎮 Java Text-Based RPG

## 📌 Overview
A simple **terminal-based role-playing game** built in Java for our **Object-Oriented Programming 1 (OOP1)** subject.  
This is our **final project** for OOP1, started on **September 16, 2025** and **still ongoing**.  
The game demonstrates **OOP concepts** such as classes, objects, inheritance, and modular design.

⚠ **Note:** This project is in its **very early development stage**.  
It is **not ready to run or play yet** — core systems and features are still being built.

---

## 🕹 Planned Features
- Start menu (Start Game/Exit)
- Player creation
- Turn-based combat
- Basic exploration
- Expandable storyline

---

## 📝 Core Concepts Game Ideas 
- Currency: Shards
- Valuable Item: Crystals — used for crafting and reviving characters
- Reviving Mechanic: Requires a specific amount of Crystals
- Enemies (Mobs): Called Fractures
- Core Crafting System:
- Normal Item + Crystals = Special Item
    Example: Pencil + Crystals = Pencil Blade
- Mini Bosses: Mascot-themed Fractures
    Example: JoliBeast = Fractured Jolibee

## 🌌 Story Progression (Current Build)
| Zone | Name | Theme | Palette | Tone |
| --- | --- | --- | --- | --- |
| 1 | School Rooftop | Awakening / Confusion | Gray, Brown | "What happened?" |
| 2 | Ruined Lab | Failed Science / Discovery | Green chemicals, Chrome machinery | "We caused this?" |
| 3 | City Ruins | Civilization's Fall / Survival | Orange fire, Black ash | "How do we survive?" |
| 4 | Fractured Sky | Ascension / Truth | Blue sky, White light | "We're close to the truth." |
| 5 | The Source (teaser) | Confrontation / Sacrifice | Blinding White, Void Black | "This ends here." |

### Zone 4: Fractured Sky (NEW)
- **Location:** A Broken Skyscraper stitched together by exposed Sky Bridges; halfway to the clouds and buffeted by storms.
- **Safe Zone:** *Observation Deck* — glassless windows, emergency generators, and the clearest view of The Source so far.
- **Atmosphere:** Shattered floors, open air, the pillar of light visible in the distance, and the sense that the ascent has begun.
- **Boss Gate Requirement:** Craft the **Thunder Spear** to punch through the storm barrier guarding the Nimbus Tyrant.
- **Thunder Spear Blueprint:** Sold by Scrapwright Kuro in the Zone 4 shop for **25 Shards**.
- **Thunder Spear Crafting:** Requires the Flame Axe as its base and **12 Crystals**. Stats: **30-50 DMG**, **25% crit**, **2.2x crit multiplier**. Lore whispers that it was forged from lightning captured mid-storm to pierce light itself.

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



