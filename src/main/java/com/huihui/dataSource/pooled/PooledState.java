package com.huihui.dataSource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/6/29 0029.
 */
public class PooledState {
    protected  List<PooledConnection> activePool = new ArrayList<>();
    protected  List<PooledConnection> idlePool = new ArrayList<>();
}
