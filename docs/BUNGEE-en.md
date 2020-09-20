# PeyangSuperbAntiCheat(PSAC) BungeeCord Integration Tutorial

[Overview](README-en.md#overview) | [Installation](README-en.md#installation) | [Permissions](README-en.md#permissions) | [Commands](README-en.md#commands) | [Config settings](README-en.md#config-settings) | BungeeCord | [FAQ](README-en.md#what-is-this-npcwatchdog)

## Overview

PSAC can be managed through all servers using BungeeCord.  
This document provides a way to enable BungeeCord integration.

## What changes with BungeeCord integration

BungeeCord is well known as a proxy server that manages all servers at the same time.  
PSAC takes advantage of it to implement a variety of new features.  

- Receiving reports on all servers
- etc... \(sorry\)

## Installation

1. Install PSAC on all servers and BungeeCord.  
   Binary releases is [here](https://github.com/P2P-Develop/PeyangSuperbAntiCheat/releases)
2. *Optional*: Change the configuration of each server. Be careful when changing the configuration!

## Precautions

### How to change the plugin configuration

Edit `database.HogeHogePath`.  
Only a **relative path** are supported for path specification.  
If the directories is:
```
servers/
├─ database.db
├─ database2.db
├─ server1/
│  ├─ plugins/
│  │  ├─ PSAC.jar
│  │  ├─ PeyangSuperbAntiCheat/
│  │  │  ├─ config.yml
│  ├─ foo
│  ├─ bar
│  ├─ Bukkit.jar
├─ server2/
│  ├─ plugins/
│  │  ├─ PSAC.jar
│  │  ├─ PeyangSuperbAntiCheat/
│  │  │  ├─ config.yml
│  ├─ foo
│  ├─ bar
│  ├─ Bukkit.jar
```
In such cases, the configuration path will be `../../ databaseX.db` counting from the plugin. X is determined by the incremental number.