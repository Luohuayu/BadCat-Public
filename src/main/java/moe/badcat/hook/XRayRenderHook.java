package moe.badcat.hook;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.pipeline.ForgeBlockModelRenderer;

public class XRayRenderHook extends ForgeBlockModelRenderer {
    public XRayRenderHook(BlockColors blockColorsIn) {
        super(blockColorsIn);
    }

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer, boolean checkSides)  {
        if (Block.getIdFromBlock(blockStateIn.getBlock()) <= 13 || blockStateIn.getBlock() instanceof BlockLeaves) return false;
        return super.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, buffer, false);
    }
}
