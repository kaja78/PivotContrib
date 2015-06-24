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
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author TEyckmans
 */
public class GuicyBXMLSerializerTest
{

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullInjector()
    {
        final Injector nullInjector = null;

        new GuicyBXMLSerializer(nullInjector);

        fail("null injector should throw IllegalArgumentException");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullModule()
    {
        final Module nullModule = null;

        new GuicyBXMLSerializer(nullModule);

        fail("null module should throw IllegalArgumentException");
    }

    @Test
    public void testSharedObject() throws Exception
    {
        final Injector injector = Guice.createInjector(new GuicyBXMLSerializerTestModule());
        final GuicyBXMLSerializer guicyBXMLSerializer = new GuicyBXMLSerializer(injector);

        final GuicyBXMLSerializerTestWindow window = (GuicyBXMLSerializerTestWindow)guicyBXMLSerializer.readObject(getClass().getResource("GuicyBXMLSerializerTest_Window.bxml"));

        assertEquals(
                window.getPanelOne().getSharedObject(),
                window.getPanelTwo().getSharedObject(),
                "shared object is different");
    }


}
