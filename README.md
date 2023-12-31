[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/no-prying-eyes)
[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg)](https://github.com/Daxanius/NoPryingEyes)
![fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg)
[![fabric-api](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/requires/fabric-api_vector.svg)](https://modrinth.com/mod/fabric-api)

# No Prying Eyes
A Minecraft mod that aims to provide control over what happens to your data.

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
I will explain everything to the best of my ability, but if something is not clear, feel free to join the [Discord](https://discord.gg/8Mm3g3T5re) and ask for clarification on any of these topics.

### Telemetry
If telemetry is disabled, it will replace the telemetry sender with a dummy that does nothing.

Telemetry is disabled by default.

### Bypassing global bans
A note on the bypass global bans thing. This **does not** work server-side, it is a client-side only feature. It works by tricking the client into thinking that the player is not banned. It falsifies ban data so that all client-side features (eg the multiplayer button) are enabled.

Implementing this server-side is currently beyond the scope of the mod.

Servers with this feature enabled still prevent players that are banned from joining and clients with this feature enabled still can't join servers that don't allow banned profiles.

It is enabled by default.

### Chat reporting
This mod removes chat reporting by allowing both client and server to select between 3 different signing modes. Servers which disable signing (by the latter options) strip all messages of their signature.

The signing mode is set to NO_KEY by default, and it is recommended to keep it this way.

#### 1. SIGN
This is the default Minecraft signing mode, it does not alter anything and allows chat reporting as normal

#### 2.  NO_SIGN
This strips message signatures, but still requires players to send their public key. This is useful for some servers, which enforce secure profiles but don't require signed messages. You can also join servers that require signed messages with this, but you will probably not be able to chat.

It is generally **discouraged to use this option** unless required.

#### 3. NO_KEY
This works both server-side and client-side. It strips signatures and public keys from signed messages (server-side) so that clients who sign their messages can still join the server, while also allowing clients who don't send their key (and also don't sign their messages) to join.

On the client-side this prevents the game client from sending the player's public key to the server, and prevents the client from signing messages.

### Profanity filter
This disables the profanity filter (used in realms) that you can also just disable in your Minecraft account settings. It removes the asterisks. At least on the client-side of things, as I cannot modify realms servers.

It's a nice feature to be able to toggle on the fly without having to potentially go on the Minecraft website, modify your account settings and restarting your game.

About the account settings, you can only disable the profanity filter if Mojang knows you are 18+, this means children cannot disable this setting in their account settings without the consent / control of a guardian. This feature can basically bypass that.

It is disabled by default.

## Reasoning
I believe that online privacy is a basic human right, the collection of your personal data should be opt-in.
You should not be obliged to share your usage information with vendors or third party groups.

Following 1.19, Minecraft implemented telemetry data collection and account bans by allowing players
to report each other on third party servers to Mojang instead of the server moderators.

Mojang should not be involved in the moderation of players on third party servers and they should not force you into
sharing personal usage data.

## Contact

[Discord](https://discord.gg/8Mm3g3T5re)