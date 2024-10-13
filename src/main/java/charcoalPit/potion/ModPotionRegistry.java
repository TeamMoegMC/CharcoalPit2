package charcoalPit.potion;

import charcoalPit.CharcoalPit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.RegisterEvent;

public class ModPotionRegistry {

    public static MobEffect DRUNK=new MobEffect(MobEffectCategory.HARMFUL,0xFFFFFF){
        @Override
        public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
            amplifier-=3;
            if(amplifier>=1){
                entityLivingBaseIn.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20*5));
            }
            if(amplifier>=2){
                entityLivingBaseIn.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20*5));
                entityLivingBaseIn.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20*5));
                entityLivingBaseIn.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20*5));
            }
            if(amplifier>=3){
                entityLivingBaseIn.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20*5));
                entityLivingBaseIn.addEffect(new MobEffectInstance(MobEffects.POISON, 20*5));
            }
            if(amplifier>=4){
                entityLivingBaseIn.hurt(entityLivingBaseIn.level().damageSources().generic(), (amplifier-3)*2);
            }
        }

        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }

        @Override
        public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
            super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
            if(amplifier>0)
                entityLivingBaseIn.addEffect(new MobEffectInstance(DRUNK, 20*60*5, amplifier-1));
        }
    };
    public static MobEffect ROULETTE=new MobEffect(MobEffectCategory.NEUTRAL, 0x00FF00){
        @Override
        public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
            Random random=new Random();
            Potion potion=ForgeRegistries.POI_TYPES.getValues().toArray(new Potion[]{})[random.nextInt(ForgeRegistries.POI_TYPES.getValues().size())];
            for(MobEffectInstance effect:potion.getEffects()){
                if(effect.getEffect().isInstantenous()){
                    effect.getEffect().applyInstantenousEffect(source, indirectSource ,entityLivingBaseIn, effect.getAmplifier(), health);
                }else{
                    entityLivingBaseIn.addEffect(new MobEffectInstance(effect));
                }
            }
        }

        @Override
        public boolean isInstantenous() {
            return true;
        }
    };
    public static MobEffect POISON_RESISTANCE=new MobEffect(MobEffectCategory.BENEFICIAL, 0xFFFF00){
        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }

        @Override
        public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
            entityLivingBaseIn.removeEffect(MobEffects.POISON);
        }
    };
    public static MobEffect WITHER_RESISTANCE=new MobEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF){
        @Override
        public boolean isDurationEffectTick(int duration, int amplifier) {
            int j = 25 >> amplifier;
            if (j > 0) {
                return duration % j == 0;
            } else {
                return true;
            }
        }
        @Override
        public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
            entityLivingBaseIn.removeEffect(MobEffects.WITHER);
        }
    };
    public static Potion CIDER=new Potion("cider",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.REGENERATION,20*45));
    public static Potion GOLDEN_CIDER=new Potion("golden_cider",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.ABSORPTION,20*60*8,4));
    public static Potion VODKA=new Potion("vodka",
            new MobEffectInstance(DRUNK, 20*60*5),
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,20*60*3));
    public static Potion BEETROOT_BEER=new Potion("beetroot_beer",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.DAMAGE_BOOST,20*60*3));
    public static Potion SWEETBERRY_WINE=new Potion("sweetberry_wine",
            new MobEffectInstance(DRUNK, 20*60*5),
            new MobEffectInstance(MobEffects.JUMP,20*60*3,1));
    public static Potion MEAD=new Potion("mead",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(POISON_RESISTANCE,20*60*3));
    public static Potion BEER=new Potion("beer",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.SATURATION,1,5));
    public static Potion RUM=new Potion("rum",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED,20*60*3));
    public static Potion CHORUS_CIDER=new Potion("chorus_cider",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.LEVITATION,20*20),
            new MobEffectInstance(MobEffects.SLOW_FALLING,20*30));
    public static Potion SPIDER_SPIRIT=new Potion("spider_spirit",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(ROULETTE));
    public static Potion HONEY_DEWOIS=new Potion("honey_dewois",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(MobEffects.DIG_SPEED,20*60*3));
    public static Potion WARPED_WINE=new Potion("warped_wine",
            new MobEffectInstance(DRUNK,20*60*5),
            new MobEffectInstance(WITHER_RESISTANCE,20*60*3));


    public static void registerEffects(RegisterEvent event){
//        event.getRegistry().registerAll(DRUNK.setRegistryName("drunk"),ROULETTE.setRegistryName("roulette"),POISON_RESISTANCE.setRegistryName("poison_resistance"),
//                WITHER_RESISTANCE.setRegistryName("wither_resistance"));
        event.register(ForgeRegistries.Keys.MOB_EFFECTS,
                helper -> {
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "drunk"), DRUNK);
                    helper.register(new ResourceLocation(CharcoalPit.MODID,"roulette"), ROULETTE);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "poison_resistance"), POISON_RESISTANCE);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "wither_resistance"), WITHER_RESISTANCE);
                }
        );
    }
    public static void registerPotions(RegisterEvent event){
//        event.getRegistry().registerAll(CIDER.setRegistryName("cider"),GOLDEN_CIDER.setRegistryName("golden_cider"),VODKA.setRegistryName("vodka"),
//                BEETROOT_BEER.setRegistryName("beetroot_beer"),SWEETBERRY_WINE.setRegistryName("sweetberry_wine"),MEAD.setRegistryName("mead"),
//                BEER.setRegistryName("beer"),RUM.setRegistryName("rum"),CHORUS_CIDER.setRegistryName("chorus_cider"),SPIDER_SPIRIT.setRegistryName("spider_spirit"),
//                HONEY_DEWOIS.setRegistryName("honey_dewois"),WARPED_WINE.setRegistryName("warped_wine"));
        event.register(ForgeRegistries.Keys.POTIONS,
                helper -> {
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "cider"), CIDER);
                    helper.register(new ResourceLocation(CharcoalPit.MODID,"golden_cider"), GOLDEN_CIDER);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "vodka"), VODKA);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "beetroot_beer"), BEETROOT_BEER);
                    helper.register(new ResourceLocation(CharcoalPit.MODID,"sweetberry_wine"), SWEETBERRY_WINE);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "mead"), MEAD);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "beer"), BEER);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "rum"), RUM);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "chorus_cider"), CHORUS_CIDER);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "spider_spirit"), SPIDER_SPIRIT);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "honey_dewois"), HONEY_DEWOIS);
                    helper.register(new ResourceLocation(CharcoalPit.MODID, "warped_wine"), WARPED_WINE);
                }
        );
    }

}
