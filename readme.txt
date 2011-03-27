[DEV] PluginDisabler v0.0.1 Easy Plugin Disabling [cb556]

Plugin Disabler

Download
Source (Coming Soon)

While attempting to integrate Permissions into another plugin I was working on I found myself wishing there was a way to selectively enable and disable plugins at will so I could make sure I was hooking into it correctly. From that need this plugin was born.

You can use this plugin to disable and enable selected plugins quickly and easily.

Commands:
penable ARGUMENTS - Enables all plugins specified. (alias: pon)
pdisable ARGUMENTS - Disables all plugins specified. (alias: poff)
ptoggle ARGUMENTS - Toggles the status of the plugins specified (alias: p)
pstatus ARGUMENTS - Lists the enabled/disabled status of each specified plugin (alias: pstat)

ARGUMENTS can be the following:
* - all plugins
   (no argument) - whatever argument you used last
pluginname1 pluginname2 ... - This plugin will attempt to match the plugin names you typed. All plugins that match will be used. This is not case sensitive.

Examples:

Show the status of all plugins:
pstat *

Disable the Permissions plugin and the iConomy plugin (and any other plugins that start with 'perm' or 'icon'):
poff perm icon

Enable the plugins you disabled last time:
pon

Toggle the status of the Essentials plugins (enable them if they are currently disabled or vice versa):
p essen


This plugin will never disable itself. Players must be ops to use these commands.


Version 0.0.1
Release