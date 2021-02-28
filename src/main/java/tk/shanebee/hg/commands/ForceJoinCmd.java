package tk.shanebee.hg.commands;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import tk.shanebee.hg.HG;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.util.Util;

public class ForceJoinCmd extends BaseCmd {

    public ForceJoinCmd() {
        forcePlayer = true;
        cmdName = "join-force";
        forceInGame = false;
        argLength = 3;
        usage = "<player-name> <arena-name>";
        permissionDefault = PermissionDefault.TRUE;
    }

    @Override
    public boolean run() {
        if(!player.hasPermission("hg.join-force"))
            return true;
        player = Bukkit.getPlayer(args[1]);
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

        return true;
    }

}
