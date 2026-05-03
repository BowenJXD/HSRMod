# HSRMod — Honkai: Star Rail × Slay the Spire

A comprehensive Slay the Spire mod themed after **Honkai: Star Rail**, adding HSR characters, mechanics, 4 custom acts, 200+ cards, 100+ relics, 100+ monsters, and 36 events.

- **Version:** 3.0.0
- **Author:** Aaron喏
- **Language:** Java 8 / Maven
- **Mod ID:** `HSRMod`

---

## Dependencies

| Dependency | Version |
|---|---|
| ModTheSpire | 3.23.2 |
| BaseMod | 5.33.1 |
| StsLib | 2.5.0 |
| ActLikeIt | 0.9.0 |

Optional integration: `TogetherInSpire`, `VideoTheSpire`

---

## Build

```bash
mvn package
```

The Maven build compiles to `hsr-mod.jar` and auto-deploys to the Steam Slay the Spire installation via `maven-antrun-plugin`. Output is copied to both the `/mods/` directory and `/hsrmod/content/`.

---

## Project Structure

```
src/main/java/hsrmod/
├── modcore/          # Entry point, enums, config
├── characters/       # Playable character (Stella/Trailblazer)
├── cards/            # Legacy card system (by rarity)
├── cardsV2/          # V2 card system (by HSR Path)
├── relics/           # Relics (by tier)
├── powers/           # Status effects, breaks, buffs/debuffs
├── monsters/         # Enemies (by dungeon act)
├── dungeons/         # 4 custom act definitions + scenes
├── events/           # 36 custom events
├── actions/          # Custom game actions
├── patches/          # Javassist patches
├── effects/          # Visual effects
├── signature/        # Signature card system
├── subscribers/      # Event subscription management
└── utils/            # Helpers (ModHelper, PathDefine, DataManager, GAMManager)

src/main/resources/HSRModResources/
├── audio/            # Music and SFX (.ogg/.wav)
├── img/              # Sprites, icons, portraits, backgrounds
│   ├── cards/
│   ├── monsters/
│   ├── powers/
│   ├── relics/
│   ├── scene/
│   ├── ui/
│   └── spine/        # Spine animation files
├── localization/     # ZHS + ENG JSON strings
│   └── {lang}/cards.json, relics.json, powers.json, monsters.json, events.json, keywords.json, ui.json
└── video/
```

---

## Core Systems

### Entry Point — `modcore/HSRMod.java`

The main mod class. Implements BaseMod subscriber interfaces and handles:
- Registering the custom card color (`HSR_PINK`: `#FF8DE3`)
- Auto-loading all cards from the `hsrmod.cards` package
- Auto-loading all relics with HSR-exclusive pool flag support
- Loading localization (auto-detects ZHS or ENG)
- Registering audio, monsters, events, and dungeons
- Initializing the Path selection system

Config toggles (via `HSRModConfig`): relic additions, enemy additions, event additions.

---

### Naming Convention

All in-game IDs are prefixed via `HSRMod.makePath(String)`, which prepends `"HSRMod:"`. Texture and string keys follow the same prefix pattern.

---

### Cards

Two parallel systems exist:

**`cards/`** — Legacy system. Cards extend `BaseCard` and are organized by rarity (`base/`, `common/`, `uncommon/`, `rare/`). The starter deck contains 10 cards (Stella, March 7th, Dan Heng, Himeko, Welt Yang themed).

**`cardsV2/`** — Modern system. Cards are organized by the 10 HSR Paths:

| Package | Path |
|---|---|
| `trailblaze/` | Trailblaze |
| `elation/` | Elation |
| `destruction/` | Destruction |
| `nihility/` | Nihility |
| `propagation/` | Propagation |
| `preservation/` | Preservation |
| `theHunt/` | The Hunt |
| `erudition/` | Erudition |
| `abundance/` | Abundance |
| `remembrance/` | Remembrance |
| `curse/` | Curse |

**Custom card tags** (via `@SpireEnum`): `TRAILBLAZE`, `ELATION`, `DESTRUCTION`, `NIHILITY`, `PROPAGATION`, `PRESERVATION`, `THE_HUNT`, `ERUDITION`, `ABUNDANCE`, `REMEMBRANCE`, `FOLLOW_UP`, `ENERGY_COSTING`, `DEBUFF`, `ENTANGLE`, `CHRYSOS_HEIR`, `TERRITORY`, `REVIVE`, `RUAN_MEI`

**Elemental damage** uses `ElementalDamageInfo` + `ElementalDamageAction` / `ElementalDamageAllAction`.

---

### Monsters

Organized under `monsters/` by act:

| Package | Act | Notable Bosses |
|---|---|---|
| `Exordium/` | Belobog | Cocolia, Antimatter Engine |
| `TheCity/` | Luofu | Phantylia, Worldpurge (multi-stage) |
| `TheBeyond/` | Penacony | Echo of Faded Dreams, Skaracabaz |
| `TheEnding/` | Amphoreus | Anti-Creator, Irontomb, Imprisoned Mythos, Manipulated Logos |
| `Bonus/` | Cross-act | Bonus encounter pool |

