package com.zhekasmirnov.apparatus.ecs.entity;

import com.zhekasmirnov.apparatus.ecs.core.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import java.util.*;

public class TickingComponent extends EntityComponent
{
    private int age;
    private int frame;
    private int loadStatusCheckRate;
    private LoadedStatusProviderComponent loadedStatusProvider;
    private final Set<IUpdatableComponent> updatableComponents;
    
    public TickingComponent() {
        this.updatableComponents = new LinkedHashSet<IUpdatableComponent>();
        this.loadedStatusProvider = null;
        this.loadStatusCheckRate = -1;
        this.frame = 0;
        this.age = 0;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public int getFrame() {
        return this.frame;
    }
    
    @Override
    protected void onActivated() {
        super.onActivated();
        this.updatableComponents.clear();
        this.updatableComponents.addAll(this.getEntity().getComponents(IUpdatableComponent.class));
    }
    
    @Override
    protected void onAdded() {
        super.onAdded();
        this.loadedStatusProvider = this.getEntity().getComponent(LoadedStatusProviderComponent.class);
        int desiredCheckRate;
        if (this.loadedStatusProvider != null) {
            desiredCheckRate = this.loadedStatusProvider.getDesiredCheckRate();
        }
        else {
            desiredCheckRate = -1;
        }
        this.loadStatusCheckRate = desiredCheckRate;
        this.frame = 0;
    }
    
    public void onBaseTick() {
        while (true) {
            while (true) {
                Label_0166: {
                    try {
                        if (this.loadStatusCheckRate > 0 && this.frame % this.loadStatusCheckRate == 0 && !this.loadedStatusProvider.isLoaded()) {
                            this.getEntity().setActive(false);
                            return;
                        }
                        final Iterator<IUpdatableComponent> iterator = this.updatableComponents.iterator();
                        if (!iterator.hasNext()) {
                            ++this.frame;
                            ++this.age;
                            return;
                        }
                        final IUpdatableComponent updatableComponent = iterator.next();
                        if (((EntityComponent)updatableComponent).isActive()) {
                            updatableComponent.onTick();
                            break Label_0166;
                        }
                        break Label_0166;
                    }
                    catch (Throwable t) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Error occurred in component ");
                        sb.append(this.getClass().getCanonicalName());
                        sb.append(" of entity ");
                        sb.append(this.getEntity());
                        UserDialog.insignificantError(sb.toString(), t, true);
                        this.setActive(false);
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    @Override
    public void onComponentAddedToEntity(final EntityComponent entityComponent) {
        if (entityComponent instanceof IUpdatableComponent) {
            this.updatableComponents.add((IUpdatableComponent)entityComponent);
        }
    }
    
    @Override
    public void onComponentRemovedFromEntity(final EntityComponent entityComponent) {
        if (entityComponent instanceof IUpdatableComponent) {
            this.updatableComponents.remove(entityComponent);
        }
    }
}
