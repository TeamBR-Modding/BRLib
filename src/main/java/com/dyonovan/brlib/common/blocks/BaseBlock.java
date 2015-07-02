package com.dyonovan.brlib.common.blocks;

import com.dyonovan.brlib.collections.BlockTextures;
import com.dyonovan.brlib.common.blocks.rotation.IRotation;
import com.dyonovan.brlib.common.blocks.rotation.NoRotation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BaseBlock extends BlockContainer {
    protected String blockName;
    protected Class<? extends TileEntity> tileEntity;

    @SideOnly(Side.CLIENT)
    protected BlockTextures textures;

    /**
     * Used as a common class for all blocks. Makes things a bit easier
     * @param mat What material the block should be
     * @param name The unlocalized name of the block, include mod name
     * @param tile Should the block have a tile, pass the class
     */
    public BaseBlock(Material mat, String name, Class<? extends TileEntity> tile) {
        super(mat);
        blockName = name;
        tileEntity = tile;

        this.setBlockName(blockName);
        this.setCreativeTab(getCreativeTabToDisplayOn());
        this.setHardness(getHardness());
    }

    /**
     * Used to change the hardness of a block, but will default to 2.0F if not overwritten
     * @return The hardness value, default 2.0F
     */
    protected float getHardness() {
        return 2.0F;
    }

    /**
     * Used to tell if this should be in a creative tab, and if so which one
     * @return Null if none, defaults to the main Modular Systems Tab
     */
    protected CreativeTabs getCreativeTab() {
        return CreativeTabs.tabBlock;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        generateDefaultTextures(iconRegister);
    }

    /**
     * Used to add the textures for the block. Uses block name by default
     *
     * Initialize the {@link BlockTextures} object here
     * @param iconRegister Icon Registry
     */
    public void generateDefaultTextures(IIconRegister iconRegister) {
        textures = new BlockTextures(iconRegister, blockName);
    }

    /**
     * Used to set the values needed for the block's rotation on placement
     * @return The rotation class needed, none by default
     */
    protected IRotation getDefaultRotation() {
        return new NoRotation();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        //Calls upon the default rotation to set the meta
        world.setBlockMetadataWithNotify(x, y, z, getDefaultRotation().getMetaFromEntity(livingBase), 2);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if(tileEntity != null) {
            try{
                return tileEntity.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}