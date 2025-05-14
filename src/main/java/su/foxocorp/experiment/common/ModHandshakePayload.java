package su.foxocorp.experiment.common;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import su.foxocorp.experiment.Experiment;

public record ModHandshakePayload(String version) implements CustomPayload {
    public static final Identifier ID_IDENTIFIER = Identifier.of(Experiment.MOD_ID, "client_handshake");

    public static final CustomPayload.Id<ModHandshakePayload> ID = new CustomPayload.Id<>(ID_IDENTIFIER);

    public static final PacketCodec<RegistryByteBuf, ModHandshakePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING,
            ModHandshakePayload::version,
            ModHandshakePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
