# CardShop Mod — Minecraft 1.20.4 Forge

## Installation (comme n'importe quel mod Forge)

1. Installe **Forge 1.20.4** : https://files.minecraftforge.net
   → Télécharge l'installer → double-clic → "Install client"

2. Lance Minecraft avec le profil **Forge 1.20.4**
   → Ferme Minecraft

3. Va dans `%appdata%\.minecraft\mods\`
   → Colle **cardshop-1.0.0.jar**

4. Relance Minecraft avec Forge → le mod est actif !

## Pour le serveur

1. Télécharge le **server installer** Forge 1.20.4
2. Lance-le → "Install server" → choisis un dossier
3. Crée `start.bat` :
   ```
   java -Xmx2G -jar forge-1.20.4-49.0.38-server.jar nogui
   pause
   ```
4. Lance une fois → accepte l'EULA (eula.txt → eula=true)
5. Mets **cardshop-1.0.0.jar** dans le dossier `mods/`
6. Relance le serveur

## Jouer ensemble (même réseau)
- Lance le serveur → ta copine se connecte via `localhost`

## Jouer ensemble (Internet)
- Télécharge ngrok (https://ngrok.com) → lance `ngrok tcp 25565`
- Donne l'adresse affichée à ta copine

---

## Gameplay
- **Craft un Shop Stand** (Chest + Planches + Dalles)
- **Place des meubles** autour pour monter en rang (1→5)
- **Les villageois visitent** ta boutique selon ton rang
- **Clic droit** sur un visiteur pour vendre tes items et cartes
- **Ouvre des boosters** (clic droit) pour obtenir des cartes
- **Équipe des augments** pour améliorer ton personnage

## Cartes
159 cartes : Monstres, Animaux, Blocs, Outils, Chats, Alcools
Raretés : Commune (60%), Rare (25%), Épique (12%), Légendaire (3%)
