package su.foxocorp.experiment.client.network;

import su.foxocorp.experiment.client.events.*;
import su.foxocorp.experiment.common.ServerEventPayload;

public class ServerEventHandler {
    public void handleEvent(ServerEventPayload payload) {
        switch (payload.eventType()) {
//            case "changeWindowSize":
//                ChangeWindowSizeEvent.changeWindowSize();
//                break;
            case "changeWindowTitle":
                ChangeWindowTitleEvent.changeWindowTitle(payload.args());
                break;
            case "testEvent":
                TestEvent.handleTestEvent();
                break;
            case "changeRenderDistanceEvent":
                ChangeRenderDistanceEvent.changeRenderDistance(Integer.parseInt(payload.args()));
                break;
            case "hideTabListEvent":
                HideTabListEvent.handleEvent();
                break;
            default:
        }
    }
}
