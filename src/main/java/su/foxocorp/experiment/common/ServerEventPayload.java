package su.foxocorp.experiment.common;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import su.foxocorp.experiment.Experiment;

public record ServerEventPayload(String eventType, String args) implements CustomPayload {

    public static final Identifier ID_IDENTIFIER = Identifier.of(Experiment.MOD_ID, "server_event");

    public static final CustomPayload.Id<ServerEventPayload> ID = new CustomPayload.Id<>(ID_IDENTIFIER);

    public static final PacketCodec<RegistryByteBuf, ServerEventPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            ServerEventPayload::eventType,
            PacketCodecs.STRING,
            ServerEventPayload::args,
            ServerEventPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}