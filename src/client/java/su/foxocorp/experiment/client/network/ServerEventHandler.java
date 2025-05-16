package su.foxocorp.experiment.client.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.foxocorp.experiment.Experiment;
import su.foxocorp.experiment.client.event.*;
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
                ChangeWindowTitleEvent.handle(payload.args());
                break;
            case "testEvent":
                TestEvent.handle();
                break;
            case "sendMessageToActionBar":
                SendMessageToActionBarEvent.handle(payload.args());
                break;
            case "changeRenderDistanceEvent":
                ChangeRenderDistanceEvent.handle(Float.parseFloat(payload.args()));
                break;
            case "hideTabListEvent":
                HideTabListEvent.handle();
                break;
            default:
        }
    }
}
