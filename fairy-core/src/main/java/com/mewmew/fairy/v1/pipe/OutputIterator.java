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
package com.mewmew.fairy.v1.pipe;

import java.util.Iterator;
import java.util.concurrent.Exchanger;

public class OutputIterator<T> implements Output<T>, Iterator<T>
{
    private volatile boolean isClosed = false ;
    private final Exchanger<T> exchanger = new Exchanger<T>();

    public void output(T obj)
    {
        try {
            exchanger.exchange(obj);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void close()
    {
        isClosed = true ;
    }

    public boolean hasNext()
    {
        return !isClosed;
    }

    public T next()
    {
        try {
            return exchanger.exchange(null);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
