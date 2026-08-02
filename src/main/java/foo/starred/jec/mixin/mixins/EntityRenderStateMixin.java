package foo.starred.jec.mixin.mixins;

import foo.starred.jec.accessors.EntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAccessor {

    @Unique
    private Entity athen$entity;

    @Override
    public Entity jec$getEntity() {
        return this.athen$entity;
    }

    @Override
    public void jec$setEntity(Entity entity) {
        this.athen$entity = entity;
    }
}