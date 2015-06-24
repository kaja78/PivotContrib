/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pivot_contrib.guicy_apache_pivot;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.serialization.Serializer;

/**
 * Use this class instead of the standard BXMLSerializer in order to have Guice create the GUI classes.
 *
 * @author TEyckmans
 */
public class GuicyBXMLSerializer extends BXMLSerializer
{
    private final Injector injector;

    public GuicyBXMLSerializer(Injector injector)
    {
        if (injector == null) {
            throw new IllegalArgumentException("injector was null!");
        }

        this.injector = injector;
    }

    public GuicyBXMLSerializer(Module module)
    {
        if (module == null) {
            throw new IllegalArgumentException("module was null!");
        }

        injector = Guice.createInjector(module);
    }

    /**
     * Make sure that when a new BXMLSerializer instance is needed, we create a new GuicyBXMLSerializer instead.
     *
     * Note: Opted not to change the static mimetypes map in the BXMLSerializer.
     *
     * @param type
     * The type of serializer being requested.
     */
    @Override
    protected Serializer<?> newIncludeSerializer(Class<? extends Serializer<?>> type)
            throws InstantiationException, IllegalAccessException
    {
        if (BXMLSerializer.class.isAssignableFrom(type)) {
            return new GuicyBXMLSerializer(injector);
        }
        else {
            return super.newIncludeSerializer(type);
        }
    }

    /**
     * Asks Guice for a new instance of the requested type.
     *
     * @param type
     * The type of object being requested.
     */
    @Override
    protected Object newTypedObject(Class<?> type)
            throws InstantiationException, IllegalAccessException
    {
        return injector.getInstance(type);
    }
}
