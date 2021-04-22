package me.xa5.smoothbedrock.mixin;

import me.xa5.smoothbedrock.SmoothBedrock;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;

@Mixin(NoiseChunkGenerator.class)
public abstract class MixinFlatBedrock extends ChunkGenerator {
    @Shadow
    @Final
    protected Supplier<DimensionSettings> field_236080_h_;
    @Shadow
    @Final
    private int /*field_24779*/ field_236085_x_; // worldHeight

    //    @Shadow
//    @Final
//    /*protected ChunkRandom random;*/
//    protected final SharedSeedRandom randomSeed;
    ThreadLocal<ResourceLocation> dimId = new ThreadLocal<>();

    public MixinFlatBedrock(BiomeProvider biomeSource_1, DimensionStructuresSettings chunkGeneratorConfig_1) {
        super(biomeSource_1, chunkGeneratorConfig_1);
    }

    @Inject(method = "generateSurface", at = @At("HEAD"), cancellable = true)
    public void test(WorldGenRegion region, IChunk chunk, CallbackInfo info) {
        // TODO why is this deprecated? Maybe there's a better way to get the ID.
        dimId.set(region.getWorld().getDimensionKey().getLocation());
    }

    @Inject(method = "makeBedrock", at = @At("HEAD"), cancellable = true)
    private void makeBedrock(IChunk chunkIn, Random rand, CallbackInfo info) {
        if (SmoothBedrock.shouldModifyBedrock(dimId.get())) {
            info.cancel();

            BlockPos.Mutable mutable = new BlockPos.Mutable();
            int chunkStartX = chunkIn.getPos().getXStart();
            int chunkStartZ = chunkIn.getPos().getZStart();

            int bedrockFloor = this.field_236080_h_.get().getBedrockFloorPosition();
            int bedrockRoof = field_236085_x_ - 1 - this.field_236080_h_.get().getBedrockRoofPosition();
            boolean generateRoof = bedrockRoof + 4 >= 0 && bedrockRoof < this.field_236085_x_;
            boolean generateFloor = bedrockFloor + 4 >= 0 && bedrockFloor < this.field_236085_x_;

            if (generateFloor || generateRoof) {
                Iterator<BlockPos> chunkBlocks = BlockPos.getAllInBoxMutable(chunkStartX, 0, chunkStartZ, chunkStartX + 15, 0, chunkStartZ + 15).iterator();
                while (true) {
                    BlockPos blockPos;
                    do {
                        if (!chunkBlocks.hasNext()) {
                            return;
                        }

                        blockPos = chunkBlocks.next();
                        if (generateRoof) {
                            chunkIn.setBlockState(mutable.setPos(blockPos.getX(), bedrockRoof, blockPos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
                        }
                    } while (!generateFloor);

                    chunkIn.setBlockState(mutable.setPos(blockPos.getX(), bedrockFloor, blockPos.getZ()), Blocks.BEDROCK.getDefaultState(), false);
                }
            }
        }
    }
}