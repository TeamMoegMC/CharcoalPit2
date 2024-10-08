package charcoalPit.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public class BlockMainBloomery extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);

    public BlockMainBloomery() {
        super(Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(4, 8).requiresCorrectToolForDrops());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    public int getLightValue(BlockState state, BlockGetter world, BlockPos pos) {
        int i = state.getValue(STAGE);
        if (i == 0)
            return 0;
        else if (i == 1)
            return 9;
        else return 15;
    }
}
