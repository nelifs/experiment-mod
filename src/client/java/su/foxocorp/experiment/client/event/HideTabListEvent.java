package su.foxocorp.experiment.client.event;

import su.foxocorp.experiment.client.util.AsyncUtils;

public class HideTabListEvent {

    private static boolean hidePlayerList = false;

    public static void handle() {
        hidePlayerList = true;

        AsyncUtils.waitForAsync(1000 * 60 * 20).thenRun(() -> hidePlayerList = false);
    }

    public static boolean shouldHidePlayerList() {
        return hidePlayerList;
    }
}
