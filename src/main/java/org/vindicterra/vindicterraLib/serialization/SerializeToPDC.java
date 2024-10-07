package org.vindicterra.vindicterraLib.serialization;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Utility for serializing an Object into a Player's PDC
 */
public class SerializeToPDC {
    /**
     *
     * @param pdc The PersistentDataContainer to serialize the Object to
     * @param key The NamespacedKey to serialize the Object under
     * @param obj The Object to serialize
     * @throws RuntimeException if the serializer raises IOException - see serialization.SerializeToBytes
     */
    public static void serialize(PersistentDataContainer pdc, NamespacedKey key, Object obj) {
        byte[] bytes;
        try {
            bytes = SerializeToBytes.serialize(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pdc.set(key, PersistentDataType.BYTE_ARRAY, bytes);
    }

    /**
     *
     * @param pdc The PersistentDataContainer to deserialize from
     * @param key The NamespacedKey to deserialize from
     * @throws RuntimeException if the serializer raises IOException - see serialization.SerializeToBytes
     * @return `null` if the object was unable to be deserialized or the location in PDC was null. Otherwise, the Object
     */
    public static @Nullable Object deserialize(PersistentDataContainer pdc, NamespacedKey key) {
        byte[] bytes = pdc.get(key, PersistentDataType.BYTE_ARRAY);
        if (bytes == null) return null;
        try {
            return SerializeToBytes.deserialize(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
