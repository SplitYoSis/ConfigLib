package dev.splityosis.configsystem.configsystem.actionsystem;

import dev.splityosis.configsystem.configsystem.ConfigSystem;
import dev.splityosis.configsystem.configsystem.actionsystem.actiontypes.DelayActionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actions {

    private List<ActionData> actionDataList;

    public Actions(List<ActionData> actionDataList) {
        this.actionDataList = actionDataList;
        if (actionDataList == null)
            this.actionDataList = new ArrayList<>();
    }

    /**
     * Runs all the actions where the target is the given player and sets PlaceholderAPI (if exists) on given player.
     * @param player Target
     * @param placeholders Placeholders that will be set, Map in the form of <From, To>
     */
    public void perform(@Nullable Player player, @Nullable Map<String, String>... placeholders){
        Map<String, String> replacements = new HashMap<>();
        if (placeholders != null)
            for (Map<String, String> placeholder : placeholders) {
                if (placeholder != null)
                    replacements.putAll(placeholder);
            }
        handle(0, player, replacements);
    }

    /**
     * Runs all the actions where the target is the given player and sets PlaceholderAPI (if exists) on given player.
     * @param player Target
     * @param placeholders Placeholders that will be set, Map in the form of <From, To>
     */
    public void perform(@Nullable Player player, @NotNull Map<String, String> placeholders){
        perform(player, new Map[]{placeholders});
    }

    private void handle(int i, Player player, Map<String, String> placeholders){
        for (; i < actionDataList.size(); i++) {
            ActionData actionData = actionDataList.get(i);
            ActionType actionType = ActionType.getActionType(actionData.getActionKey());
            if (actionType == null)
                new InvalidActionTypeException(actionData.getActionKey()).printStackTrace();
            else if (actionType instanceof DelayActionType && ConfigSystem.plugin != null){
                if (actionData.getParameters().size() == 0) continue;
                int ticks;
                try {
                    ticks = Integer.parseInt(actionData.getParameters().get(0));
                }catch (Exception e){
                    new InvalidActionParameterException("Invalid wait time '"+actionData.getParameters().get(0)).printStackTrace();
                    continue;
                }

                int finalI = i;
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        handle(finalI +1, player, placeholders);
                    }
                }.runTaskLater(ConfigSystem.plugin, ticks);
                return;
            }
            else
                actionType.run(player, actionData.getParameters(), placeholders);
        }
    }


    /**
     * Runs the same logic as {perform} on all the players online.
     */
    public void performOnAll(@Nullable Map<String, String>... placeholders){
        Map<String, String> replacements = new HashMap<>();
        if (placeholders != null)
            for (Map<String, String> placeholder : placeholders) {
                if (placeholder != null)
                    replacements.putAll(placeholder);
            }
        handleAll(0, replacements);
    }

    /**
     * Runs the same logic as {@link #perform(Player)} on all the players online.
     */
    public void performOnAll(@Nullable Map<String, String> placeholders){
        performOnAll(new Map[]{placeholders});
    }

    /**
     * Runs the same logic as {@link #perform(Player)} on all the players online.
     */
    public void performOnAll(){
        performOnAll();
    }

    public void handleAll(int i, Map<String, String> placeholders){
        for (; i < actionDataList.size(); i++) {
            ActionData actionData = actionDataList.get(i);
            ActionType actionType = ActionType.getActionType(actionData.getActionKey());
            if (actionType == null)
                new InvalidActionTypeException(actionData.getActionKey()).printStackTrace();
            else if (actionType instanceof DelayActionType && ConfigSystem.plugin != null){
                if (actionData.getParameters().size() == 0) continue;
                int ticks;
                try {
                    ticks = Integer.parseInt(actionData.getParameters().get(0));
                }catch (Exception e){
                    new InvalidActionParameterException("Invalid wait time '"+actionData.getParameters().get(0)).printStackTrace();
                    continue;
                }

                int finalI = i;
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        handleAll(finalI +1, placeholders);
                    }
                }.runTaskLater(ConfigSystem.plugin, ticks);
                return;
            }
            else
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    actionType.run(onlinePlayer, actionData.getParameters(), placeholders);
                }
        }
    }

    public List<ActionData> getActionDataList() {
        return actionDataList;
    }
}
