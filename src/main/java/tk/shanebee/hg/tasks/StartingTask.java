package tk.shanebee.hg.tasks;

import org.bukkit.Bukkit;
import tk.shanebee.hg.HG;
import tk.shanebee.hg.data.Config;
import tk.shanebee.hg.data.Language;
import tk.shanebee.hg.data.PlayerData;
import tk.shanebee.hg.game.Game;
import tk.shanebee.hg.game.GameArenaData;
import tk.shanebee.hg.game.Team;
import tk.shanebee.hg.managers.PlayerManager;
import tk.shanebee.hg.util.Util;

import java.util.*;

public class StartingTask implements Runnable {

    private int timer;
    private final int id;
    private final Game game;
    private final Language lang;

    public StartingTask(Game game) {
        HG plugin = HG.getPlugin();
        GameArenaData arenaData = game.getGameArenaData();
        this.timer = arenaData.getCountDownTime();
        this.game = game;
        this.lang = plugin.getLang();
        String name = arenaData.getName();
        String broadcast = lang.game_started
                .replace("<arena>", name)
                .replace("<seconds>", "" + timer);
        if (Config.broadcastJoinMessages) {
            Util.broadcast(broadcast);
            Util.broadcast(lang.game_join.replace("<arena>", name));
        } else {
            this.game.getGamePlayerData().msgAll(broadcast);
        }
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 5 * 20L, 20L);
    }

    @Override
    public void run() {
        timer -= 1;

        if (timer <= 0) {
            stop();
            if(HG.getPlugin().getTeamSize() != 0){
                int TeamID = 1;
                Object[] players = game.getGamePlayerData().getPlayers().toArray();
                ArrayList<Object> playersList = new ArrayList<>(Arrays.asList(players));

                Random random = new Random();

                while(playersList.toArray().length!=0){
                    int var2 = random.nextInt(playersList.toArray().length);
                    PlayerData playerData = HG.getPlugin().getPlayerManager().getPlayerData((UUID) playersList.get(var2));

                    Team team = new Team(playerData.getBukkitPlayer(), "Team " + TeamID, playerData.getGame());
                    playerData.setTeam(team);
                    playersList.remove(var2);


                    for(int i=0;i<HG.getPlugin().getTeamSize()-1;i++){
                        if(playersList.toArray().length == 0)
                            break;
                        var2 = random.nextInt(playersList.toArray().length);
                        playerData = HG.getPlugin().getPlayerManager().getPlayerData((UUID) playersList.get(var2));

                        playerData.setTeam(team);
                        playerData.getBukkitPlayer().teleport(Bukkit.getPlayer(playerData.getTeam().getLeader()));
                        playersList.remove(var2);
                    }

                }
            }
            game.startFreeRoam();
        } else if (timer % 10 == 0) {
            game.getGamePlayerData().msgAll(lang.game_countdown.replace("<timer>", "" + timer));
        }
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
