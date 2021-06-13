package charcoalPit.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BlockMainBloomery extends Block {
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 2);

    public BlockMainBloomery() {
        super(Properties.create(Material.ROCK, MaterialColor.ADOBE).hardnessAndResistance(2, 6).setRequiresTool().harvestLevel(0).harvestTool(ToolType.PICKAXE));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        int i = state.get(STAGE);
        if (i == 0)
            return 0;
        else if (i == 1)
            return 9;
        else return 15;
    }
}
