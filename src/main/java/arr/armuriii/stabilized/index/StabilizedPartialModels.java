package arr.armuriii.stabilized.index;

import arr.armuriii.stabilized.Stabilized;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class StabilizedPartialModels {
    public static final PartialModel
            STABILIZER_BEARING_COG = block("stabilizer_bearing/brass_cog");


    private static PartialModel block(final String path) {
        return PartialModel.of(Stabilized.path("block/" + path));
    }

    private static PartialModel entity(final String path) {
        return PartialModel.of(Stabilized.path("entity/" + path));
    }

    private static PartialModel item(final String path) {
        return PartialModel.of(Stabilized.path("item/" + path));
    }

    public static void init() {
        // init static fields
    }
}
