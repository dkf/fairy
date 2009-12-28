/*
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
*/
package com.mewmew.fairy.v1.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Simple class that turn any JSON Array in a stream into an iterator.
 * @param <T>
 */
public class JsonArrayIterator<T> implements Iterator<T>
{
    T curr;
    final JsonParser parser;
    final Class<T> clazz;

    public JsonArrayIterator(JsonParser parser, Class<T> clazz) throws IOException
    {
        this.parser = parser;
        this.clazz = clazz;
        // skip to the nearest array
        while (parser.getCurrentToken() != JsonToken.START_ARRAY && parser.nextToken() != JsonToken.END_OBJECT) ;
        parser.nextToken();
        next();
    }

    public boolean hasNext()
    {
        return curr != null;
    }

    public T next()
    {
        try {
            return curr;
        }
        finally {
            try {
                curr = parser.readValueAs(clazz);
            }
            catch (IOException e) {
                curr = null;
                throw new RuntimeException(e);
            }
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
