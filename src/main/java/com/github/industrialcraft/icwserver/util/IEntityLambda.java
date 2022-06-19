package com.github.industrialcraft.icwserver.util;

import com.github.industrialcraft.icwserver.world.entity.Entity;

public interface IEntityLambda<T extends Entity> {
    void exec(T entity);
}
