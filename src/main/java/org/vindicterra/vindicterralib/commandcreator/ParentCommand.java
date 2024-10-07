package org.vindicterra.vindicterralib.commandcreator;

import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a parent command.<br>
 * Needs to be extended to be used as a command.<br><br>
 * this.getCommand("example").setExecutor(new ExampleParentCommand(this, ExampleParentCommand.class.getAnnotation(CommandData.class)));<br><br>
 * this.getCommand("example").setTabCompleter(new ExampleParentCommand(this, ExampleParentCommand.class.getAnnotation(CommandData.class)));<br>
 *
 * <p>@CommandData(
 *     name = "example",<br>
 *     permission = "example.permission",<br>
 *     description = "An example command",<br>
 *     aliases = {"ex", "e"},<br>
 *     playerOnly = true,<br>
 *     usage = "{@literal <param1>} {@literal <param2|param3>} [optional param]",<br>
 *     requiredArgs = 2)
 */
@Getter
public abstract class ParentCommand extends Command implements TabExecutor {

    protected transient final JavaPlugin plugin;

    private final CommandData data;
    private final boolean playerOnly;

    @Setter
    boolean enabled = true;

    private List<ChildCommand> subCommands;

    public ParentCommand(JavaPlugin plugin, CommandData command) {
        super(command.name(), command.description(), command.usage(), Arrays.asList(command.aliases()));

        this.plugin = plugin;
        this.data = command;
        this.setPermission(command.permission());
        this.playerOnly = command.playerOnly();
    }
    /**
     * executes the command and the abstract tests(do not override implement run instead)
     * @param sender the sender of the command
     * @param label the label of the command
     * @param args the arguments of the command
     * @return true if the command was executed successfully
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!enabled && !sender.isOp()) {
            sender.sendMessage("§cThis command is currently disabled.");
            return true;
        }

        if(playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cOnly a player can use this command.");
            return true;
        }

        if(!testPermissionSilent(sender)) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if(args.length < data.requiredArgs()) {
            sendUsage(sender, label, 1);
            return true;
        }

        if(args.length > 0) {
            ChildCommand subCommand = this.getSubCommand(args[0]);

            if(subCommand != null) {

                subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
                //VindicterraFactions.getInstance().getFactionHandler().save(true);
                return true;
            } else if(subCommands != null && !subCommands.isEmpty()) {
                int page = 1;

                if(args.length > 1 && args[0].equalsIgnoreCase("help")) {
                    if(Ints.tryParse(args[1]) == null) {
                        sender.sendMessage("§cInvalid page.");
                        return true;
                    }

                    page = Integer.valueOf(args[1]);
                }

                sendUsage(sender, label, page);
                return true;
            }
        }

        run(sender, label, args);
        //VindicterraFactions.getInstance().getFactionHandler().save(true);
        return true;
    }

    public void addCommand(Class<? extends ChildCommand> subCommand) {
        try {
            if(this.subCommands == null) {
                this.subCommands = new ArrayList<>();
            }

            Constructor<? extends ChildCommand> constructor = subCommand.getConstructor(JavaPlugin.class, CommandData.class);
            CommandData data = subCommand.getAnnotation(CommandData.class);

            ChildCommand command = constructor.newInstance(this.plugin, data);
            this.subCommands.add(command);
        } catch(Exception exception) {
            plugin.getLogger().severe("Failed to add faction sub command " + subCommand.getSimpleName() + "." + exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace()));
        }
    }

    public ChildCommand getSubCommand(String name) {
        return this.subCommands == null ? null : this.subCommands.stream()
                .filter(subCommand -> subCommand.getName().equalsIgnoreCase(name)
                        || subCommand.getAliases().contains(name.toLowerCase()))
                .findAny().orElse(null);
    }

    public List<String> getUsageOfCommands(List<ChildCommand> subCommands) {
        List<String> names = new ArrayList<>();

        for(ChildCommand subCommand : subCommands) {
            names.add("/" + getName() + " " + subCommand.getName() + " " + (subCommand.getData().usage().isEmpty() ? "" : subCommand.getData().usage() + " ") + "§7- " + subCommand.getDescription() + "§r");
        }

        return names;
    }

    public void sendUsage(CommandSender sender, String label, int page) {
        if(this.subCommands == null) {
            sender.sendMessage("§cUsage: /" + label + " " + this.usageMessage);
            return;
        }
        if(page <= 0) {
            sender.sendMessage("§cInvalid page.");
            return;
        }
        //Only display usage for sub commands the sender has access to
        final List<ChildCommand> subCommands = this.subCommands.stream()
                .filter(command -> command.hasPermission(sender))
                .collect(Collectors.toList());
        if (subCommands.isEmpty()) {
            return;
        }

        final int index = (page - 1);
        final int threshold = 10;
        final int ceil = (int) Math.ceil(((double) subCommands.size() / threshold));
        if (ceil >= 1 && page > ceil) {
            sender.sendMessage("§cInvalid page.");
            return;
        }

        List<String> toSend = new ArrayList<>();
        List<String> names = this.getUsageOfCommands(subCommands);

        final int start = (index * threshold);
        final int finish = (page * threshold) - 1;

        for (int i = start; i <= finish && i < names.size(); i++) {
            toSend.add(names.get(i));
        }

        sender.sendMessage("§7§m                                                                                              ");
        sender.sendMessage("§6§l" + WordUtils.capitalizeFully(getName()) + "§6 Command Help" + (page <= ceil ? " §7(Page §f#" + page + "§7 out of §f" + ceil + "§7)" : ""));
        sender.sendMessage(" ");
        sender.sendMessage(StringUtils.join(toSend, "\n"));

        if (ceil > page) {
            sender.sendMessage(" ");

            TextComponent clickableComponent = new TextComponent("§6§lHERE");
            clickableComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aView the next page").create()));
            clickableComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + getName() + " help " + (page + 1)));

            TextComponent component = new TextComponent("§6Click ");
            component.addExtra(clickableComponent);
            component.addExtra("§6 to view the next page.");

            sender.spigot().sendMessage(component);


            sender.sendMessage("§7§m                                                                                              ");
        }
    }

    @SuppressWarnings("EmptyMethod")
    public abstract void run(CommandSender sender, String label, String[] args);//L
}
