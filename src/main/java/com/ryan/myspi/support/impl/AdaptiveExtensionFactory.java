/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ryan.myspi.support.impl;


import com.ryan.Adaptive;
import com.ryan.myspi.ExtensionLoader;
import com.ryan.myspi.support.ExtensionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AdaptiveExtensionFactory
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {

    private final List<ExtensionFactory> factories;


    // 通过newInstance调用该无参构造函数
    public AdaptiveExtensionFactory() {
        // 通过代理获取ExtensionFactory扩展点实现类(通过将自适应扩展点实现类和自适应扩展点实现类进行区分实现代理,
        // 通过ExtensionLoader类中的injectedCachedInstances 和 cachedAdaptiveInstance对两种扩展点实现类进行区分，
        // ExtensionLoader中的getExtensionLoader和getAdaptiveExtension方法的区别，严格遵守了单一职责原则
        ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
        List<ExtensionFactory> list = new ArrayList<ExtensionFactory>();
        for (String name : loader.getSupportedExtensions()) {
            list.add(loader.getExtension(name));
        }
        factories = Collections.unmodifiableList(list);
    }

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        // 在所有的ExtensionFactory中进行查查找，查到后直接返回，如果没有查到，返回null值
        for (ExtensionFactory factory : factories) {
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        try {
            AdaptiveExtensionFactory adaptiveExtensionFactory = AdaptiveExtensionFactory.class.newInstance ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
