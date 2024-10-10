package org.vindicterra.vindicterralib.commandcreator;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Represents a child command of a parent command.
 * Needs to be extended to be used as a command.
 * Must be registered in the parent command to be used.
 * this.addSubCommand(ExampleCommand.class);
 *
 * <p>@CommandData(
 *     name = "example",<br>
 *     permission = "example.permission",<br>
 *     description = "An example command",<br>
 *     aliases = {"ex", "e"},<br>
 *     playerOnly = true,<br>
 *     usage = "{@literal <param1>} {@literal <param2|param3>} [optional param]",<br>
 *     requiredArgs = 2)<br>
 *     public class ExampleChildCommand extends ChildCommand {<br>
 * @see ParentCommand
 * @see CommandData
 */
@Getter
@SuppressWarnings("unused")
public abstract class ChildCommand {
    private final Plugin plugin;
    private final CommandData data;
    private final String name;
    private final String permission;
    private final String description;
    private final List<String> aliases;
    private final boolean playerOnly;

    @Setter
    private boolean enabled = true;

    public ChildCommand(Plugin plugin, CommandData command){
        this.plugin = plugin;
        this.data = command;
        this.name = command.name();
        this.permission = command.permission();
        this.description = command.description();
        this.aliases = List.of(command.aliases());
        this.playerOnly = command.playerOnly();
    }

    /**
     * @param sender the sender of the command
     * @param label the label of the command
     * @param args the arguments of the command
     */
    public void execute(CommandSender sender, String label, String[] args) {
        if(!enabled) {
            sender.sendMessage("§cThis command is currently disabled.");
            return;
        }

        if(playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cOnly a player can use this command.");
            return;
        }

        if(hasPermission() && !sender.hasPermission(permission)) {
            sender.sendMessage("§cNo permission.");
            return;
        }

        run(sender, label, args);
    }

    /**
     * @return whether the command has a permission node
     */
    public boolean hasPermission() {
        return !this.permission.isEmpty();
    }

    /**
     * @param sender the sender of the command
     * @return whether the sender has the permission to execute the command
     */
    public boolean hasPermission(CommandSender sender) {
        return !this.hasPermission() || sender.hasPermission(this.permission);
    }

    /**
     * @param sender the sender of the command
     * @param label the label of the command
     * @param args the arguments of the command
     */
    public abstract void run(CommandSender sender, String label, String[] args);

    /**
     * @param commandSender the sender of the command
     * @param command the command being executed
     * @param s the label of the command
     * @param strings the array of arguments to autocomplete
     * @return a list of valid completions
     */
    public abstract @NonNull List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings);
}
