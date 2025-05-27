package su.foxocorp.experiment.client.event;

import com.mojang.authlib.GameProfile;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientRegistries;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.session.Session;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.StatHandler;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import su.foxocorp.experiment.client.ExperimentClient;

import java.util.UUID;

public class TestEvent {

    public static void handle() {
        MinecraftClient client = ExperimentClient.client;
    }
}