package net.lyof.phantasm.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;

public class VividNihilisBlock extends BushBlock {
    public VividNihilisBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return simpleCodec(VividNihilisBlock::new);
    }
}
