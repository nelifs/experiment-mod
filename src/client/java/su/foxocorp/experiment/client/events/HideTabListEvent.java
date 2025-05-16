package su.foxocorp.experiment.client.events;

import su.foxocorp.experiment.client.utils.AsyncUtils;

public class HideTabListEvent {

    private static boolean hidePlayerList = false;

    public static void handleEvent() {
        hidePlayerList = true;

        AsyncUtils.waitForAsync(1000 * 60 * 20).thenRun(() -> hidePlayerList = false);
    }

    public static boolean shouldHidePlayerList() {
        return hidePlayerList;
    }
}
