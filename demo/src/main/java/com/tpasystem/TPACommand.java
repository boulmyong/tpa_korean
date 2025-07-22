package com.tpasystem;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPACommand implements CommandExecutor {

    private final TPASystem plugin;

    public TPACommand(TPASystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "사용법: /tpa <플레이어>");
            return false;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "플레이어를 찾을 수 없습니다.");
            return true;
        }

        Player requester = (Player) sender;
        if (requester == target) {
            requester.sendMessage(ChatColor.RED + "자기 자신에게 텔레포트 요청을 보낼 수 없습니다.");
            return true;
        }

        TPAManager tpaManager = plugin.getTpaManager();
        long cooldown = tpaManager.getCooldown(requester);
        if (cooldown > 0) {
            requester.sendMessage(ChatColor.RED + "TPA 요청을 보내려면 " + cooldown + "초를 더 기다려야 합니다.");
            return true;
        }

        if (tpaManager.getRequest(target.getUniqueId()) != null) {
            requester.sendMessage(ChatColor.RED + "해당 플레이어에게 이미 보낸 요청이 있습니다.");
            return true;
        }

        TPARequest tpaRequest = new TPARequest(requester, target);
        tpaManager.addRequest(tpaRequest);

        requester.sendMessage(ChatColor.GREEN + "[TPA] " + ChatColor.WHITE + target.getName() + "님에게 텔레포트 요청을 보냈습니다.");

        TextComponent message = new TextComponent(ChatColor.GREEN + "[TPA] " + ChatColor.WHITE + requester.getName() + "님이 당신에게 텔레포트를 요청했습니다. ");
        TextComponent acceptButton = new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + "[✔️ 수락]");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("클릭하여 텔레포트 요청을 수락합니다.")));

        TextComponent denyButton = new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "[❌ 거절]");
        denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
        denyButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("클릭하여 텔레포트 요청을 거절합니다.")));
        
        TextComponent space = new TextComponent(" ");

        message.addExtra(acceptButton);
        message.addExtra(space);
        message.addExtra(denyButton);

        target.spigot().sendMessage(message);

        return true;
    }
}
