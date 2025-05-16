package su.foxocorp.experiment.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.client.events.*;
import su.foxocorp.experiment.common.ServerEventPayload;

public class ServerEventHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(Experiment.MOD_ID);

    public void handleEvent(ServerEventPayload payload) {
        LOGGER.info("Received server event: {}", payload.eventType());

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
            case "sendMessageToActionBar":
                SendMessageToActionBarEvent.handleEvent(payload.args());
                break;
            case "changeRenderDistanceEvent":
                ChangeRenderDistanceEvent.changeRenderDistance(Float.parseFloat(payload.args()));
                break;
            case "hideTabListEvent":
                HideTabListEvent.handleEvent();
                break;
            default:
        }
    }
}
