package dev.splityosis.configsystem.configsystem.actionsystem.actiontypes;

import dev.splityosis.configsystem.configsystem.actionsystem.ActionType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DelayActionType extends ActionType {

    public DelayActionType() {
        super("delay", "wait", "pause", "sleep");
    }

    @Override
    public void run(@Nullable Player player, @NotNull List<String> params, @NotNull Map<String, String> placeholders) {

    }
}
