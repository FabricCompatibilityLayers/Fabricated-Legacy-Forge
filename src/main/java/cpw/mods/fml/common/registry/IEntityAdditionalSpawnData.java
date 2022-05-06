package cpw.mods.fml.common.registry;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface IEntityAdditionalSpawnData {
    void writeSpawnData(ByteArrayDataOutput byteArrayDataOutput);

    void readSpawnData(ByteArrayDataInput byteArrayDataInput);
}
