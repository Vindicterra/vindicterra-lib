package org.vindicterra.vindicterralib.serialization;

import java.io.*;

/**
 * Generic implementation of Java object serialization.
 * Objects to be serialized must implement the "java.io.Serializable" interface.
 * For a higher-level PDC serializer, check {@link SerializeToPDC}
 */
public class SerializeToBytes {
    /**
     *
     * @param obj The Object to serialize
     * @return  A byte[] representing the serialized object
     * @throws IOException (I've never had it raise this, so I don't know what would cause it)
     */
    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    /**
     *
     * @param bytes The byte[] to attempt to deserialize into an Object
     * @return  If successful, the deserialized Object
     * @throws IOException (I've never had it raise this, so I don't know what would cause it)
     * @throws ClassNotFoundException If the serialized data does not contain a Java class
     */
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }
}