All monsters extend `BaseMonster`, which provides HSR-specific HP/damage scaling, ascension modifiers, and custom intent rendering.

---

### Powers

161 power files under `powers/`, split into subpackages:

- `breaks/` — Elemental break statuses (Bleed, Burn, Freeze, Shock, …)
- `enemyOnly/` — Enemy-exclusive mechanics (ChargingPower, PerformancePointPower, SnarelockPower, …)
- `uniqueBuffs/` — Character-specific buffs
- `uniqueDebuffs/` — Character-specific debuffs
- `misc/` — General-purpose effects

All extend `BasePower`, which auto-loads textures and manages localized descriptions.

---

### Relics

101 relic files under `relics/`, organized by tier: `boss/`, `rare/`, `uncommon/`, `common/`, `shop/`, `starter/`, `special/`

`special/` contains Path-specific wax relics (e.g., `WaxOfElation`, `WaxOfDestruction`, `WaxOfRemembrance`).

All extend `BaseRelic`, which integrates with `DataManager` for metadata (tier, sound, magic number, flags) and supports custom `RelicTagField` properties (`destructible`, `subtle`, `economic`, `hsrOnly`).

---

### Dungeons (Acts)

4 custom acts replace the default StS dungeons, registered via `ActLikeIt`:

| Class | Act | Theme | Bosses |
|---|---|---|---|
| `Belobog` | Act 1 | Frozen city | Cocolia, Antimatter Engine |
| `Luofu` | Act 2 | Xianzhou intrigue | Phantylia, Worldpurge |
| `Penacony` | Act 3 | Dream entertainment world | Echo of Faded Dreams, Skaracabaz |
| `Amphoreus` | Act 4 | Rusted iron carcass apocalypse | 4-part final encounter |

Each dungeon has a paired `*Scene` class for background rendering.

---

### Events

36 events in `events/`, registered per-dungeon. Key events:

- **Path events**: Cosmic Crescendo (Belobog), Tavern (Luofu), Slumbering Overlord (Penacony) — trigger Path-related rewards
- **Ruan Mei event**: Special spawn/bonus conditions tied to relics
- **Wax Manufacturer**: Lets the player choose a Path tag for wax relics
- **Story encounters**: Three Little Pigs, We Are Cowboys, Apes Such As You, The Returning Heliobus
- **Shop events**: Robot Sales Terminal, Flea Market (conditional on gold ≥ 50), IOU Dispenser

Events use a `BaseEvent` + `AddEventParams` builder pattern with configurable spawn probability, resource conditions, and bonus conditions.

---

### Patches

27 patch files in `patches/` using Javassist via SpirePatch annotations:

- `OtherModFixes` — Compatibility fixes for other mods
- `PathSelectScreen` — Injects the Path selection UI at run start
- `RelicTagField` — Adds custom fields to AbstractRelic
- Various render/UI patches

---

### Signature System

`signature/` contains an advanced card locking/unlocking system (`SignatureHelper`, custom libraries, compatibility patches). Handles signature cards that unlock under specific conditions.

---

### Utilities

| Class | Purpose |
|---|---|
| `PathDefine` | Centralized resource path constants |
| `DataManager` | Relic metadata store |
| `ModHelper` | General mod helpers |
| `GAMManager` | Game state/act manager |
| `RewardEditor` | Custom reward manipulation and state persistence |

---

### Localization

All strings live in `src/main/resources/HSRModResources/localization/{lang}/`. The mod auto-detects the game language at init and loads either `ZHS/` or `ENG/`. Adding a new language requires creating the corresponding folder and JSON files, then adding a detection branch in `HSRMod.java`.

---

### Audio

Audio files are discovered automatically by scanning the JAR at runtime. Place `.ogg` or `.wav` files in `audio/` and they are registered without manual enumeration. Language-specific audio variants are supported.

---

## Adding Content

**New card (V2 system):** Extend `BaseCard`, place in the appropriate `cardsV2/{path}/` package, add strings to `localization/{lang}/cards.json`. It will be auto-loaded.

**New relic:** Extend `BaseRelic`, place in the appropriate `relics/{tier}/` package, register metadata in `DataManager`, add strings to `localization/{lang}/relics.json`. It will be auto-loaded.

**New power:** Extend `BasePower`, place in the appropriate `powers/` subpackage, add a 128×128 icon at the matching path under `img/powers/`, add strings to `localization/{lang}/powers.json`.

**New monster:** Extend `BaseMonster`, place in `monsters/{act}/`, register in `HSRMod.addMonsters()`, add strings to `localization/{lang}/monsters.json`.

**New event:** Extend `BaseEvent`, register via `AddEventParams` in `HSRMod.addEvents()`, add strings to `localization/{lang}/events.json`.
