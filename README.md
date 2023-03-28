# Paper-Annotations
Paper annotations will automatically create a `paper-plugin.yml` file for you whenever you compile your plugin!

To learn about a `paper-plugin.yml` file, see https://docs.papermc.io/paper/dev/getting-started/paper-plugins

## Inspiration
I wanted to make what, in my mind, was a significantly cleaner version of Spigot's annotation processor, and apply 
it to all Paper Plugins. I also wanted a light-weight compilation annotation processor, and not runtime, for server boot speeds

## Annotations

#### PaperPlugin
`PaperPlugin` is the only required annotation to generate! It is to be appended to your `JavaPlugin` class.

#### Dependency
`Dependency` annotation will account for any dependencies for your plugin! This also gets appended to your `JavaPlugin` class

#### LoadBefore.Plugin
`LoadBefore.Plugin` annotation specifies all plugins that you want to mandate be loaded before yours. 
This is another `JavaPlugin` class annotation

_Be careful about cyclic loading!_

#### LoadAfter.Plugin
`LoadAfter.Plugin` annotation specifies all plugins that you want to mandate be loaded after yours.
This is another `JavaPlugin` class annotation

_Be careful about cyclic loading!_

#### Bootstrapper
`Bootstrapper` annotation can be applied to your `PluginBootstrapper` class to register it

#### Loader
`Loader` annotation will register your `PluginLoader` instance

## Todo

- Address `AnnotationProcessor` logic, I put in a very lazy error check for now because the return logic is
very poor, but that isn't great for production
- Add possible registries for Command and Listener based annotation registry