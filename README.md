[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/no-prying-eyes)
[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg)](https://github.com/Daxanius/NoPryingEyes)
![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg)
[![fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

# No Prying Eyes
NPE is a simple Minecraft mod that aims to give the user control over the collection of their data in an attempt to free the game.

## Requirements

- Fabric loader
- Fabric API

## Features
All of these features are configurable and enabled by default.

- Disable chat reporting using 3 different modes
- Disable telemetry data collection
- (Clientside) bypass global bans
- Disable profanity filter
- Fake ban (yes, pretend to be banned)
- More features planned

## How it works

Here I do my best to try to explain how the mod works, but if something is not clear, feel free to join my Discord and ping me asking for clarification on any of these topics.

### Telemetry
If telemetry is disabled, it will make the game think it has already sent it's telemetry data to Mojang, causing it to not send the data. It does so by injecting code into the constructor of the Telemetry class, and tells it: "hey, you've already sent this data, don't do it".

Telemetry is disabled by default.

### Bypassing global bans
A note on the bypass global bans thing. This DOES NOT work server-side, it is a client-side only feature. It works by tricking the client into thinking that the player is not banned. It falsifies ban data so that all client-side features (eg the multiplayer button) are enabled.

Implementing this server-side is currently beyond the scope of the mod.

Servers with this feature enabled still prevent players that are banned from joining and clients with this feature enabled still can't join servers that don't allow banned profiles.

It is enabled by default.

### Chat reporting

This mod removes chat reporting by allowing both client and server to select between 3 different signing modes. Servers which disable signing (by the latter options) send messages as system messages, meaning that the messages won't display as unsafe on the client-side of things.

The signing mode is set to NO_KEY by default, and it is recommended to keep it this way.

#### 1. Sign

This is the default Minecraft signing mode, it does not alter anything and allows chat reporting as normal

#### 2.  NO_SIGN

This strips message signatures, but still requires players to send their public key. This is useful for weird servers which enforce secure profiles, but don't require signed messages. You can also join servers that require signed messages with this, but they will kick you the moment you send a message stripped of signature.

it is generally recommended to not use this option unless required.

#### 3. NO_KEY

This works both server-side and client-side. It strips signatures and public keys from signed messages (server-side) so that clients who sign their messages can still join the server, while also allowing clients who don't send their key (and also don't sign their messages) to join.

On the client-side of things this prevents the game client from sending the player's public key to the server, and prevents the client from signing messages.

### Profanity filter
This disables the profanity filter (used in realms) that you can also just disable in your Minecraft account settings. It removes the asterisks. At least on the client-side of things, as I cannot modify realms servers.

It's a nice feature to be able to toggle on the fly without having to potentially go on the Minecraft website, modify your account settings and restarting your game.

About the account settings, you can only disable the profanity filter if Mojang knows you are 18+, this means children cannot disable this setting in their account settings without the consent / control of a guardian. This feature can basically bypass that.

It is disabled by default.

## Reason

I am of the belief that online privacy is a right that everyone has and
the collection of data by big tech should be optional. You have no obligation to share personal
information with said companies and said companies do not have the right to collect data on the users of their product.

The newest version of Minecraft implemented telemetry data collection and account bans by allowing players
to report each other to Mojang instead of the server moderators.

Mojang should not be involved in the moderation of players on third party servers and they do not attain
the right to collect your user data. (This being my personal take on it)

Also I just kinda wanted to make this mod for fun...

## Contact

Feel free to join my [Discord](https://discord.gg/8Mm3g3T5re)
