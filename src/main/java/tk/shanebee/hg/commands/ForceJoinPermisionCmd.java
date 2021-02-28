package tk.shanebee.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import tk.shanebee.hg.HG;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.util.Util;

public class ForceJoinPermisionCmd extends BaseCmd {

    public ForceJoinPermisionCmd() {
        forcePlayer = true;
        cmdName = "join-force-perm";
        forceInGame = false;
        argLength = 2;
        usage = "<arena-name>";
        permissionDefault = PermissionDefault.TRUE;
    }

    @Override
    public boolean run() {
        if(!player.hasPermission("hg.join-force-perm"))
            return true;
        for(Player player : Bukkit.getOnlinePlayers()){
            this.player = player;
            if(player.hasPermission("hg.join")){
                if (playerManager.hasPlayerData(player) || playerManager.hasSpectatorData(player)) {
                    Util.sendPrefixedMessage(player, HG.getPlugin().getLang().cmd_join_in_game);
                } else {
                    Game g = gameManager.getGame(args[2]);
                    if (g != null && !g.getGamePlayerData().getPlayers().contains(player.getUniqueId())) {
                        g.getGamePlayerData().join(player, true);
                    } else {
                        Util.sendPrefixedMessage(player, lang.cmd_delete_noexist);
                    }
                }
            }

        }

        return true;
    }

}
