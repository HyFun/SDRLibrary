package com.sdr.lib.ui.tree;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2018/1/3.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface TreeNodeIsLeaf {
}
