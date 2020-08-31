# Requirements

The information here was gathered from online research. Sources below: 

http://www.neocomputer.org/projects/doomrpg/

Quakeman224
https://www.youtube.com/watch?v=cHzKF1buDp0
https://www.youtube.com/watch?v=9uXEfp28Jxs
https://www.youtube.com/watch?v=TrurE3tDGMo

The information will be managed in the level editor on a per-level basis except for the Entities, since those will be global.

## Entities

The engine handles a set of abstract items and entities that need to be configured for each level. For example the game 
engine will define a class Enemy. The level editor will need a way to define a concrete a version of this enemy(for example
a HellHound).

Types of available entities:
- Enemy
- Human
- Key Item
- Health
- Armor
- Credits
- Weapon
- Ammo
- Furniture
- Breakable

## Dialog 

We need a UI to manage the dialog for each entity. The dialog may be stateless or statefull and it may contain multiple 
options. All these may be manageable visually. Managing Dialogs also include a way to ensure a Dialog is linked to an 
event/trigger.

## Triggers/Events

Changes in the game will be done via triggers and events. We need a UI to manage the possible triggers and events for each
level. This component also should ensure all triggers/events are tied to a valid location or entity.

## Map Editor

There is the necessity for creating a mechanism to manage an entire level and to place all the required entities and 
triggers in it.