package dev.dontblameme.utilsapi.events.event;

import dev.dontblameme.utilsapi.main.Main;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class EventUtils {

    private EventUtils() {}

    /**
     *
     * @param clazz Spigot-Event-Class which should be listened for
     * @param consumer Consumer for the event
     * @param priority Priority for the event
     * @apiNote This is a simple method of registering events. You need to call the method this event is put into, and then it will be called automatically without needing to add a @EventHandler or registering it manually
     */
    public static <T extends Event> void registerEvent(Class<T> clazz, Consumer<T> consumer, EventPriority priority) {
        JavaPlugin.getProvidingPlugin(Main.class).getServer().getPluginManager().registerEvent(clazz, new Listener() {}, priority, (listener, event) -> consumer.accept((T) event), JavaPlugin.getProvidingPlugin(Main.class));
    }

    /**
     *
     * @param clazz Spigot-Event-Class which should be listened for
     * @param consumer Consumer for the event
     * @apiNote This is a simple method of registering events. You need to call the method this event is put into, and then it will be called automatically without needing to add a @EventHandler or registering it manually
     */
    public static <T extends Event> void registerEvent(Class<T> clazz, Consumer<T> consumer) {
        EventUtils.registerEvent(clazz, consumer, EventPriority.NORMAL);
    }
}
