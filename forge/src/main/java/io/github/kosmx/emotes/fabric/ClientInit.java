package io.github.kosmx.emotes.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.kosmx.emotes.fabric.executor.FabricClientMethods;
import io.github.kosmx.emotes.fabric.gui.screen.ingame.FastChosseScreen;
import io.github.kosmx.emotes.fabric.network.ClientNetworkInstance;
import io.github.kosmx.emotes.main.network.ClientEmotePlay;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class ClientInit {

    static KeyMapping openMenuKey;
    static KeyMapping stopEmote;
    static Consumer<Minecraft> keyBindingFunction;

    static void initClient(){

        initKeyBinding();

        ClientNetworkInstance.networkInstance.init(); //init network
        /*
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            FabricClientMethods.tick++;

            keyBindingFunction.accept(client);
        });

         */
    }

    @SubscribeEvent
    public void endClientTick(TickEvent.ClientTickEvent event){
        FabricClientMethods.tick++;
    }

    @SubscribeEvent
    public void keyListenerEvent(InputEvent.KeyInputEvent event){
        keyBindingFunction.accept(null);
    }


    private static void initKeyBinding(){
        openMenuKey = new KeyMapping("key.emotecraft.fastchoose", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, "category.emotecraft.keybinding");
        //KeyBindingRegistryImpl.registerKeyBinding(openMenuKey);
        ClientRegistry.registerKeyBinding(openMenuKey);

        stopEmote = new KeyMapping("key.emotecraft.stop", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.emotecraft.keybinding");
        ClientRegistry.registerKeyBinding(stopEmote);

        keyBindingFunction = client -> {

            if(openMenuKey.consumeClick()){
                if(Minecraft.getInstance().player == Minecraft.getInstance().getCameraEntity()){
                    Minecraft.getInstance().setScreen(new FastChosseScreen(null));
                }
            }
            if(stopEmote.consumeClick()){
                ClientEmotePlay.clientStopLocalEmote();
            }
        };
    }
}
