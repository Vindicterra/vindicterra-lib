package org.vindicterra.vindicterralib.commandcreator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
    /**
     * Returns the help menu containing all sub commands and their descriptions
     * @param command Parent command to show menu for
     * @return Component message to show to player
     */
    public static Component returnHelpMenu(ParentCommand command) {
        List<Component> subCommands = new ArrayList<>();
        for (ChildCommand sub : command.getSubCommands()) {
            subCommands.add(Component.text(sub.getName() + " - " + sub.getDescription()));
        }
        Component header = Component.text("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", NamedTextColor.GOLD)
            .appendNewline()
            .append(Component.text(String.format("   --- /%s Subcommands ---",
                command.getName()), NamedTextColor.AQUA))
            .appendNewline().appendNewline();
        for (Component component : subCommands) {
            header = header.append(component).appendNewline();
        }
        return header.appendNewline().append(Component.text(
            "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-", NamedTextColor.GOLD));
    }
}
