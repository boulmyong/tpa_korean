package com.tpasystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TPATabCompleter implements TabCompleter {

    private final TPASystem plugin;

    public TPATabCompleter(TPASystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;
        if (args.length == 1) {
            if (command.getName().equalsIgnoreCase("tpa")) {
                return plugin.getServer().getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            }

            if (command.getName().equalsIgnoreCase("tpaccept") || command.getName().equalsIgnoreCase("tpdeny")) {
                TPAManager tpaManager = plugin.getTpaManager();
                TPARequest request = tpaManager.getRequest(player.getUniqueId());
                if (request != null) {
                    List<String> completions = new ArrayList<>();
                    String requesterName = request.getRequester().getName();
                    if (requesterName.toLowerCase().startsWith(args[0].toLowerCase())) {
                        completions.add(requesterName);
                    }
                    return completions;
                }
            }
        }
        return null;
    }
}
