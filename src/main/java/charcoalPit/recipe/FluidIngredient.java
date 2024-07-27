package charcoalPit.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidIngredient {
	
	public Fluid fluid;
	public Tag<Fluid> tag;
	public int amount;
	public CompoundTag nbt;
	
	public boolean test(Fluid in) {
		if(fluid!=null&&fluid==in)
			return true;
		if(tag!=null&&tag.contains(in))
			return true;
		return false;
	}
	
	public Fluid getFluid() {
		if(fluid!=null&&fluid!=Fluids.EMPTY)
			return fluid;
		if(tag!=null&&!tag.getValues().isEmpty())
			return tag.getValues().get(0);
		return Fluids.EMPTY;
	}
	
	public boolean isEmpty() {
		return getFluid()==Fluids.EMPTY;
	}
	
	public static FluidIngredient readJson(JsonObject json){
		FluidIngredient ingredient=new FluidIngredient();
		if(json.has("fluid")) {
			ResourceLocation f=new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
			Fluid fluid=ForgeRegistries.FLUIDS.getValue(f);
			if(fluid!=null&&fluid!=Fluids.EMPTY)
				ingredient.fluid=fluid;
		}
		if(json.has("tag")) {
			ResourceLocation t=new ResourceLocation(GsonHelper.getAsString(json, "tag"));
			Tag<Fluid> tag=SerializationTags.getInstance().getFluids().getTag(t);
			if(tag!=null&&!tag.getValues().isEmpty())
				ingredient.tag=tag;
		}
		if(ingredient.isEmpty())
			throw new JsonParseException("invalid fluid");
		if(json.has("amount")) {
			ingredient.amount=GsonHelper.getAsInt(json, "amount");
		}
		if(json.has("nbt")) {
			try {
				ingredient.nbt=TagParser.parseTag(GsonHelper.getAsString(json, "nbt"));
			} catch (CommandSyntaxException e) {
				throw new JsonParseException(e);
			}
		}
		return ingredient;
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		int mode=0;
		if(fluid!=null)
			mode+=1;
		if(tag!=null)
			mode+=2;
		buffer.writeByte(mode);
		if((mode&1)==1) {
			buffer.writeResourceLocation(fluid.getRegistryName());
		}
		if((mode&2)==2) {
			buffer.writeResourceLocation(SerializationTags.getInstance().getFluids().getIdOrThrow(tag));
		}
		buffer.writeInt(amount);
		if(nbt!=null) {
			buffer.writeBoolean(true);
			buffer.writeNbt(nbt);
		}else {
			buffer.writeBoolean(false);
		}
	}
	
	public static FluidIngredient readBuffer(FriendlyByteBuf buffer) {
		FluidIngredient ingredient=new FluidIngredient();
		int mode=buffer.readByte();
		if((mode&1)==1) {
			ingredient.fluid=ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());
		}
		if((mode&2)==2) {
			ingredient.tag=SerializationTags.getInstance().getFluids().getTag(buffer.readResourceLocation());
		}
		ingredient.amount=buffer.readInt();
		if(buffer.readBoolean()) {
			ingredient.nbt=buffer.readNbt();
		}
		return ingredient;
	}
	
	
}
